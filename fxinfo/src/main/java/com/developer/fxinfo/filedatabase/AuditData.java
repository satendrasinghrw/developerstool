package com.developer.fxinfo.filedatabase;

import java.nio.ByteBuffer;
import java.util.Arrays;

public class AuditData {
	
	private static final int USER_ID_OFFSET = 0;
	private static final int USER_ID_MAX_LENGTH = 50;
	
	private static final int EVENT_OFFSET = 50;
	private static final int EVENT_MAX_LENGTH = 20;
	
	private static final int DATE_TIME_OFFSET = 70;
	private static final int DATE_TIME_MAX_LENGTH = 30;
	
	public static final int USER_DATA_LENGTH = 100;

	private final ByteBuffer buffer;
	
	
	public AuditData(String userId, String eventName, String dateTime) {
		buffer = ByteBuffer.allocate(USER_DATA_LENGTH);
		byte data[];
		
		buffer.position(USER_ID_OFFSET);
		if (userId != null) {
			userId = userId.length() > 50 ? userId.substring(0, 50) : userId;	
			data = new byte[USER_ID_MAX_LENGTH];
			System.arraycopy(userId.getBytes(), 0, data, 0, userId.length());
			buffer.put(data);
		}

		if (eventName != null) {
			buffer.position(EVENT_OFFSET);
			data = new byte[EVENT_MAX_LENGTH];
			System.arraycopy(eventName.getBytes(), 0, data, 0, eventName.length());
			buffer.put(data);
		}

		if (dateTime != null) {
			buffer.position(DATE_TIME_OFFSET);
			data = new byte[DATE_TIME_MAX_LENGTH];
			System.arraycopy(dateTime.getBytes(), 0, data, 0, dateTime.length());
			buffer.put(data);
		}
		buffer.flip();
	}
	
	public AuditData(byte []data) {
		buffer = ByteBuffer.allocate(USER_DATA_LENGTH);
		buffer.put(data);
		buffer.flip();
	}

	public String getUserId() {
		final byte data[] = new byte[USER_ID_MAX_LENGTH];
		buffer.position(USER_ID_OFFSET);
		buffer.get(data);
		return new String(data).trim();
	}

	public String getEventName() {
		final byte data[] = new byte[EVENT_MAX_LENGTH];
		buffer.position(EVENT_OFFSET);
		buffer.get(data);
		return new String(data).trim();
	}

	public String getDateTime() {	
		final byte data[] = new byte[DATE_TIME_MAX_LENGTH];
		buffer.position(DATE_TIME_OFFSET);
		buffer.get(data);
		return new String(data).trim();
	}

	public byte[] byteArray() {
		return Arrays.copyOf(buffer.array(), USER_DATA_LENGTH);
	}

	@Override
	public String toString() {
		buffer.position(0);
		return "AuditData [buffer=" + new String(buffer.array()) + "]";
	}
}