package com.ubs.fxinfo.model;

public class SpotRate {
	
	private String currencyPair;
	private float spotRate;
	
	public SpotRate(String currencyPair, float spotRate) {
		this.currencyPair = currencyPair;
		this.spotRate = spotRate;
	}
	
	public String getCurrencyPair() {
		return currencyPair;
	}
	public void setCurrencyPair(String currencyPair) {
		this.currencyPair = currencyPair;
	}
	public float getSpotRate() {
		return spotRate;
	}
	public void setSpotRate(float spotRate) {
		this.spotRate = spotRate;
	}
	@Override
	public String toString() {
		return "SpotRate [currencyPair=" + currencyPair + ", spotRate="
				+ spotRate + "]";
	}

}
