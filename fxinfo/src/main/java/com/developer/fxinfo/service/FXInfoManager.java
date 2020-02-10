package com.developer.fxinfo.service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.developer.fxinfo.common.Currency;
import com.developer.fxinfo.common.MajorCurrencyPair;
import com.developer.fxinfo.model.SpotRate;

@Service
public class FXInfoManager implements FXInfoService {

	private static final Logger log = LoggerFactory
			.getLogger(FXInfoManager.class);

	public FXInfoManager() {

	}
	
	@Override
	public SpotRate getSpotRate(String currencyPair) {

		if(currencyPair != null && currencyPair.length() == 6) {
			Currency base = Currency.valueOf(currencyPair.substring(0, 3));
			Currency underlying = Currency.valueOf(currencyPair.substring(3));
			return new SpotRate(currencyPair, underlying.getRate()/base.getRate());
		} else {
			log.info("invalid currency pair {}",currencyPair);
			return null;
		}
	}

}
