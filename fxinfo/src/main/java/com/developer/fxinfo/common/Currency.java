package com.developer.fxinfo.common;

import java.util.ArrayList;
import java.util.List;

public enum Currency {
	EUR("EUR", "eur dollar", 1.0F),
	USD("USD", "US dollar", 1.1035F),
	JPY("JPY", "Japanese yen", 120.96F),
	BGN("BGN", "Bulgarian lev",	1.9558F),
	CZK("CZK", "Czech koruna", 25.160F),
	DKK("DKK", "Danish krone", 7.4730F),
	GBP("GBP", "Pound sterling", 0.84313F),
	HUF("HUF", "Hungarian forint", 336.01F),
	PLN("PLN", "Polish zloty", 4.2565F),
	RON("RON", "Romanian leu", 4.7799F),
	SEK("SEK", "Swedish krona", 10.5363F),
	CHF("CHF", "Swiss franc", 1.0712F),
	ISK("ISK", "Icelandic krona", 137.40F),
	NOK("NOK", "Norwegian krone", 9.9375F),
	HRK("HRK", "Croatian kuna", 7.4415F),
	RUB("RUB", "Russian rouble", 68.1692F),
	TRY("TRY", "Turkish lira", 6.5539F),
	AUD("AUD", "Australian dollar", 1.6127F),
	BRL("BRL", "Brazilian real", 4.6084F),
	CAD("CAD", "Canadian dollar", 1.4491F),
	CNY("CNY", "Chinese yuan renminbi", 7.6509F),
	HKD("HKD", "Hong Kong dollar", 8.5770F),
	IDR("IDR", "Indonesian rupiah", 15002.91F),
	ILS("ILS", "Israeli shekel", 3.8100F),
	INR("INR", "Indian rupee", 78.6700F),
	KRW("KRW", "South Korean won", 1289.12F),
	MXN("MXN", "Mexican peso", 20.7170F),
	MYR("MYR", "Malaysian ringgit", 4.4857F),
	NZD("NZD", "New Zealand dollar", 1.6684F),
	PHP("PHP", "Philippine peso", 56.092F),
	SGD("SGD", "Singapore dollar", 1.4910F),
	THB("THB", "Thai baht", 33.723F),
	ZAR("ZAR", "South African rand", 15.8585F);
	
	private String code;
	private String name;
	private float rate;
	
	Currency(String code, String name, float ratePerEUR) {
		this.code = code;
		this.name = name;
		this.rate = ratePerEUR;
	}
	
	public static List<String> getCurrencyPair() {
		final List<String> currencyPairs = new ArrayList<>();
		final Currency[] curriencies = Currency.values();
		final int len = curriencies.length;
		
		for (int i=0; i < len; i++) {
			for (int j=0; j<len; j++) {
				if (i != j) { 
					currencyPairs.add(curriencies[i].code+"/"+curriencies[j].code); 
				}
			}
		}	
		
		return currencyPairs;
	}
	
	@Override
	public String toString() {
		return "Currency [code="+code+", name="+name+", rate="+rate+"]";
	}

	public float getRate() {
		return rate;
	}
}
