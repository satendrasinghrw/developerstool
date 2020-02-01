package com.ubs.fxinfo.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ubs.fxinfo.filedatabase.AuditDatabase;
import com.ubs.fxinfo.filedatabase.AuditDatabase.AuditHistory;
import com.ubs.fxinfo.filedatabase.AuditDatabase.AuditRecord;
import com.ubs.fxinfo.model.LoginRequest;
import com.ubs.fxinfo.model.SignupRequest;
import com.ubs.fxinfo.model.SpotRate;
import com.ubs.fxinfo.service.LoginService;
import com.ubs.fxinfo.service.Statistics;

@RestController
@RequestMapping(path = "/login")
@CrossOrigin(origins = "http://192.168.10.89:5502")
public final class LoginController {

	private static final Logger log = LoggerFactory
			.getLogger(LoginController.class);

	@Autowired
	private LoginService loginService;

	@PostMapping("/signup")
	public ResponseEntity<String> signupRequest(
			@RequestBody SignupRequest.SignupBuilder signup) {

		final SignupRequest request = signup.build();

		log.info("received signup request: {}", request);

		final String response = loginService.processSignup(request);

		return new ResponseEntity<String>(response, HttpStatus.OK);
	}

	@PostMapping(path = "/signin")
	public ResponseEntity<String> signinRequest(
			@RequestBody LoginRequest.LoginBuilder user) {

		final LoginRequest request = user.build();

		log.info("received login request: {}", request);
		
		final String response = loginService.processLogin(request);

		return new ResponseEntity<String>(response, HttpStatus.OK);
	}

	@GetMapping("/audit")
	public ResponseEntity<List<AuditRecord>> getAuditHistry() {

		log.info("received audit request");

		final AuditDatabase auditDB = Statistics.INSTANCE.getAuditDB();
		List<AuditRecord> audit = null;
		try {
			audit = auditDB.getAuditRecords();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new ResponseEntity<List<AuditRecord>>(audit, HttpStatus.OK);
	}
}
