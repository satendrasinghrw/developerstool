package com.ubs.fxinfo.filedatabase;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.UUID;

import com.ubs.fxinfo.common.Utils;
import com.ubs.fxinfo.model.SignupRequest;

public final class UserData {
	
	private static final int USER_ID_OFFSET = 0;
	private static final int FIRST_NAME_OFFSET = 100;
	private static final int LAST_NAME_OFFSET = 200;
	private static final int PASSWORD_OFFSET = 300;
	private static final int MOBILE_NUMBER_OFFSET = 350;
	private static final int COMPANY_NAME_OFFSET = 390;
	private static final int CREATION_DATE_TIME_OFFSET = 490;
	private static final int GENERATED_TOKEN_OFFSET = 520;
	
	private static final int USER_ID_MAX_LENGTH = 100;
	private static final int FIRST_NAME_MAX_LENGTH = 100;
	private static final int LAST_NAME_MAX_LENGTH = 100;
	private static final int PASSWORD_MAX_LENGTH = 50;
	private static final int MOBILE_NUMBER_MAX_LENGTH = 40;
	private static final int COMPANY_NAME_MAX_LENGTH = 100;
	private static final int CREATION_DATE_TIME_MAX_LENGTH = 30;
	private static final int GENERATED_TOKEN_MAX_LENGTH = 40;
	
	public static final int USER_DATA_LENGTH = 560;

	private final ByteBuffer buffer;
	
	public UserData(SignupRequest request) {
		buffer = ByteBuffer.allocate(USER_DATA_LENGTH);
		setUserId(request.getEmailId());
		setFirstName(request.getFirstName());
		setLastName(request.getLastName());
		setCompanyName(request.getCompanyName());
		setMobileNumber(request.getMobileNumber());
		setPassword(request.getPassword());
		setAccessToken(UUID.randomUUID().toString());
		setCreateDateTime(Utils.getStrDateTime(Utils.getDateTime().toEpochSecond()));
		buffer.flip();
	}

	public UserData(byte []data) {
		assert data.length == USER_DATA_LENGTH;
		buffer = ByteBuffer.allocate(USER_DATA_LENGTH);
		buffer.put(data);
		buffer.flip();
	}
	
	private void setUserId(String input) {
		byte data[] = new byte[USER_ID_MAX_LENGTH];
		assert USER_ID_MAX_LENGTH >= input.length();
		System.arraycopy(input.getBytes(), 0, data, 0, input.length());
		buffer.position(USER_ID_OFFSET);
		buffer.put(data);
	}
	
	
	private void setCreateDateTime(String input) {
		byte data[] = new byte[CREATION_DATE_TIME_MAX_LENGTH];
		assert CREATION_DATE_TIME_MAX_LENGTH >= input.length();
		System.arraycopy(input.getBytes(), 0, data, 0, input.length());
		buffer.position(CREATION_DATE_TIME_OFFSET);
		buffer.put(data);
	}

	private void setAccessToken(String input) {
		byte data[] = new byte[GENERATED_TOKEN_MAX_LENGTH];
		assert GENERATED_TOKEN_MAX_LENGTH >= input.length();
		System.arraycopy(input.getBytes(), 0, data, 0, input.length());
		buffer.position(GENERATED_TOKEN_OFFSET);
		buffer.put(data);		
	}

	private void setPassword(String input) {
		byte data[] = new byte[PASSWORD_MAX_LENGTH];
		assert PASSWORD_MAX_LENGTH >= input.length();
		System.arraycopy(input.getBytes(), 0, data, 0, input.length());
		buffer.position(PASSWORD_OFFSET);
		buffer.put(data);
	}

	private void setMobileNumber(String input) {
		byte data[] = new byte[MOBILE_NUMBER_MAX_LENGTH];
		assert MOBILE_NUMBER_MAX_LENGTH >= input.length();
		System.arraycopy(input.getBytes(), 0, data, 0, input.length());
		buffer.position(MOBILE_NUMBER_OFFSET);
		buffer.put(data);		
	}

	private void setCompanyName(String input) {
		byte data[] = new byte[COMPANY_NAME_MAX_LENGTH];
		assert COMPANY_NAME_MAX_LENGTH >= input.length();
		System.arraycopy(input.getBytes(), 0, data, 0, input.length());
		buffer.position(COMPANY_NAME_OFFSET);
		buffer.put(data);
	}

	private void setLastName(String input) {
		byte data[] = new byte[LAST_NAME_MAX_LENGTH];
		assert LAST_NAME_MAX_LENGTH >= input.length();
		System.arraycopy(input.getBytes(), 0, data, 0, input.length());
		buffer.position(LAST_NAME_OFFSET);
		buffer.put(data);
	}

	private void setFirstName(String input) {
		byte data[] = new byte[FIRST_NAME_MAX_LENGTH];
		assert FIRST_NAME_MAX_LENGTH >= input.length();
		System.arraycopy(input.getBytes(), 0, data, 0, input.length());
		buffer.position(FIRST_NAME_OFFSET);
		buffer.put(data);
	}

	public String getUserId() {
		final byte data[] = new byte[USER_ID_MAX_LENGTH];
		buffer.position(USER_ID_OFFSET);
		buffer.get(data);
		return new String(data).trim();
	}
	
	
	public String getCreateDateTime() {
		final byte data[] = new byte[CREATION_DATE_TIME_MAX_LENGTH];
		buffer.position(CREATION_DATE_TIME_OFFSET);
		buffer.get(data);
		return new String(data).trim();
	}

	public String getAccessToken() {
		final byte data[] = new byte[GENERATED_TOKEN_MAX_LENGTH];
		buffer.position(GENERATED_TOKEN_OFFSET);
		buffer.get(data);
		return new String(data).trim();
	}

	public String getPassword() {	
		final byte data[] = new byte[PASSWORD_MAX_LENGTH];
		buffer.position(PASSWORD_OFFSET);
		buffer.get(data);
		return new String(data).trim();
	}

	public String getMobileNumber() {	
		final byte data[] = new byte[MOBILE_NUMBER_MAX_LENGTH];
		buffer.position(MOBILE_NUMBER_OFFSET);
		buffer.get(data);
		return new String(data).trim();
	}

	public String getCompanyName() {
		final byte data[] = new byte[COMPANY_NAME_MAX_LENGTH];
		buffer.position(COMPANY_NAME_OFFSET);
		buffer.get(data);
		return new String(data).trim();
	}

	public String setLastName() {
		final byte data[] = new byte[LAST_NAME_MAX_LENGTH];
		buffer.position(LAST_NAME_OFFSET);
		buffer.get(data);
		return new String(data).trim();
	}

	public String getFirstName() {
		final byte data[] = new byte[FIRST_NAME_MAX_LENGTH];
		buffer.position(FIRST_NAME_OFFSET);
		buffer.get(data);
		return new String(data).trim();
	}

	public byte[] byteArray() {
		return Arrays.copyOf(buffer.array(), buffer.array().length);
	}
}
