package com.ubs.fxinfo.common;


import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class Utils {

	private static final ZoneId zone = ZoneId.systemDefault();
	
	private static final DateTimeFormatter pattern = DateTimeFormatter
			.ofPattern("yyyyMMdd-HH:mm:ss.SSS");

	private Utils() {

	}

	public static ZonedDateTime getDateTime() {
		return Instant.now().atZone(zone);
	}
	
	public static long getDateTimeEpochSecond() {
		return Instant.now().atZone(zone).toEpochSecond();
	}
	
	public static String getCurrentDateTime() {
		return Instant.now().atZone(zone).format(pattern);
	}
	
	public static ZonedDateTime getDateTime(long epochMillis) {
		return Instant.ofEpochMilli(epochMillis).atZone(zone);
	}

	public static String getStrDateTime(long epochMillis) {
		return Instant.ofEpochMilli(epochMillis).atZone(zone).format(pattern);
	}

}
