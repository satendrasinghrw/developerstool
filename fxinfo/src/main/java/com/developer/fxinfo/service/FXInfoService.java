package com.ubs.fxinfo.service;

import com.ubs.fxinfo.model.SpotRate;

public interface FXInfoService {

	SpotRate getSpotRate(String currencyPair);

}
