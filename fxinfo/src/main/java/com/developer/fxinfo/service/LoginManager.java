package com.ubs.fxinfo.service;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.ubs.fxinfo.common.Utils;
import com.ubs.fxinfo.filedatabase.AuditData;
import com.ubs.fxinfo.filedatabase.FileDatabase;
import com.ubs.fxinfo.filedatabase.UserData;
import com.ubs.fxinfo.model.LoginRequest;
import com.ubs.fxinfo.model.SignupRequest;

@Service
public class LoginManager implements LoginService {

	private final long DATA_MAPPED_SIZE = 1024*100L;
	private final long METADATA_MAPPED_SIZE = 1024*10L;

	private final FileDatabase db;
	
	public LoginManager () {
		db = new FileDatabase(DATA_MAPPED_SIZE, METADATA_MAPPED_SIZE, ".", "rw");
	}
	
	@Override
	public ResponseEntity<String> processSignup(SignupRequest request) {
		final boolean allowed = !db.isUserPresent(request.getEmailId());
		if (allowed) {
			UserData data = new UserData(request);
			try {
				db.writeToFile(request.getEmailId(), data.byteArray());
			} catch (IOException e) {
				e.printStackTrace();
				return new ResponseEntity<String>("User creation failed, Please contact system admin.",HttpStatus.FORBIDDEN);
			}
		} else {
			return new ResponseEntity<String>(request.getEmailId() +" user already exist, please try with different emailId", HttpStatus.NOT_ACCEPTABLE);
		}
		return new ResponseEntity<String>(request.getEmailId() +" user successfully created!", HttpStatus.CREATED);
	}

	@Override
	public ResponseEntity<String> processLogin(LoginRequest request) {
		
		try {
			final byte []data = db.readFromFile(request.getUserName());
			if (data == null ) {
				return new ResponseEntity<String>(request.getUserName()+" user does not exist", HttpStatus.BAD_REQUEST);
			} else {
				final UserData userData = new UserData(data);
			
				if (request.getPassword() == null || !request.getPassword().equals(userData.getPassword())) {
					return new ResponseEntity<String>("invalid userid or password", HttpStatus.UNAUTHORIZED);
				} else {
					Statistics.INSTANCE.addLiveUser(userData);
					Statistics.INSTANCE.getAuditDB().writeToFile(
							new AuditData(userData.getUserId(), "Login", Utils.getCurrentDateTime()));
					return new ResponseEntity<String>("You are logged in successfully, your access code is ["+userData.getAccessToken()+"]", HttpStatus.OK);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new ResponseEntity<String>("unknow error, please contact to system admin", HttpStatus.FORBIDDEN);
	}
}
