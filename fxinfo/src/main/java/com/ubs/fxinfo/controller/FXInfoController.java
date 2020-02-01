package com.ubs.fxinfo.controller;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scripting.support.StaticScriptSource;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ubs.fxinfo.model.SpotRate;
import com.ubs.fxinfo.service.FXInfoService;
import com.ubs.fxinfo.service.Statistics;

@RestController
@RequestMapping("/fx")
@CrossOrigin(origins = "http://192.168.10.89:5502")
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
	public ResponseEntity<SpotRate> getSpotRate(
			@PathVariable(value = "currencypair") String currencyPair, @RequestHeader(value = "auth") String auth) {

		log.info("received currency pair for spot rate: {}", currencyPair);
		
		try {
			Statistics.INSTANCE.addAuditRecord(auth, "spoRate");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		final SpotRate fxrate = fxinfoService.getSpotRate(currencyPair);
		
		return new ResponseEntity<SpotRate>(fxrate, HttpStatus.OK);
	}
}
