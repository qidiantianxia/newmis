package org.com.yada.sdk.net;

import java.nio.ByteBuffer;

/**
 * 
 * @author tao
 *
 */
public class DataTransceiversContext {
	
	private DataTransceivers dataTransceivers;
	private ByteBuffer data;
	
	public DataTransceiversContext(DataTransceivers dataTransceivers, ByteBuffer data)
	{
		this.dataTransceivers = dataTransceivers;
		this.data = data;
	}
	
	public DataTransceivers getDataTransceivers()
	{
		return dataTransceivers;
	}
		
	public ByteBuffer getData()
	{
		return data;
	}
}