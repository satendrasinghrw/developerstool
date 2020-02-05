package com.developer.fxinfo.model;

import java.util.Arrays;

public class SignupRequest {
	
	private final String firstName;
	private final String lastName;
	private final String emailId;
	private final char[] password;
	private final String companyName;
	private final String mobileNumber;
	
	private SignupRequest (
			final String firstName, 
			final String lastName, 
			final String emailId, 
			final char[] password,
			final String companyName,
			final String mobileNumber) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.emailId = emailId;
		this.password = password;
		this.companyName = companyName;
		this.mobileNumber = mobileNumber;
	}
	
	private SignupRequest () {
		this.firstName = null;
		this.lastName = null;
		this.emailId = null;
		this.password = null;
		this.companyName = null;
		this.mobileNumber = null;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public String getCompanyName() {
		return companyName;
	}

	public String getEmailId() {
		return emailId;
	}

	public String getPassword() {
		return new String(password);
	}

	public String getMobileNumber() {
		return mobileNumber;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((companyName == null) ? 0 : companyName.hashCode());
		result = prime * result + ((emailId == null) ? 0 : emailId.hashCode());
		result = prime * result
				+ ((firstName == null) ? 0 : firstName.hashCode());
		result = prime * result
				+ ((lastName == null) ? 0 : lastName.hashCode());
		result = prime * result
				+ ((mobileNumber == null) ? 0 : mobileNumber.hashCode());
		result = prime * result + Arrays.hashCode(password);
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
		SignupRequest other = (SignupRequest) obj;
		if (companyName == null) {
			if (other.companyName != null)
				return false;
		} else if (!companyName.equals(other.companyName))
			return false;
		if (emailId == null) {
			if (other.emailId != null)
				return false;
		} else if (!emailId.equals(other.emailId))
			return false;
		if (firstName == null) {
			if (other.firstName != null)
				return false;
		} else if (!firstName.equals(other.firstName))
			return false;
		if (lastName == null) {
			if (other.lastName != null)
				return false;
		} else if (!lastName.equals(other.lastName))
			return false;
		if (mobileNumber == null) {
			if (other.mobileNumber != null)
				return false;
		} else if (!mobileNumber.equals(other.mobileNumber))
			return false;
		if (!Arrays.equals(password, other.password))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "SignupRequest [firstName=" + firstName + ", lastName="
				+ lastName + ", emailId=" + emailId + ", password="
				+ Arrays.toString(password) + ", companyName=" + companyName
				+ ", mobileNumber=" + mobileNumber + "]";
	}

	public static class SignupBuilder {
		private String firstName;
		private String lastName;
		private String emailId;
		private String password;
		private String companyName;
		private String mobileNumber;
		
		public SignupBuilder(){
			
		}
		
		public SignupBuilder setFirstName(final String firstName) {
			this.firstName = firstName;
			return this;
		}
		
		public SignupBuilder setLastName(final String lastName) {
			this.lastName = lastName;
			return this;
		}
		
		public SignupBuilder setEmailId(final String emailId) {
			this.emailId = emailId;
			return this;
		}
		
		
		public SignupBuilder setPassword(final String password) {
			this.password = password;
			return this;
		}
		
		
		public SignupBuilder setCompanyName(final String companyName) {
			this.companyName = companyName;
			return this;
		}
				
		public SignupBuilder setMobileNumber(final String mobileNumber) {
			this.mobileNumber = mobileNumber;
			return this;
		}

		public SignupRequest build() {
			return new SignupRequest(
						firstName,
						lastName,
						emailId, 
						password.toCharArray(), 
						companyName, 
						mobileNumber
					);
		}
	}
	
}
