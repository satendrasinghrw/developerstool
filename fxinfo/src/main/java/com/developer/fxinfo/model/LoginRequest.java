package com.developer.fxinfo.model;

import java.util.Arrays;

public class LoginRequest {
	private final String userName;
	private final char[] password;
	
	private LoginRequest () {
		this.userName = null;
		this.password = null;
	}
	
	private LoginRequest (final String userName, final char[] password) {
		this.userName = userName;
		this.password = password;
	}

	public String getUserName() {
		return userName;
	}

	public String getPassword() {
		return new String(password);
	}

	@Override
	public String toString() {
		return "LoginRequest [userName=" + userName + ", password="
				+ Arrays.toString(password) + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(password);
		result = prime * result
				+ ((userName == null) ? 0 : userName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LoginRequest other = (LoginRequest) obj;
		if (!Arrays.equals(password, other.password))
			return false;
		if (userName == null) {
			if (other.userName != null)
				return false;
		} else if (!userName.equals(other.userName))
			return false;
		return true;
	}
	
	public static class LoginBuilder {
		private String userName;
		private String password;
		
		public LoginBuilder() {
			
		}
		
		public LoginBuilder setUserName(String userName) {
			this.userName = userName;
			return this;
		}
		
		public LoginBuilder setPassword(final String password) {
			this.password = password;
			return this;
		}
		
		public LoginRequest build() {
			return new LoginRequest(userName, password.toCharArray());
		}
	}
}
