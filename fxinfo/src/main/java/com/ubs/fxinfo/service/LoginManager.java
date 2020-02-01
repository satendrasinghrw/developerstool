package com.ubs.fxinfo.service;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.ubs.fxinfo.common.Utils;
import com.ubs.fxinfo.filedatabase.AuditData;
import com.ubs.fxinfo.filedatabase.FileDatabase;
import com.ubs.fxinfo.filedatabase.UserData;
import com.ubs.fxinfo.model.LoginRequest;
import com.ubs.fxinfo.model.SignupRequest;

@Service
public class LoginManager implements LoginService {

	private final Logger log = LoggerFactory
			.getLogger(LoginManager.class);

	private final long DATA_MAPPED_SIZE = 1024*100L;
	private final long METADATA_MAPPED_SIZE = 1024*10L;

	private final FileDatabase db;
	
	public LoginManager () {
		db = new FileDatabase(DATA_MAPPED_SIZE, METADATA_MAPPED_SIZE, ".", "rw");
	}
	
	@Override
	public String processSignup(SignupRequest request) {
		final boolean allowed = !db.isUserPresent(request.getEmailId());
		if (allowed) {
			UserData data = new UserData(request);
			try {
				db.writeToFile(request.getEmailId(), data.byteArray());
			} catch (IOException e) {
				e.printStackTrace();
				return "User creation failed, Please contact system admin.";
			}
		} else {
			return request.getEmailId() +" user already exist, please try with different emailId";
		}
		return request.getEmailId() +" user successfully created!";
	}

	@Override
	public String processLogin(LoginRequest request) {
		
		try {
			final byte []data = db.readFromFile(request.getUserName());
			if (data == null ) {
				return request.getUserName()+" user does not exist";
			} else {
				final UserData userData = new UserData(data);
			
				if (request.getPassword() == null || !request.getPassword().equals(userData.getPassword())) {
					return "invalid userid or password";
				} else {
					Statistics.INSTANCE.addLiveUser(userData);
					Statistics.INSTANCE.getAuditDB().writeToFile(
							new AuditData(userData.getUserId(), "Login", Utils.getCurrentDateTime()));
					return "You are logged in successfully, your access code is ["+userData.getAccessToken()+"]";
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "unknow error, please contact to system admin";
	}
}
