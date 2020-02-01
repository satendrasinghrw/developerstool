package com.ubs.fxinfo.service;

import com.ubs.fxinfo.model.LoginRequest;
import com.ubs.fxinfo.model.SignupRequest;

public interface LoginService {

	String processSignup(SignupRequest request);

	String processLogin(LoginRequest request);

}
