package com.ubs.fxinfo.controller;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ubs.fxinfo.filedatabase.AuditDatabase;
import com.ubs.fxinfo.filedatabase.AuditDatabase.AuditRecord;
import com.ubs.fxinfo.model.LoginRequest;
import com.ubs.fxinfo.model.SignupRequest;
import com.ubs.fxinfo.service.LoginService;
import com.ubs.fxinfo.service.Statistics;

@RestController
@RequestMapping(path = "/login")
public final class LoginController {

	private static final Logger log = LoggerFactory
			.getLogger(LoginController.class);

	@Autowired
	private LoginService loginService;

	@PostMapping("/signup")
	public ResponseEntity<String> signupRequest(@RequestBody SignupRequest.SignupBuilder signup) {

		final SignupRequest request = signup.build();

		log.info("received signup request: {}", request);

		final ResponseEntity<String> response = loginService.processSignup(request);

		return response;
	}

	@PostMapping(path = "/signin")
	public ResponseEntity<String> signinRequest(@RequestBody LoginRequest.LoginBuilder user) {

		final LoginRequest request = user.build();

		log.info("received login request: {}", request);
		
		final ResponseEntity<String> response = loginService.processLogin(request);

		return response;
	}
	
	@GetMapping(path = "/signout")
	public ResponseEntity<String> signoutRequest(@RequestHeader(value = "auth") String auth) {

		log.info("received signout request");
		
		String response = Statistics.INSTANCE.removeLiveUser(auth);
		
		return new ResponseEntity<String>(response, HttpStatus.OK);
	}

	@GetMapping("/audit")
	public ResponseEntity<List<AuditRecord>> getAuditHistry() {

		log.info("received audit request");

		final AuditDatabase auditDB = Statistics.INSTANCE.getAuditDB();
		ResponseEntity<List<AuditRecord>> response = null;
		try {
			response = auditDB.getAuditRecords();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return response;
	}
}
