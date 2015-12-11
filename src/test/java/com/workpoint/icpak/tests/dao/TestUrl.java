package com.workpoint.icpak.tests.dao;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;

import org.junit.Test;

public class TestUrl {

	class Tester implements Runnable{
		private int count;

		public Tester(int count) {
			this.count = count;
		}
		
		@Override
		public void run() {
			try{
				
//				URL url = new URL("https://www.icpak.com/icpakportal/getreport?bookingRefId=U5G0Yi5WPoeUIf1V&action=GETPROFORMA");
				URL url = new URL("http://www.icpak.com:8080/icpakportal/api/users");
				//URL url = new URL("https://www.icpak.com/icpakportal/index.html");
				//URL url = new URL("http://127.0.0.1:8888/index.html");
				System.err.println("Request "+count);
				InputStream stream = url.openStream();
				
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	@Test
	public void loadUrls(){
		System.out.println(Thread.currentThread());
		for(int i=0; i<10000; i++){
			Thread t = new Thread(new Tester(i));
			t.start();
			try{
				//t.join();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		
	}
}
