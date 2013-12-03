package org.com.yada.sdk.net;

import java.nio.ByteBuffer;

/**
 * 
 * @author tao
 *
 */
public interface IPackageSplitter {
	ByteBuffer getPackage(ByteBuffer newData);
}