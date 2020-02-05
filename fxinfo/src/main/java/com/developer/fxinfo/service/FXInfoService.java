package com.developer.fxinfo.service;

import com.developer.fxinfo.model.SpotRate;

public interface FXInfoService {

	SpotRate getSpotRate(String currencyPair);

}
