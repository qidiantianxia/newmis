package com.yada.sdk.net;

import java.nio.ByteBuffer;

/**
 * 
 * @author tao
 *
 */
public interface IPackageSplitter {
	ByteBuffer getPackage(ByteBuffer newData);
	ByteBuffer pack(ByteBuffer newData);
}