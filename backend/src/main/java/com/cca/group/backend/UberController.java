package com.cca.group.backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/uber")
public class UberController {
	
	@Value("${uber.api.token}")
	private String uberToken;
	
	@Autowired
	RestTemplate restTemplate;
	
	@RequestMapping("/estimate")
	public String uberEstimate() {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("Authorization", uberToken);
		HttpEntity <String> entity = new HttpEntity<String>(headers);
		
		return restTemplate.exchange("https://api.uber.com/v1.2/estimates/price?start_latitude=37.7752315&start_longitude=-122.418075&end_latitude=37.7752415&end_longitude=-122.518075", 
				HttpMethod.GET, entity, String.class).getBody();
	}

}
