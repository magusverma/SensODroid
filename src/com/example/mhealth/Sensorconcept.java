package com.example.mhealth;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


public class Sensorconcept extends ActionBarActivity implements View.OnClickListener {

	EditText sensor,patient;
	Button b1;
	String query;
	LinearLayout main1;
	String username;
	String password;
	String url;
	TextView S, S1;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	
		main1=new LinearLayout(this);
       main1.setOrientation(LinearLayout.VERTICAL);
  	 setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
  	 Bundle extras = getIntent().getExtras(); 
  	 username= extras.getString("uname");
	 password = extras.getString("pword");	
	 url=extras.getString("url");
     S=new TextView(this);
      S.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT));     
      S.setText("Enter sensor Id");
      main1.addView(S);
  	 sensor =new EditText(this);
  	 sensor.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT));     
      main1.addView(sensor);
      S1=new TextView(this);
      S1.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT));     
      S1.setText("Enter Patient Id");
      main1.addView(S1);
  	 patient =new EditText(this);
  	 patient.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT));     
      main1.addView(patient);
      b1=new Button(this);
      b1.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT));   
      b1.setText("Get Concepts");
      b1.setOnClickListener(this);
      main1.addView(b1);
      setContentView(main1);
		
		
		
	
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
	
	public static String Httpget (String username, String password, String url,String query) throws ClientProtocolException, IOException
	{
		HttpClient httpClient = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(url+"/ws/rest/v1/sensor/scm/"+query);
		httpGet.addHeader(BasicScheme.authenticate(new UsernamePasswordCredentials(username, password),"UTF-8", false));
		HttpResponse httpResponse = httpClient.execute(httpGet);
		HttpEntity responseEntity = httpResponse.getEntity();
		String str = inputStreamToString(responseEntity.getContent()).toString();
		httpClient.getConnectionManager().shutdown();
		return str;  
		         
    	} 
	
	public static String JsonParse(String str) throws JSONException
	{ 
		String res="";
		JSONObject jo = new JSONObject(str);
		JSONArray ja = jo.getJSONArray("concepts");
		if (ja.length()==0)
				{
			
			return "zero";
				}
		int len = ja.length();
		System.out.println(len);
		for(int i=0;i<len;i++)
		{
			JSONObject jo1 = (JSONObject) ja.get(i);
			res= res + (String)jo1.get("display") +"*";
			System.out.println(res);

		}
		
		return res;
	}
	
	
	private class MyAsyncTask2 extends AsyncTask<String, String, String>
	{

		@Override
		protected String doInBackground(String... params) {
			// method for posting the readings 
			return null;
		}
		
		 protected void onPostExecute(String params){
		 
		 }
		
		
	}
private  class MyAsyncTask extends AsyncTask<String, String, String>{
		
		String res1,res2;
		  
		  protected String doInBackground(String... params) {
			  
			   try {
				   
				   res1 = JsonParse(Httpget(params[0],params[1], params[2],params[3]));
				  
				  
				   
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
		       
				return res1;
		  }
		  
		  
		  protected void onPostExecute(String params){
			  int size;
			  String arrayString[];
			  //Toast.makeText(getApplicationContext(),params, Toast.LENGTH_LONG).show();
			  Toast.makeText(getApplicationContext(),"PostExecute.", Toast.LENGTH_LONG).show();
			  EditText ed;
			List<EditText> allEds = new ArrayList<EditText>();
			  Toast.makeText(getApplicationContext(),"PostExecute2.", Toast.LENGTH_LONG).show();
			  if(params.equals("zero"))
			  {
				  Toast.makeText(getApplicationContext(),"No Concepts found.", Toast.LENGTH_LONG).show();
			  }
			  
			  else{
				
				 System.out.println("array string length");
	      arrayString= params.split("\\*");
	      System.out.print(arrayString[0]);
	      System.out.print(arrayString[1]); 
	      size=arrayString.length;
          Toast.makeText(getApplicationContext(),size +"  ", Toast.LENGTH_LONG).show();
          System.out.println("array string length"+size);

	 
	 
	 for(int i=0;i<size;i++)
	 {
		 Toast.makeText(getApplicationContext(),i+"  ", Toast.LENGTH_LONG).show();
		 TextView rowTextView = new TextView(Sensorconcept.this);
		 rowTextView.setText(arrayString[i]);
		 main1.addView(rowTextView);
	    ed = new EditText(Sensorconcept.this);
	    allEds.add(ed);
     ed.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT));
	    main1.addView(ed);	 
		
		 
	 }
	 
	 String[] strings = new String[size];
	 String fin = new String() ;
	 for(int i=0; i < allEds.size(); i++){
	     strings[i] = allEds.get(i).getText().toString();
	     fin.concat(strings[i]+"*");
	 }
	 //new MyAsyncTask2().execute(username,password,url,fin);
		  
	
			 
		  }
		  }	  
		  
		  protected void onProgressUpdate(Integer... progress){
			    
		  }
		  }
		 
	

@Override
public void onClick(View v) {
		query=sensor.getText().toString();	
	  new MyAsyncTask().execute(username,password,url,query);
	
	
}
	
}
