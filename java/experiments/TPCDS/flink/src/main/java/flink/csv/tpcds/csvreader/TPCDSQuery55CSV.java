/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package flink.csv.tpcds.csvreader;


import flink.utilities.Util;
import org.apache.flink.api.common.functions.FilterFunction;
import org.apache.flink.api.common.functions.JoinFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.aggregation.Aggregations;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.api.java.tuple.Tuple4;

import java.io.IOException;

/**
 * This program implements a modified version of the TPC-DS query 55. The
 * example demonstrates how to assign names to fields by extending the Tuple class.
 * The original query can be found at
 * <a href="http://www.tpc.org/tpc_documents_current_versions/pdf/tpcds_1.3.1.pdf">http://www.tpc
 * .org/tpc_documents_current_versions/pdf/tpcds_1.3.1.pdf</a>.
 * <p/>
 * <p/>
 * This program implements the following SQL equivalent:
 * <p/>
 * <p/>
 * <code><pre>
 * select  i_brand_id brand_id, i_brand brand,
 * sum(ss_ext_sales_price) ext_price
 * from date_dim, store_sales, item
 * where date_dim.d_date_sk = store_sales.ss_sold_date_sk
 * and store_sales.ss_item_sk = item.i_item_sk
 * and i_manager_id=28
 * and d_moy=11
 * and d_year=1999
 * group by i_brand, i_brand_id
 * order by ext_price desc, i_brand_id
 * limit 100 ;
 * </pre></code>
 * <p/>
 * <p/>
 * Compared to the original TPC-DS query this version does not sort and does limit the result.
 * <p/>
 * <p/>
 * Input files are plain text CSV files using the pipe character ('|') as field separator
 * as generated by the TPC-DS data generator which is available at <a href="http://www.tpc.org/tpcds/">http://www.tpc
 * .org/tpcds/</a>.
 * <p/>
 * <p/>
 * Usage: <code>TPCDSQuery55CSV &lt;date_dim-csv path&gt; &lt;store_sales-csv path&gt; &lt;item-csv path&gt; &lt;
 * result path&gt;</code><br>
 * <p/>
 * <p/>
 * This example shows how to use:
 * <ul>
 * <li> custom data type derived from tuple data types
 * <li> inline-defined functions
 * <li> build-in aggregation functions
 * </ul>
 */
@SuppressWarnings("serial")
public class TPCDSQuery55CSV {

	// *************************************************************************
	//     PROGRAM
	// *************************************************************************

	public static void main(String[] args) throws Exception {

		long startTime = System.currentTimeMillis();

		if (!parseParameters(args)) {
			return;
		}

		final ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();

		// get input data
		DataSet<DateDim> dataDims = getDataDimDataSet(env);
		DataSet<Item> item = getItemDataSet(env);
		DataSet<StoreSales> storeSales = getStoreSalesDataSet(env);

		dataDims = dataDims.filter(
			new FilterFunction<DateDim>() {
				public boolean filter(DateDim d) {
					return d.getD_moy() == 11L && d.getD_year() == 1999L;
				}
			});

		item = item.filter(
			new FilterFunction<Item>() {
				public boolean filter(Item i) {
					return i.getI_manager_id() == 28L;
				}
			});

		dataDims.join(storeSales).where(0).equalTo(0).with(new DataDimAndStoreSales())
			.join(item).where(1).equalTo(0).with(new DataDimAndStoreSalesAndItems())
			.groupBy(1, 0).aggregate(Aggregations.SUM, 2)
			.print();

		// execute program
		env.execute("TPC-DS Query 55 Example with CSV input");


		System.out.println("Execution time: " + (System.currentTimeMillis() - startTime));
	}

	public static class DataDimAndStoreSales implements JoinFunction<DateDim, StoreSales, Tuple2<Double, Long>> {

		public Tuple2<Double, Long> join(DateDim d, StoreSales s) {
			return new Tuple2<Double, Long>(s.getSs_ext_sales_price(), s.getSs_item_sk());
		}
	}

	public static class DataDimAndStoreSalesAndItems implements JoinFunction<Tuple2<Double, Long>, Item, Tuple3<Long, 
		String, Double>> {

		public Tuple3<Long, String, Double> join(Tuple2<Double, Long> twoTables, Item i) {
			return new Tuple3<Long, String, Double>(i.getI_brand_id(), i.getI_brand(), twoTables.f0);
		}
	}


