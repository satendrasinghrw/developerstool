package com.developer.fxinfo.service;

import org.springframework.http.ResponseEntity;

import com.developer.fxinfo.model.LoginRequest;
import com.developer.fxinfo.model.SignupRequest;

public interface LoginService {

	ResponseEntity<String> processSignup(SignupRequest request);

	ResponseEntity<String> processLogin(LoginRequest request);

}
