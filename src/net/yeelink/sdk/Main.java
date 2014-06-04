package net.yeelink.sdk;

import java.io.File;
import java.util.HashMap;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		HttpClient http = new HttpClient();
		HashMap<String, String> headers = new HashMap<String, String>();
		
		headers.put("U-APIKEY", "4d0cd8e2e9cd21714b10696f80645d42");
		
		
		
		File foto = new File("C:\\Users\\Administrator\\Pictures\\45fd27285ef90bde7e84019c81c29938.png");
		http.post("http://api.yeelink.net/v1.0/device/2714/sensor/17534/photos", headers, foto);
		
		System.out.println(http.getStatusCode());
		System.out.println(http.getResponseContent());
		
		
		http.get("http://api.yeelink.net/v1.1/device/2714/sensor/17534/photo/info", headers);
		System.out.println(http.getStatusCode());
		System.out.println(http.getResponseContent());
	}
}