	// *************************************************************************
	//     UTIL METHODS
	// *************************************************************************

	private static String dateDimPath;
	private static String storeSalesPath;
	private static String itemPath;

	private static boolean parseParameters(String[] programArguments) {

		if (programArguments.length > 0) {
			if (programArguments.length == 3) {
				dateDimPath = programArguments[0];
				storeSalesPath = programArguments[1];
				itemPath = programArguments[2];
			} else {
				System.err.println("Usage: TPCDSQuery55CSV <date_dim-csv path> <store_sales-csv path> <item-csv " +
					"path>");
				return false;
			}
		} else {
			System.err.println("This program expects data from the TPC-DS benchmark as input data.\n" +
				"  Due to legal restrictions, we can not ship generated data.\n" +
				"  You can find the TPC-DS data generator at http://www.tpc.org/tpcds/.\n" +
				"  Usage: TPCDSQuery55CSV <date_dim-csv path> <store_sales-csv path> <item-csv path>");
			return false;
		}
		return true;
	}

	private static final class MapItem implements MapFunction<ItemString, Item> {
		public Item map(ItemString value) {
			Item tuple = new Item();
			tuple.f0 = Util.emptyStringToLongZero(value.f0);
			tuple.f1 = Util.emptyStringToLongZero(value.f1);
			tuple.f2 = value.f2;
			tuple.f3 = Util.emptyStringToLongZero(value.f3);
			return tuple;
		}
	}

	private static final class MapDateDim implements MapFunction<DateDimString, DateDim> {
		public DateDim map(DateDimString value) {
			DateDim tuple = new DateDim();
			tuple.f0 = Util.emptyStringToLongZero(value.f0);
			tuple.f1 = Util.emptyStringToLongZero(value.f1);
			tuple.f2 = Util.emptyStringToLongZero(value.f2);
			return tuple;
		}
	}

	private static final class MapStoreSales implements MapFunction<StoreSalesString, StoreSales> {
		public StoreSales map(StoreSalesString value) {
			StoreSales tuple = new StoreSales();
			tuple.f0 = Util.emptyStringToLongZero(value.f0);
			tuple.f1 = Util.emptyStringToLongZero(value.f1);
			tuple.f2 = Util.emptyStringToDoubleZero(value.f2);
			return tuple;
		}
	}

	private static DataSet<DateDim> getDataDimDataSet(ExecutionEnvironment env) throws IOException {
		return env.readCsvFile(dateDimPath)
			.fieldDelimiter('|')
			.includeFields("1000001010000000000000000000")
			.tupleType(DateDimString.class)
			.map(new MapDateDim());
	}

	private static DataSet<StoreSales> getStoreSalesDataSet(ExecutionEnvironment env) throws IOException {
		return env.readCsvFile(storeSalesPath)
			.fieldDelimiter('|')
			.includeFields("10100000000000010000000")
			.tupleType(StoreSalesString.class)
			.map(new MapStoreSales());
	}

	private static DataSet<Item> getItemDataSet(ExecutionEnvironment env) throws IOException {
		return env.readCsvFile(itemPath)
			.fieldDelimiter('|')
			.includeFields("1000000110000000000010")
			.tupleType(ItemString.class)
			.map(new MapItem());
	}


	public static class ItemString extends Tuple4<String, String, String, String> {
	}

	public static class StoreSalesString extends Tuple3<String, String, String> {
	}

	public static class DateDimString extends Tuple3<String, String, String> {
	}

	public static class DateDim extends Tuple3<Long, Long, Long> {

		public Long getD_date_sk() {
			return this.f0;
		}

		public Long getD_year() {
			return this.f1;
		}

		public Long getD_moy() {
			return this.f2;
		}
	}

	public static class StoreSales extends Tuple3<Long, Long, Double> {

		public Long getSs_sold_date_sk() {
			return this.f0;
		}

		public Long getSs_item_sk() {
			return this.f1;
		}

		public Double getSs_ext_sales_price() {
			return this.f2;
		}
	}

	public static class Item extends Tuple4<Long, Long, String, Long> {

		public Long getI_item_sk() {
			return this.f0;
		}

		public Long getI_brand_id() {
			return this.f1;
		}

		public String getI_brand() {
			return this.f2;
		}

		public Long getI_manager_id() {
			return this.f3;
		}
	}
}
