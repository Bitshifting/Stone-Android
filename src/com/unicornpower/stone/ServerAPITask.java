package com.unicornpower.stone;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class ServerAPITask extends AsyncTask<String, Void, String>{
	private Exception exception;
	private String request;
	BufferedReader in = null;
	public void setAPIRequest(String s){
		this.request = s;
	}
	
	protected String doInBackground(String... requests){
		try{
			HttpClient client = new DefaultHttpClient();
			HttpGet get = new HttpGet();
			URI website = new URI(request);
			get.setURI(website);
			HttpResponse response = client.execute(get);
			in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String ss = in.readLine();
			Log.e("RESPONSE", ss);
			return ss;
		}catch (Exception e){
			exception = e;
			Log.e("Exception", e.toString());
			return null;
		}
	}
	
	@Override
	protected void onPostExecute(String s){
		super.onPostExecute(s);
		Message msg = new Message();
		msg.obj = s;
//		Handler mHandler = new Handler();
//		mHandler.sendMessage(msg);
	}
}
