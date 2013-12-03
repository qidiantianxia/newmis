package org.com.yada.sdk.net;

import static org.mockito.Mockito.mock;
import java.nio.ByteBuffer;

import junit.framework.TestCase;

public class DataTransceiversContextTest extends TestCase {

	public void test() {
		DataTransceivers dt = mock(DataTransceivers.class);
		ByteBuffer buffer = mock(ByteBuffer.class);

		DataTransceiversContext context = new DataTransceiversContext(dt,
				buffer);

		assertTrue(context.getData() == buffer);
		assertTrue(context.getDataTransceivers() == dt);
	}
}
