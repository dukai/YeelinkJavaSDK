package net.yeelink.sdk;


public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		/*
		HttpClient http = new HttpClient();
		HashMap<String, String> headers = new HashMap<String, String>();
		
		headers.put("U-APIKEY", "4d0cd8e2e9cd21714b10696f80645d42");
		
		
		
		File foto = new File("C:\\Users\\Administrator\\Pictures\\45fd27285ef90bde7e84019c81c29938.png");
		http.post("http://api.yeelink.net/v1.0/device/2714/sensor/17534/photos", foto, headers);
		
		System.out.println(http.getStatusCode());
		System.out.println(http.getResponseContent());
		
		
		http.get("http://api.yeelink.net/v1.1/device/2714/sensor/17534/photo/info", headers);
		System.out.println(http.getStatusCode());
		System.out.println(http.getResponseContent());
		*/
		
		HttpClient http = new HttpClient();
		http.addHeader("U-APIKEY", "4d0cd8e2e9cd21714b10696f80645d42");
		http.get("http://api.yeelink.net/v1.0/device/2714/sensor/3751/datapoints");
		
		System.out.println(http.getResponseContent());
	}
}
