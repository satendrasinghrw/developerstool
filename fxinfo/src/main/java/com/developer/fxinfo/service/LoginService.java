package com.ubs.fxinfo.service;

import org.springframework.http.ResponseEntity;

import com.ubs.fxinfo.model.LoginRequest;
import com.ubs.fxinfo.model.SignupRequest;

public interface LoginService {

	ResponseEntity<String> processSignup(SignupRequest request);

	ResponseEntity<String> processLogin(LoginRequest request);

}
