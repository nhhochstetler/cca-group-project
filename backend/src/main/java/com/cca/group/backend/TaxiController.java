package com.cca.group.backend;

import java.io.IOException;
import java.util.Date;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.util.Bytes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/taxi")
public class TaxiController {

	@Autowired
	RestTemplate restTemplate;

	@Autowired
	Configuration conf;

	Logger logger = LoggerFactory.getLogger(UberController.class);

	@RequestMapping("/averagePrice")
	public String averagePrice(@RequestParam(value = "date", required = true) Date date,
			@RequestParam(value = "distance", required = true) String startLong) throws IOException {
		
		HTable table = new HTable(conf, "green_taxi");
		
		Scan scan = new Scan();
		
		scan.addColumn(Bytes.toBytes("pickup"), Bytes.toBytes("pickupTime"));
		scan.addColumn(Bytes.toBytes("cost_fees"), Bytes.toBytes("totalAmount"));
		
		ResultScanner scanner = table.getScanner(scan);
		
		for (Result result = scanner.next(); result != null; result = scanner.next()) {
			String dateValue = Bytes.toString(result.getValue(Bytes.toBytes("pickup"), Bytes.toBytes("pickupTime")));
			String totalValue = Bytes.toString(result.getValue(Bytes.toBytes("cost_fees"), Bytes.toBytes("totalAmount")));
			
			logger.debug("Read {} {}", dateValue, totalValue);
		}
		return "";

	}

}