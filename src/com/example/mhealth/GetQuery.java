package com.example.mhealth;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;

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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Build;

public class GetQuery extends ActionBarActivity implements View.OnClickListener {
	
	Button get ;
	EditText q;
	String query;
	ProgressBar pb;
	String username;
	String password;
	String url;
	TextView num;
	TextView name;
	TextView uuid;
	TextView age,gender,birthdate;
	public Patient patient = new Patient();
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.get_layout);
		get=(Button)findViewById(R.id.button4);
		q=(EditText)findViewById(R.id.editText2);
		num=(TextView)findViewById(R.id.textView7);
		name=(TextView)findViewById(R.id.textView9);
		uuid=(TextView)findViewById(R.id.textView11);
		age=(TextView)findViewById(R.id.textView13);
		gender=(TextView)findViewById(R.id.textView15);
		birthdate=(TextView)findViewById(R.id.textView17);
		Bundle extras = getIntent().getExtras();
		username= extras.getString("uname");
		password = extras.getString("pword");
		url=extras.getString("url");
		get.setOnClickListener(this);

	}
	
	public String jsonParse(String str) throws JSONException
	{
		String info;
		Integer Id;
		String name;
		String uuid;
		JSONObject obj = new JSONObject(str);
	JSONArray ja = obj.getJSONArray("results");
	if(ja.length()==0)
	{
		return "zero";
	}
	
	else
	{
		JSONObject j1= (JSONObject)ja.get(0);
		info = (String) j1.get("display");
		uuid=(String)j1.get("uuid");
		patient.setIdentifier(uuid);
		String stri[] = info.split("-");
		String temp = stri[0].trim();
		Id=Integer.parseInt(temp);
		patient.setPatientId(Id);
		name=stri[1].substring(1);	
		String[] st= name.split("\\s+");
		if (st.length==1)
		{
			patient.setGivenName(st[0]);
		}
		else if (st.length==2)
		{
			patient.setGivenName(st[0]);
			patient.setFamilyName(st[1]);
		}
		else
		{
			patient.setGivenName(st[0]);
			patient.setMiddleName(st[1]);
			patient.setFamilyName(st[2]);
		}
		
		
		
		return Id+"*"+name+"*"+uuid; 
	}
		
		 
				 	
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
	
	
	
	public static String Httpget (String username, String password, String url, String query) throws ClientProtocolException, IOException
	{
		HttpClient httpClient = new DefaultHttpClient();
		
		HttpGet httpGet = new HttpGet(url+"/ws/rest/v1/person?q="+query);
		httpGet.addHeader(BasicScheme.authenticate(
		 new UsernamePasswordCredentials(username, password),
		 "UTF-8", false));

		HttpResponse httpResponse = httpClient.execute(httpGet);
		HttpEntity responseEntity = httpResponse.getEntity();
		String str = inputStreamToString(responseEntity.getContent()).toString();
		httpClient.getConnectionManager().shutdown();  
		return (str);            	

		
	}
	
	public static String DetailsHttpget (String username, String password, String url, String uuid) throws ClientProtocolException, IOException
	{
		HttpClient httpClient = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(url+"/ws/rest/v1/patient/"+uuid+"?v=custom:(gender,age,birthdate)");
		httpGet.addHeader(BasicScheme.authenticate(new UsernamePasswordCredentials(username, password),"UTF-8", false));
		HttpResponse httpResponse = httpClient.execute(httpGet);
		HttpEntity responseEntity = httpResponse.getEntity();
		String str = inputStreamToString(responseEntity.getContent()).toString();
		httpClient.getConnectionManager().shutdown();
		return str;  
		       
        
    	} 
	
	public String DetailsjsonParse(String str) throws JSONException
	{
		JSONObject obj = new JSONObject(str);
		String gender,birthdate;
		Integer age;
		gender=obj.getString("gender");
		patient.setGender(gender);
		age=obj.getInt("age");
		patient.setAge(age);
		birthdate=obj.getString("birthdate");
		patient.setBirthDate(birthdate);
		return patient.getAge()+"*"+patient.getGender()+"*"+patient.getBirthdate();
		
		
	}
	
	private  class MyAsyncTask extends AsyncTask<String, String, String>{
		
		String res1,res2;
		  @Override
		  protected String doInBackground(String... params) {
			  
			   try {
				   
				   res1 = jsonParse(Httpget(params[0],params[1], params[2],params[3]));
				   if(res1.equals("zero"))
				   {
					   return "zero";
				   }
				   else
				   {
				   String arrayString[] = res1.split("\\*");
				   res2=DetailsjsonParse(DetailsHttpget(params[0],params[1], params[2],arrayString[2]));}
				   
				   
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
		       
				return res1 +"*"+ res2;
		  }
		  
		  
		  protected void onPostExecute(String params){
			  
			  if(params.equals("zero"))
			  {
				  Toast.makeText(getApplicationContext(),"No patients found.", Toast.LENGTH_LONG).show();
			  }
			  
			  else{
			  String arrayString[] = params.split("\\*");
			  //Toast.makeText(getApplicationContext(),params, Toast.LENGTH_LONG).show();
				// System.out.println(((String) j1.get("display")).substring(0, 3)); 
				num.setText(arrayString[0]);
				uuid.setText(arrayString[2]);
				name.setText(arrayString[1]);
				age.setText(arrayString[3]);
				gender.setText(arrayString[4]);
				birthdate.setText(arrayString[5]);
			  }
		  }
		  
		  protected void onProgressUpdate(Integer... progress){
			    			
		  }
		  }
		  
		 
	
	
	@Override
	public void onClick(View v)
	{
		query=q.getText().toString();
		switch (v.getId())
				{
			case R.id.button4:
				
		  new MyAsyncTask().execute(username,password,url,query);
		  //Toast.makeText(getApplicationContext(), number+" "+full_name+" "+Uuid, Toast.LENGTH_LONG).show();
		  break;
					
			
				
				}	
			
			
	}
	
}
		