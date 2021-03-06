package com.cca.group.backend;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.MasterNotRunningException;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.ZooKeeperConnectionException;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.util.Bytes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
  
@SpringBootApplication
public class BackendApplication {

	Logger logger = LoggerFactory.getLogger(BackendApplication.class);
	public static void main(String[] args) {
		SpringApplication.run(BackendApplication.class, args);
	}

	@Bean
	public RestTemplate getRestTemplate() {
		return new RestTemplate();
	}
	
	@Bean
	public Configuration hbaseConfig() throws MasterNotRunningException, ZooKeeperConnectionException, IOException {
		logger.debug("Initializing HBase connection");
		Configuration conf = HBaseConfiguration.create();
		HBaseAdmin admin = new HBaseAdmin(conf);
		
		if (!admin.tableExists(TableName.valueOf("green_taxi"))) {
			createTable(admin);
			loadData(conf);
		} else {
			logger.debug("Taxi table already exists. No need to create it");
		}
		
		return conf;
	}
	
	private void createTable(HBaseAdmin admin) throws IOException {
		logger.debug("Creating the taxi table");
		
		HTableDescriptor taxiDescriptor = new HTableDescriptor(TableName.valueOf("green_taxi"));
		
		taxiDescriptor.addFamily(new HColumnDescriptor("pickup"));
		taxiDescriptor.addFamily(new HColumnDescriptor("dropoff"));
		taxiDescriptor.addFamily(new HColumnDescriptor("trip_data"));
		taxiDescriptor.addFamily(new HColumnDescriptor("cost_fees"));
		
		admin.createTable(taxiDescriptor);
	}
	
	@SuppressWarnings("deprecation")
	private void loadData(Configuration conf) throws IOException {
		logger.debug("Populating the taxi tables with data");
		
		HTable table = new HTable(conf, "green_taxi");
		
		//For now, just getting one months data
		BufferedReader br = new BufferedReader(new FileReader("/cca-group-project/data/green_tripdata_2018-01.csv"));
		String str;
		
		br.readLine();
		
		int count = 0;
		while ((str = br.readLine()) != null) {
			logger.debug("Reading line {}", str);
			String[] vals = str.split(",");
			
			if (vals.length > 18) {
				Put p = new Put(Bytes.toBytes(vals[1].split(" ")[0]));
				
				p.add(Bytes.toBytes("pickup"),
						Bytes.toBytes("pickupTime"),
						Bytes.toBytes(vals[1]));
				
				p.add(Bytes.toBytes("pickup"),
						Bytes.toBytes("pickupLocationID"),
						Bytes.toBytes(vals[5]));
				
				p.add(Bytes.toBytes("dropoff"),
						Bytes.toBytes("dropoffTime"),
						Bytes.toBytes(vals[2]));
				
				p.add(Bytes.toBytes("dropoff"),
						Bytes.toBytes("dropoffLocationID"),
						Bytes.toBytes(vals[6]));
				
				p.add(Bytes.toBytes("trip_data"),
						Bytes.toBytes("tripDistance"),
						Bytes.toBytes(vals[4]));
				
				p.add(Bytes.toBytes("trip_data"),
						Bytes.toBytes("passengerCount"),
						Bytes.toBytes(vals[3]));
				
				p.add(Bytes.toBytes("cost_fees"),
						Bytes.toBytes("totalAmount"),
						Bytes.toBytes(vals[16]));
				
				table.put(p);
				count++;
			}
		}
	}
}