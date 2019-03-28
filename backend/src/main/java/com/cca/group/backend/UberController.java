package com.cca.group.backend;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/uber")
public class UberController {

	@Value("${uber.api.token}")
	private String uberToken;

	@Value("${uber.api.url.price}")
	private String priceUrl;
	
	@Value("${uber.api.url.time}")
	private String timeUrl;

	@Autowired
	RestTemplate restTemplate;

	Logger logger = LoggerFactory.getLogger(UberController.class);
	
	@RequestMapping("/estimate/price")
	public String uberPriceEstimate(@RequestParam(value = "startLat", required = true) String startLat,
			@RequestParam(value = "startLong", required = true) String startLong,
			@RequestParam(value = "endLat", required = true) String endLat,
			@RequestParam(value = "endLong", required = true) String endLong) {

		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(priceUrl).queryParam("start_latitude", startLat)
				.queryParam("start_longitude", startLong).queryParam("end_latitude", endLat)
				.queryParam("end_longitude", endLong);

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("Authorization", uberToken);
		HttpEntity<String> entity = new HttpEntity<String>(headers);
		
		logger.debug("Executing url {}", builder.toUriString());

		return restTemplate.exchange(builder.toUriString(), HttpMethod.GET, entity, String.class).getBody();
	}
	
	@RequestMapping("/estimate/time")
	public String uberTimeEstimate(@RequestParam(value = "startLat", required = true) String startLat,
			@RequestParam(value = "startLong", required = true) String startLong) {

		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(timeUrl).queryParam("start_latitude", startLat)
				.queryParam("start_longitude", startLong);

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("Authorization", uberToken);
		HttpEntity<String> entity = new HttpEntity<String>(headers);
		
		logger.debug("Executing url {}", builder.toUriString());

		return restTemplate.exchange(builder.toUriString(), HttpMethod.GET, entity, String.class).getBody();
	}

}
