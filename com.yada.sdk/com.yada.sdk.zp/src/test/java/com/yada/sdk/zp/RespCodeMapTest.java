package com.yada.sdk.zp;

import org.junit.Test;

public class RespCodeMapTest {

	@Test
	public void test() {
		System.out.println(RespCodeMap.getMessage("00"));
	}
	
	@Test
	public void testRun() {
		String[] codes = {"00", "A2", "01", "03", "05", "08", "12", "13", "14", "21"};
		
		for(int i=0; i<10; i++){
			RunTest run = new RunTest(codes[i], i);
			Thread thread = new Thread(run);
			thread.start();
		}
	}
	
	private class RunTest implements Runnable {
		
		private String code;
		private int num;
		
		public RunTest(String code, int num) {
			this.code = code;
			this.num = num;
		}

		public void run() {
			String mess = RespCodeMap.getMessage(code);
			System.out.println("Thread-"+num+":"+code+"-"+mess);
		}
	}
}
