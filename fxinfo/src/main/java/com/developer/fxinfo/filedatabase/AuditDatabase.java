package com.ubs.fxinfo.filedatabase;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * facilitate in-memory file based storage
 * 
 * @author satendra
 *
 */
public final class AuditDatabase {
	private RandomAccessFile dataFile;

	private final ReentrantLock lock = new ReentrantLock();

	public AuditDatabase(String filePath, String fileMode) {
		final String file = filePath != null ? filePath + "/audit" : "audit";
		try {
			dataFile = new RandomAccessFile(file + ".data", fileMode);
			dataFile.seek(dataFile.length());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.exit(-1);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String writeToFile(AuditData data) throws IOException {
		if (data == null)
			return "Invalid input";
		try {
			lock.lock();
			dataFile.write(data.byteArray());
		} finally {
			lock.unlock();
		}
		return null;
	}

	public static class AuditRecord {
		private final String userId;
		private final String evenName;
		private final String eventTimeStamp;

		public AuditRecord(AuditData data) {
			userId = data.getUserId();
			evenName = data.getEventName();
			eventTimeStamp = data.getDateTime();
		}

		public String getUserId() {
			return userId;
		}

		public String getEvenName() {
			return evenName;
		}

		public String getEventTimeStamp() {
			return eventTimeStamp;
		}

		@Override
		public String toString() {
			return "AuditRecord [userId=" + userId + ", evenName=" + evenName
					+ ", eventTimeStamp=" + eventTimeStamp + "]";
		}
	}

	public ResponseEntity<List<AuditRecord>> getAuditRecords()
			throws IOException {

		final List<AuditRecord> records = new ArrayList<>();
		try {
			dataFile.seek(0);
			final int readLength = AuditData.USER_DATA_LENGTH;
			final long toBeReadLength = dataFile.length();
			final int noOfRecords = toBeReadLength % readLength == 0L ? (int) (toBeReadLength / readLength)
					: (int) (toBeReadLength / readLength) + 1;
			for (int i = 0; i < noOfRecords; i++) {
				final byte d[] = new byte[readLength];
				dataFile.read(d);
				final AuditData audiData = new AuditData(d);
				records.add(new AuditRecord(audiData));
			}
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<List<AuditRecord>>(records,
					HttpStatus.FORBIDDEN);
		}
		return new ResponseEntity<List<AuditRecord>>(records, HttpStatus.OK);
	}

	@Deprecated
	public Map<String, AuditHistory> readAuditData() throws IOException {
		Map<String, AuditHistory> auditDataMap = new HashMap<>();
		dataFile.seek(0);

		final int readLength = AuditData.USER_DATA_LENGTH;
		final long toBeReadLength = dataFile.length();
		final int records = toBeReadLength % readLength == 0L ? (int) (toBeReadLength / readLength)
				: (int) (toBeReadLength / readLength) + 1;
		for (int i = 0; i < records; i++) {
			final byte d[] = new byte[readLength];
			dataFile.read(d);
			final AuditData audiData = new AuditData(d);
			final String userId = audiData.getUserId();
			final AuditHistory history;
			if (!auditDataMap.containsKey(userId)) {
				history = new AuditHistory(userId);
				auditDataMap.put(userId, history);
			} else {
				history = auditDataMap.get(userId);
			}
			history.updateHistory(audiData);
		}
		return auditDataMap;
	}

	@Deprecated
	public static class AuditHistory {
		private final String userId;
		private final List<String> loginDateTimeList;
		private final Map<String, Integer> apiAccess;

		public AuditHistory(String userId) {
			this.userId = userId;
			this.loginDateTimeList = new ArrayList<>();
			this.apiAccess = new HashMap<>();
		}

		public void updateHistory(AuditData data) {
			loginDateTimeList.add(data.getDateTime());
			final String event = data.getEventName();
			if (apiAccess.containsKey(event)) {
				apiAccess.put(event, (apiAccess.get(event).intValue() + 1));
			} else {
				apiAccess.put(event, 1);
			}
		}

		public String getUserId() {
			return userId;
		}

		public List<String> getLoginDateTimeList() {
			return Collections.unmodifiableList(loginDateTimeList);
		}

		public Map<String, Integer> getApiAccess() {
			return Collections.unmodifiableMap(apiAccess);
		}

		@Override
		public String toString() {
			return "AuditHistory [userId=" + userId + ", loginDateTimeList="
					+ loginDateTimeList + ", apiAccess=" + apiAccess + "]";
		}
	}
}