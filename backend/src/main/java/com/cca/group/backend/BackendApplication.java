package com.cca.group.backend;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.MasterNotRunningException;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.ZooKeeperConnectionException;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
  
@SpringBootApplication
public class BackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(BackendApplication.class, args);
	}

	@Bean
	public RestTemplate getRestTemplate() {
		return new RestTemplate();
	}
	
	@Bean
	public HBaseAdmin hbaseConfig() throws MasterNotRunningException, ZooKeeperConnectionException, IOException {
		Configuration conf = HBaseConfiguration.create();
		HBaseAdmin admin = new HBaseAdmin(conf);
		
		createTable(admin);
		
		return admin;
	}
	
	private void createTable(HBaseAdmin admin) throws IOException {
		HTableDescriptor taxiDescriptor = new HTableDescriptor(TableName.valueOf("taxi"));
		taxiDescriptor.addFamily(new HColumnDescriptor("green"));
		admin.createTable(taxiDescriptor);
	}

}
