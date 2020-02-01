package com.ubs.fxinfo.filedatabase;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * facilitate in-memory file based storage 
 * @author satendra
 *
 */
public final class FileDatabase {
	private final Logger logger = LoggerFactory.getLogger(FileDatabase.class);
	
	private FileChannel dataFileChannel, 
						metadataFileChannel;
	
	private MappedByteBuffer dataByteBuffer, 
							metadataByteBuffer;
	
	private final long dataFileMappedSize;
	private final long metaDataFileMappedSize;
	private final int BF_SIZE = 512;

	private final char METADATA_DELIMETER  = '\u0001';
	private final ReentrantLock lock = new ReentrantLock();
	
	private long dataFileStartIdx, 
				 dataByteBufferSizeRemaining, 
				 metadataFileStartIdx, 
				 metadataFileByteRemaining;
	
	private Map<String,String> dataLocator;

	public FileDatabase(long dataFileSize, long metaDataFileSize, String filePath,
			String fileMode) {
		
		if(dataFileSize <= 0L || metaDataFileSize <= 0L || fileMode == null){
			logger.error("invalid configuration. exiting...");
			System.exit(-1);
		}
		
		dataFileMappedSize = dataFileSize;
		metaDataFileMappedSize   = metaDataFileSize;
		final String file = filePath != null ? filePath+"/user" : "user";
		try {
			dataFileChannel = new RandomAccessFile(file+ ".data", fileMode).getChannel();
			metadataFileChannel = new RandomAccessFile(file+ ".metadata", fileMode).getChannel();
			
			dataFileStartIdx = dataFileChannel.size();
			metadataFileStartIdx = metadataFileChannel.size();
			
			if (dataFileStartIdx == 0L) {
				dataByteBuffer  = dataFileChannel.map(MapMode.READ_WRITE, dataFileStartIdx, dataFileMappedSize);
				dataByteBufferSizeRemaining = dataFileMappedSize;
			}
			
			if (metadataFileStartIdx == 0L) {
				metadataByteBuffer   = metadataFileChannel.map(MapMode.READ_WRITE, metadataFileStartIdx, metaDataFileMappedSize);
				metadataFileByteRemaining = metaDataFileMappedSize;
			}

			dataLocator = new HashMap<String, String>();			
			loadMetadataFile();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.exit(-1);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}
		
		logger.info(" dataFileStartIdx: {}, metadataFileStartIdx {} - remaining {} {} ", dataFileStartIdx , metadataFileStartIdx, dataByteBufferSizeRemaining, metadataFileByteRemaining );
	}
	
