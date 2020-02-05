package com.developer.fxinfo.service;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.developer.fxinfo.common.Utils;
import com.developer.fxinfo.filedatabase.AuditData;
import com.developer.fxinfo.filedatabase.AuditDatabase;
import com.developer.fxinfo.filedatabase.UserData;

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

	public void addAuditRecord(String userId, String event) throws IOException {
		auditDB.writeToFile(new AuditData(userId, event, Utils.getCurrentDateTime()));
	}

	public String removeLiveUser(String authToken) {
		if(liveUserMap.containsKey(authToken)) {
			liveUserMap.remove(authToken);
			return "User logged out"; 
		} else {
			return "User not found to remove";
		}
	}
}
