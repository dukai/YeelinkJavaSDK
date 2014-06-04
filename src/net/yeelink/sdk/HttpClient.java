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
	
	public void httpGet(String urlString, HashMap<String, String> headers, String method){
		try {
			conn = this.getConnection(urlString);
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

	public void get(String urlString, HashMap<String, String> headers) {
		this.httpGet(urlString, headers, REQUEST_METHOD_GET);
	}
	
	public void delete(String urlString, HashMap<String, String> headers) {
		this.httpGet(urlString, headers, REQUEST_METHOD_DELETE);
	}
	
	public void post(String url, HashMap<String, String> headers, byte[] postContent){
		httpPost(url, headers, postContent, REQUEST_METHOD_POST);
	}
	/**
	 * 上传字符串内容
	 * @param url
	 * @param headers
	 * @param postContent
	 */
	public void post(String url, HashMap<String, String> headers, String postContent){
		this.post(url, headers, postContent.getBytes());
	}
	
	public void post(String url, HashMap<String, String> headers, File file){
		try {
			FileInputStream fin = new FileInputStream(file);
			BufferedInputStream bis = new BufferedInputStream(fin);
			int signal = 0;
			ByteArrayOutputStream bais = new ByteArrayOutputStream();
			while((signal = bis.read()) != -1){
				bais.write(signal);
			}
			this.post(url, headers, bais.toByteArray());
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public void put(String url, HashMap<String, String> headers, byte[] postContent){
		httpPost(url, headers, postContent, REQUEST_METHOD_POST);
	}
	
	public void put(String url, HashMap<String, String> headers, String postContent){
		put(url, headers, postContent.getBytes());
	}
	
	/**
	 * 获取连接
	 * @param url
	 * @return
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	private HttpURLConnection getConnection(String url) throws MalformedURLException, IOException{
		return (HttpURLConnection) new URL(url).openConnection();
	}
	/**
	 * 设置headers
	 * @param conn
	 * @param headers
	 */
	private void setHeaders(HttpURLConnection conn, HashMap<String, String> headers){
		Iterator<?> it = headers.entrySet().iterator();

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
	 * 读取数据
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
