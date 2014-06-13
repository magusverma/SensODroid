package com.example.mhealth;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.os.Build;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;




public class PostQuery extends ActionBarActivity implements View.OnClickListener{
	
	EditText first,family,num,gender,age;
	Button bt;
	String username;
	String password;
	String url;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.post_layout);
		first=(EditText)findViewById(R.id.editText3);
		family=(EditText)findViewById(R.id.editText4);
		gender=(EditText)findViewById(R.id.editText5);
		num=(EditText)findViewById(R.id.editText6);
		age=(EditText)findViewById(R.id.editText7);
		bt=(Button)findViewById(R.id.button5);
		Bundle extras = getIntent().getExtras();
		username= extras.getString("uname");
		password = extras.getString("pword");
		url=extras.getString("url");
		bt.setOnClickListener(this);


	}
	
	
	public static StringBuilder inputStreamToString (InputStream is) {
		String line = "";
		StringBuilder total = new StringBuilder();
		// Wrap a BufferedReader around the InputStream
		BufferedReader rd = new BufferedReader(new InputStreamReader(is));
		// Read response until the end
		try {
			while ((line = rd.readLine()) != null) { 
				total.append(line); 
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		// Return full string
		return total;
		}
	
	
	public static String Httppost(String username, String password ,String type, String url, JSONObject jo) throws ClientProtocolException, IOException, HttpResponseException
	{
		HttpClient httpClient = new DefaultHttpClient();
		ResponseHandler<String> resonseHandler = new BasicResponseHandler();
		HttpPost postMethod = new HttpPost(url+"/ws/rest/v1/"+type);    
		postMethod.setEntity(new StringEntity(jo.toString()));
		postMethod.setHeader( "Content-Type", "application/json");
		//String authorizationString = "Basic " + Base64.encodeToString(("admin" + ":" + "Admin123").getBytes(), Base64.DEFAULT); //this line is diffe
		//authorizationString.replace("\n", "");
		//postMethod.setHeader("Authorization", authorizationString);
		postMethod.addHeader(BasicScheme.authenticate(new UsernamePasswordCredentials(username,password),"UTF-8", false));
		String response = httpClient.execute(postMethod,resonseHandler);
		return response;
		
	}
	
	public static String Httpget (String username, String password, String url,String query) throws ClientProtocolException, IOException
	{
		HttpClient httpClient = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(url+"/ws/rest/v1/"+query);
		httpGet.addHeader(BasicScheme.authenticate(new UsernamePasswordCredentials(username, password),"UTF-8", false));
		HttpResponse httpResponse = httpClient.execute(httpGet);
		HttpEntity responseEntity = httpResponse.getEntity();
		String str = inputStreamToString(responseEntity.getContent()).toString();
		httpClient.getConnectionManager().shutdown();
		return str;  
		       
        
    	} 
	public static String JsonParse(String str) throws JSONException
	{
		JSONObject jo = new JSONObject(str);
		JSONArray ja = jo.getJSONArray("results");
			JSONObject j1= (JSONObject)ja.get(0);
			String bla = (String)j1.get("uuid");
			return bla;
		
	}
	
	public static String uuidJsonParse(String str) throws JSONException
	{
		
		JSONObject obj = new JSONObject(str);
		String uuid = obj.getString("uuid");
		return uuid;
		 	 
		
	}
	
	public static String create_patient(String username, String password, String url, String givenName,String familyName, String Id, String gender,int num) throws JSONException, ClientProtocolException, IOException
	{
		JSONObject nm = new JSONObject();
		nm.put("givenName",givenName);
		nm.put("familyName",familyName);
		
		JSONArray ja = new JSONArray();
		ja.put(nm);
		
		JSONObject json = new JSONObject();
		json.put("gender",gender);
		json.put("names",ja);
		json.put("age", num);
		//json.put("preferredAddress", address);
		
		String res;
		res=Httppost(username, password , "person",url,json);
		
		String uuid = uuidJsonParse(res);
		String idType=JsonParse(Httpget(username,password,url,"patientidentifiertype"));
		String loc=JsonParse(Httpget(username,password,url,"location"));
		
		JSONObject fijo = new JSONObject();
		JSONObject iden = new JSONObject();
		iden.put("identifier", Id);
		iden.put("identifierType", idType);
		iden.put("location", loc);
		iden.put("preferred", true);
		
		JSONArray finarr = new JSONArray();
		finarr.put(iden);
		fijo.put("identifiers", finarr);
		fijo.put("person", uuid);
		
		String finstr;
		finstr=Httppost(username,password,"patient",url, fijo);
		return finstr;
		
			
		
		
	
	}
	
		private  class DoAsyncTask extends AsyncTask<String, String, String>{
				
				String res;
				  @Override
				  protected String doInBackground(String... params) {
					  
					   try {
						   Integer a = Integer.parseInt(params[7]);
						   res =create_patient(params[0],params[1], params[2],params[3],params[4],params[5],params[6],a);
						   			
					   }
						
					   catch ( HttpResponseException e){
						   
						   return "boo";
					} catch (ClientProtocolException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				       
						return res;
				  }
		  
		  
		  protected void onPostExecute(String params){
			  
			  if(params.equals("boo"))	
			  {
				 Toast.makeText(getApplicationContext(),"Patient could not be created\nID should be unique!!", Toast.LENGTH_LONG).show();  
			  }
			  else{
			  Toast.makeText(getApplicationContext(),"Patient Created Successfully!!", Toast.LENGTH_LONG).show();
			  }
				
				
		  }
		  
		  protected void onProgressUpdate(Integer... progress){
			    
		  }
		  }

		@Override
		public void onClick(View v) {
			
			String name,last,gen,Id,ag;
			name=first.getText().toString();
			last=family.getText().toString();
			gen=gender.getText().toString();
			Id=num.getText().toString();
			ag=age.getText().toString();
		
			
			
			
			
			switch (v.getId())
					{
				case R.id.button5:
					//Toast.makeText(getApplicationContext(), "create selected", Toast.LENGTH_LONG).show();
			
					if(name.length()<1||last.length()<1||gen.length()<1||Id.length()<1||ag.length()<1)	
					{
						Toast.makeText(getApplicationContext(), "Enter complete details", Toast.LENGTH_LONG).show();	
						
					}
					else{
					
					new DoAsyncTask().execute(username,password,url,name,last,Id,gen,ag);
			  //Toast.makeText(getApplicationContext(), number+" "+full_name+" "+Uuid, Toast.LENGTH_LONG).show();
			  break;
					}
			
		}
		}
		

		
	




}