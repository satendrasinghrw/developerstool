package com.ubs.fxinfo.service;

import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.ubs.fxinfo.common.Utils;
import com.ubs.fxinfo.filedatabase.AuditData;
import com.ubs.fxinfo.filedatabase.AuditDatabase;
import com.ubs.fxinfo.filedatabase.UserData;

public enum Statistics {

	INSTANCE;
	
	private final Map<String, UserData> liveUserMap;
	private final AuditDatabase auditDB;
	
	Statistics() {
		auditDB = new AuditDatabase(".", "rw");
		liveUserMap = new HashMap<>();
	}
	
	public void addLiveUser(UserData data) {
		liveUserMap.put(data.getAccessToken(), data);
	}
	
	public Map<String, UserData> getLiveUsers() {
		return Collections.unmodifiableMap(liveUserMap);
	}
	
	public String getUserId(String authToken) {
		if(liveUserMap.containsKey(authToken)) {
			return liveUserMap.get(authToken).getUserId();
		}
		return null;
	}
	
	public AuditDatabase getAuditDB() {
		return auditDB;
	}

	public void addAuditRecord(String auth, String event) throws IOException {
		auditDB.writeToFile(new AuditData(getUserId(auth), event, Utils.getCurrentDateTime()));
	}
}