	private void loadMetadataFile(){
		try {
			final long metadataLen = metadataFileChannel.size();
			final int temp = (int) (metadataLen/metaDataFileMappedSize);
			final int timesRead = metadataLen%metaDataFileMappedSize == 0 ? temp : temp + 1;
			
			final StringBuilder sb = new StringBuilder();
			final ByteBuffer bf = ByteBuffer.allocate((int) metaDataFileMappedSize);
			
			for(int i = 0 ; i < timesRead; i++ ){
				final int readByte = metadataFileChannel.read(bf);
				bf.flip();
				final int actualReadBytes = readByte <= -1 ? 0 : readByte;		
				sb.append(new String(bf.array(),0,actualReadBytes)); //TODO need to parse data at run time
				bf.clear();
			}
			logger.debug("Metadata file size: "+ metadataLen +", StringBuffer size: "+ sb.length());

			final String metaData = sb.toString();
			metadataFileStartIdx = metaData.lastIndexOf('\n')+1L;
			dataFileStartIdx = metadataFileStartIdx >= 0L ? 0L : dataFileStartIdx;
			
			Arrays.stream(metaData.split("\n")).forEach(line -> {
				final int index = line.indexOf(METADATA_DELIMETER);
				if (index > 0 && index < line.length()) {
					final String key = line.substring(0, index);
					final String value = line.substring(index+1);		
					logger.info("reloaded: key [{}] value [{}]", key, value);
					dataLocator.put(key, value);
					updateActualDataLength(value);
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void updateActualDataLength(String value) {
		
		final int idx = value.indexOf(METADATA_DELIMETER);
		if (idx > 0) {
			try {
				final long pos = Long.parseLong(value.substring(0, idx));
				final int len = Integer.parseInt(value.substring(idx+1));
				dataFileStartIdx = dataFileStartIdx < pos+(long)len ? pos+(long)len : dataFileStartIdx;
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
		}	
	}

	public boolean isUserPresent(String userId) {
		return dataLocator.containsKey(userId);
	}
	
	public String writeToFile(String userId, byte[] data) throws IOException {
		if(data == null || userId == null) return "Invalid input";
		
		if(dataLocator.containsKey(userId)){
			logger.debug("{} is already exist.",userId);
			return "Duplicate user creation not allowed";
		}
		
		final int dataLen = data.length;
		try {
			lock.lock();
			if(dataByteBufferSizeRemaining  < dataLen){
				dataByteBuffer = dataFileChannel.map(MapMode.READ_WRITE, dataFileStartIdx, dataFileMappedSize);
				dataByteBufferSizeRemaining = dataFileMappedSize;
			}
			
			dataByteBuffer.put(data, 0, dataLen);
			
			StringBuilder locatorVal = new StringBuilder();
			locatorVal.append(dataFileStartIdx);
			locatorVal.append(METADATA_DELIMETER);
			locatorVal.append(dataLen);
			final String metaDataValue = locatorVal.toString();
			dataLocator.put(userId, metaDataValue);
			writeToMetadataFile(userId, metaDataValue);
			dataFileStartIdx += dataLen;
			dataByteBufferSizeRemaining -= dataLen;
		} finally {
			lock.unlock();
		}
		return null;
	}
	
	private void writeToMetadataFile(String key, String value) 
		throws IOException{
		StringBuilder locatorMsg = new StringBuilder();
		locatorMsg.append(key);
		locatorMsg.append(METADATA_DELIMETER);
		locatorMsg.append(value);
		locatorMsg.append('\n');
		final int locatorSize = locatorMsg.length();
		
		if(metadataFileByteRemaining < locatorSize){
			metadataByteBuffer = metadataFileChannel.map(MapMode.READ_WRITE, metadataFileStartIdx, metaDataFileMappedSize);
			metadataFileByteRemaining = metaDataFileMappedSize;
		}
		
		metadataByteBuffer.put(locatorMsg.toString().getBytes(),0,locatorSize);
		metadataFileStartIdx += locatorSize;
		metadataFileByteRemaining -= locatorSize;
	}
	
	public byte[] readFromFile(String userId) throws IOException{
		if( userId == null ) return null;
		
		final String locator = dataLocator.get(userId);
    	if (locator == null) return null;
    	
    	long startIdx = -1; 
    	int len = -1;
    	
    	try{
    		final int idx = locator.indexOf(METADATA_DELIMETER);
    		startIdx = Long.parseLong(locator.substring(0, idx));
    	    len = Integer.parseInt(locator.substring(idx+1));
    	}catch (NumberFormatException e){
    		e.printStackTrace();
    		return null;
    	}catch (StringIndexOutOfBoundsException e){
    		e.printStackTrace();
    		return null;
    	}
    	
		final byte data[] = new byte[len];
		final ByteBuffer byteBuffer = ByteBuffer.allocate(BF_SIZE);
		final int timesRead = len%BF_SIZE == 0 ? len/BF_SIZE : len/BF_SIZE +1;
		int startPos = 0;
		for(int i = 0; i < timesRead; i++){
			final int readByte = dataFileChannel.read(byteBuffer, startIdx);
			final int actualReadByte = len > readByte ? readByte : len;
			System.arraycopy(byteBuffer.array(), 0, data, startPos, actualReadByte);
			startPos+=actualReadByte;
			startIdx+=readByte;
			len-=readByte;
			byteBuffer.clear();
		}
		return data;
	}
}