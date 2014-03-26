package com.yada.sdk.zp;

import java.nio.ByteBuffer;
import java.util.concurrent.ConcurrentMap;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.yada.sdk.net.DataTransceivers;
import com.yada.sdk.net.DataTransceiversContext;
import com.yada.sdk.packages.PackagingException;
import com.yada.sdk.packages.transaction.IMessage;
import com.yada.sdk.packages.transaction.IPacker;
import com.yada.sdk.packages.transaction.jpos.JposMessage;



public class RecvPackageProcessorTest {
	
	private DataTransceiversContext context;
	private DataTransceivers transceivers;
	private IMessage message;
	
	private RecvPackageProcessor processor;
	
	private ConcurrentMap<String, TranContext> map;
	private IPacker packer;
	private IZpSystemConfigService zpSystemConfigService;
	private IZpkChangeNotify zpkChangedNotify;
	
	@SuppressWarnings("unchecked")
	@Before
	public void before() throws Exception {
		map = Mockito.mock(ConcurrentMap.class);
		packer = Mockito.mock(IPacker.class);
		zpSystemConfigService = Mockito.mock(IZpSystemConfigService.class);
		zpkChangedNotify = Mockito.mock(IZpkChangeNotify.class);
		
		processor = new RecvPackageProcessor(map, packer, zpSystemConfigService, zpkChangedNotify);
		
		context = Mockito.mock(DataTransceiversContext.class);
		transceivers = Mockito.mock(DataTransceivers.class);
		Mockito.when(transceivers.isValid()).thenReturn(true).thenReturn(false);
		Mockito.when(context.getDataTransceivers()).thenReturn(transceivers);
		
		message = new JposMessage();
		message.setFieldString(0, "0800");
		message.setFieldString(70, "001");
		Mockito.when(packer.unpack(Mockito.any(ByteBuffer.class))).
					thenReturn(message).thenReturn(message).thenThrow(PackagingException.class);
		Mockito.when(map.get(Mockito.any())).thenReturn(new TranContext()).thenReturn(null);
		
		Mockito.when(packer.createEmpty()).thenReturn(message).thenReturn(message).
					thenThrow(InterruptedException.class).thenThrow(PackagingException.class);
	}
	
	@Test
	public void testProc() throws Exception {
		// tranContext notNull
		processor.proc(context);
		// tranContext isNull
		processor.proc(context);
		// tranContext isNull
		processor.proc(context);
		
		Mockito.verify(map, Mockito.times(2)).get(Mockito.any());
		Mockito.verify(packer, Mockito.times(3)).unpack(Mockito.any(ByteBuffer.class));
	}
	
	@Test
	public void testProcNotMatchMessage() throws Exception {
		// mti FieldString(0) not0800
		message.setFieldString(0, "0000");
		processor.procNotMatchMessage(message, context);
	}
	
	@Test
	public void testProcNetManagementMessage() throws Exception {
		// FieldString(70) is001
		processor.procNetManagementMessage(message, context);
		
		// FieldString(70) is002
		message.setFieldString(70, "002");
		processor.procNetManagementMessage(message, context);
		
		// FieldString(70) is101 keyType is1
		message.setFieldString(48, "01234567890123456789012345678901234");
		message.setFieldString(70, "101");
		processor.procNetManagementMessage(message, context);
		
		// FieldString(70) is101 keyType not1
		message.setFieldString(48, "0i234567890123456789012345678901234");
		message.setFieldString(70, "101");
		processor.procNetManagementMessage(message, context);
		
		// FieldString(70) is301
		message.setFieldString(70, "301");
		processor.procNetManagementMessage(message, context);
		
		// FieldString(70) isNull
		message.setFieldString(70, "");
		processor.procNetManagementMessage(message, context);
		
		Mockito.verify(zpSystemConfigService, Mockito.times(1)).savePinKey(Mockito.anyString());
	}
	
	@Test
	public void testNetManagementResponse() throws Exception {
		// transceivers.isValid() isTrue
		processor.netManagementResponse(message, context);
		//Mockito.verify(transceivers).isValid();
		// transceivers.isValid() isFalse
		processor.netManagementResponse(message, context);
		// isInterruptedException
		processor.netManagementResponse(message, context);
		// isPackagingException
		processor.netManagementResponse(message, context);
		
		Mockito.verify(transceivers, Mockito.times(4)).isValid();
		Mockito.verify(packer, Mockito.times(4)).createEmpty();
		Mockito.verify(packer, Mockito.times(2)).pack(Mockito.any(IMessage.class));
	}
}
