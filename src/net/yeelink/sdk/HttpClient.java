package net.yeelink.sdk;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class HttpClient {
	final String REQUEST_METHOD_GET = "GET";
	final String REQUEST_METHOD_POST = "POST";
	final String REQUEST_METHOD_PUT = "PUT";
	final String REQUEST_METHOD_DELETE = "DELETE";
	private HttpURLConnection conn = null;
	private int statusCode = 0;
	private String responseContent = "";
	private HashMap<String, String> globalHeaders = new HashMap<String, String>();
	/**
	 * get status code
	 * @return
	 */
	public int getStatusCode(){
		return this.statusCode;
	}
	/**
	 * get response content body
	 * @return
	 */
	public String getResponseContent(){
		return this.responseContent;
	}
	
	public void addHeader(String key, String val){
		this.globalHeaders.put(key, val);
	}
	
	public void httpGet(String url, HashMap<String, String> headers, String method){
		try {
			conn = this.getConnection(url);
			conn.setRequestMethod(method);
			this.setHeaders(conn, headers);
			statusCode = conn.getResponseCode();
			InputStream in;
			if (statusCode == HttpURLConnection.HTTP_OK) {
				in = conn.getInputStream();
			} else {
				in = conn.getErrorStream();
			}

			this.responseContent = readStream(in);
			conn.disconnect();
		} catch (MalformedURLException e) {
		} catch (IOException e) {
		}
	}
	
	public void httpPost(String url, HashMap<String, String> headers, byte[] postContent, String method){
		try {
			conn = this.getConnection(url);
			conn.setRequestMethod(method);
			this.setHeaders(conn, headers);
			conn.setDoOutput(true);
			conn.setRequestProperty("charset", "utf-8");
			conn.setRequestProperty("Content-Length", "" + Integer.toString(postContent.length));
			conn.setUseCaches (false);
			
			DataOutputStream writer = new DataOutputStream(conn.getOutputStream());
			writer.write(postContent);
			writer.flush();
			writer.close();
			statusCode = conn.getResponseCode();
			
			
			InputStream in;
			if (statusCode == HttpURLConnection.HTTP_OK) {
				in = conn.getInputStream();
			} else {
				in = conn.getErrorStream();
			}
			this.responseContent = readStream(in);
			conn.disconnect();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void get(String url, HashMap<String, String> headers) {
		this.httpGet(url, headers, REQUEST_METHOD_GET);
	}
	
	public void get(String url){
		this.httpGet(url, null, REQUEST_METHOD_GET);
	}
	
	public void delete(String url, HashMap<String, String> headers) {
		this.httpGet(url, headers, REQUEST_METHOD_DELETE);
	}
	public void delete(String url){
		this.httpGet(url, null, REQUEST_METHOD_DELETE);
	}
	
	public void post(String url, byte[] postContent, HashMap<String, String> headers){
		httpPost(url, headers, postContent, REQUEST_METHOD_POST);
	}
	/**
	 * post content string
	 * @param url
	 * @param postContent
	 * @param headers
	 */
	public void post(String url, String postContent, HashMap<String, String> headers){
		this.post(url, postContent.getBytes(), headers);
	}
	
	public void post(String url, String postContent){
		this.post(url, postContent, null);
	}
	
	public void post(String url, File file, HashMap<String, String> headers){
		try {
			FileInputStream fin = new FileInputStream(file);
			BufferedInputStream bis = new BufferedInputStream(fin);
			int signal = 0;
			ByteArrayOutputStream bais = new ByteArrayOutputStream();
			while((signal = bis.read()) != -1){
				bais.write(signal);
			}
			this.post(url, bais.toByteArray(), headers);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void post(String url, File file){
		this.post(url, file, null);
	}
	
	
	public void put(String url, byte[] postContent, HashMap<String, String> headers){
		httpPost(url, headers, postContent, REQUEST_METHOD_POST);
	}
	
	public void put(String url, String postContent, HashMap<String, String> headers){
		put(url, postContent.getBytes(), headers);
	}
	
	public void put(String url, String postContent){
		put(url, postContent.getBytes(), null);
	}
	
	/**
	 * get url connection
	 * @param url
	 * @return
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	private HttpURLConnection getConnection(String url) throws MalformedURLException, IOException{
		return (HttpURLConnection) new URL(url).openConnection();
	}
	/**
	 * set headers
	 * @param conn
	 * @param headers
	 */
	private void setHeaders(HttpURLConnection conn, HashMap<String, String> headers){
		HashMap<String, String> map = new HashMap<String, String>();
		
		map.putAll(this.globalHeaders);
		if(!headers.equals(null)){
			map.putAll(headers);
		}
		
		Iterator<?> it = map.entrySet().iterator();

		while (it.hasNext()) {
			@SuppressWarnings("unchecked")
			Map.Entry<String, String> entry = (Entry<String, String>) it
					.next();
			String key = (String) entry.getKey();
			String val = (String) entry.getValue();

			conn.setRequestProperty(key, val);
		}
	}
	/**
	 * read stream to string
	 * @param in
	 * @return
	 */
	private String readStream(InputStream in) {
		String result = "";
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		String inputLine;
		try {
			while ((inputLine = br.readLine()) != null)
				result += inputLine;
			in.close();

			return result;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

			return "";
		}
	}

}
