package com.developer.fxinfo.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.developer.fxinfo.model.SpotRate;
import com.developer.fxinfo.service.FXInfoService;
import com.developer.fxinfo.service.Statistics;

@RestController
@RequestMapping("/fx")
@Api(value="FX Spot Rate", description="Returns the spot rate for given currency pair")
@SwaggerDefinition(tags = @Tag(name = ""))
public final class FXInfoController {

	private static final Logger log = LoggerFactory
			.getLogger(FXInfoController.class);

	@Autowired
	private FXInfoService fxinfoService;

	/**
	 * facilitate end user to get the spot rate for supplied currency pair
	 * @param currencyPair required param to get spot rate
	 * @return SpotRate
	 */
	@GetMapping("/rate/{currencypair}")
	@ApiOperation(value = "Get spot rate for given currency pair")
	public ResponseEntity<SpotRate> getSpotRate(
			@PathVariable(value = "currencypair") String currencyPair, @RequestHeader(value = "auth") String auth) {

		log.info("received currency pair for spot rate: {}", currencyPair);
		
		try {
			final String userId = Statistics.INSTANCE.getUserId(auth);
			if (userId == null) {
				SpotRate sr = new SpotRate("NA", -1);
				sr.setRemark("User not logged in currently for supplied auth code");
				log.error("{}",sr.getRemark());
				return new ResponseEntity<SpotRate>(sr, HttpStatus.UNAUTHORIZED);
			}
			Statistics.INSTANCE.addAuditRecord(userId, "spoRate");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		final SpotRate fxrate = fxinfoService.getSpotRate(currencyPair);
		
		return new ResponseEntity<SpotRate>(fxrate, HttpStatus.OK);
	}
}
