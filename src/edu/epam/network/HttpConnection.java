package edu.epam.network;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;


public class HttpConnection {

	private  HttpContext localContext;	
	private  HttpClient client;
    private static HttpConnection instance;
	public static HttpConnection getInstance(){
		if(instance==null){
			CookieStore cookieStore = new BasicCookieStore();
			HttpContext localContext = new BasicHttpContext();
			localContext.setAttribute(ClientContext.COOKIE_STORE, cookieStore);
			HttpClient client = new DefaultHttpClient();
			
			HttpConnection connection = new HttpConnection();
			connection.setLocalContext(localContext);
			connection.setClient(client);
			instance = connection;
		}
		return instance;
		
	}

	private void setLocalContext(HttpContext localContext) {
		this.localContext = localContext;
	}
	
	private void setClient(HttpClient client) {
		this.client = client;
	}
	
	
	@SuppressWarnings("deprecation")
	public String post(String url,List<NameValuePair> parameters){
		HttpPost post = new HttpPost(url);
    	try {
			post.setEntity(new UrlEncodedFormEntity(parameters));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		HttpResponse response = null;
		try {
			response = client.execute(post,this.localContext);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		HttpEntity resEntity = response.getEntity();
		try {
			String responseString =  EntityUtils.toString(resEntity);
			return responseString;
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}



}
