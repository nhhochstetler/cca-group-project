package com.cca.group.backend;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.MasterNotRunningException;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.ZooKeeperConnectionException;
import org.apache.hadoop.hbase.client.HBaseAdmin;
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
	public HBaseAdmin hbaseConfig() throws MasterNotRunningException, ZooKeeperConnectionException, IOException {
		logger.debug("Initializing HBase connection");
		Configuration conf = HBaseConfiguration.create();
		HBaseAdmin admin = new HBaseAdmin(conf);
		
		createTable(admin);
		loadData();
		
		return admin;
	}
	
	private void createTable(HBaseAdmin admin) throws IOException {
		logger.debug("Creating the taxi table");
		
		HTableDescriptor taxiDescriptor = admin.getTableDescriptor(TableName.valueOf("green_taxi"));
		
		if (taxiDescriptor == null) {
			logger.debug("Table {} already exists. No need to create table", taxiDescriptor.getNameAsString());
			
			return;
		}
		
		taxiDescriptor = new HTableDescriptor(TableName.valueOf("green_taxi"));
		
		taxiDescriptor.addFamily(new HColumnDescriptor("vendorID"));
		taxiDescriptor.addFamily(new HColumnDescriptor("pickupTime"));
		taxiDescriptor.addFamily(new HColumnDescriptor("dropoffTime"));
		taxiDescriptor.addFamily(new HColumnDescriptor("storeFwdFlag"));
		taxiDescriptor.addFamily(new HColumnDescriptor("ratecodeID"));
		taxiDescriptor.addFamily(new HColumnDescriptor("pickupLocationID"));
		taxiDescriptor.addFamily(new HColumnDescriptor("dropoffLocationID"));
		taxiDescriptor.addFamily(new HColumnDescriptor("passengerCount"));
		taxiDescriptor.addFamily(new HColumnDescriptor("tripDistance"));
		taxiDescriptor.addFamily(new HColumnDescriptor("fareAmount"));
		taxiDescriptor.addFamily(new HColumnDescriptor("extra"));
		taxiDescriptor.addFamily(new HColumnDescriptor("mtaTax"));
		taxiDescriptor.addFamily(new HColumnDescriptor("tipAmount"));
		taxiDescriptor.addFamily(new HColumnDescriptor("tollsAmount"));
		taxiDescriptor.addFamily(new HColumnDescriptor("ehailFee"));
		taxiDescriptor.addFamily(new HColumnDescriptor("improvementSurcharge"));
		taxiDescriptor.addFamily(new HColumnDescriptor("totalAmount"));
		taxiDescriptor.addFamily(new HColumnDescriptor("paymentType"));
		taxiDescriptor.addFamily(new HColumnDescriptor("tripType"));
		
		admin.createTable(taxiDescriptor);
	}
	
	private void loadData() throws IOException {
		logger.debug("Populating the taxi tables with data");
		
		//For now, just getting one months data
		BufferedReader br = new BufferedReader(new FileReader("../data/green_tripdata_2018-01.csv"));
		String input;
		int count = 0;
		while ((input = br.readLine()) != null) {
			String[] vals = input.split(",");
			
			Put p = new Put(Bytes.toBytes("row" + count));
			
			System.out.println(input);
			
			count++;
		}
	}
}