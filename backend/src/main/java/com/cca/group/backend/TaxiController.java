package com.cca.group.backend;

import org.apache.hadoop.hbase.HBaseConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/taxi")
public class TaxiController {
	
	@Autowired
	RestTemplate restTemplate;
	
	@RequestMapping("/estimate")
	public String taxiEstimate() {
		return "taxi estimate";
	}

}
