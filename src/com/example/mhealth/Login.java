package com.example.mhealth;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.os.Build;
import android.content.Context;

public class Login extends ActionBarActivity implements View.OnClickListener{
	
	EditText user;
	EditText pwd;
	EditText add;
	Button btn;
	public String username;
	public String password;
	public String url;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.first_act);
		user=(EditText)findViewById(R.id.editText1);
         pwd=(EditText)findViewById(R.id.editText2);
         add=(EditText)findViewById(R.id.editText4);
       btn=(Button)findViewById(R.id.button1);
        btn.setOnClickListener(this);
        //btn.setOnClickListener((android.view.View.OnClickListener) this);;  
        
        
        
        
        

		
	}

	public void buttonClick()
	{
		username = (user.getText().toString());
		 password= (pwd.getText().toString());
		 url=(add.getText().toString());
		//Toast.makeText(this, username+"  " + password, Toast.LENGTH_LONG).show();
		Intent intent = new Intent(this, Options.class);
		intent.putExtra("uname",username);
		intent.putExtra("pword",password);
		intent.putExtra("url", url);
		startActivity(intent);
	}
	
	public static int Httpget (String username, String password, String url) throws ClientProtocolException, IOException, HttpHostConnectException
	{
		HttpClient httpClient = new DefaultHttpClient();
		
		HttpGet httpGet = new HttpGet(url+"/ws/rest/v1/patientidentifiertype");
		httpGet.addHeader(BasicScheme.authenticate(
		 new UsernamePasswordCredentials(username, password),
		 "UTF-8", false));

		HttpResponse httpResponse = httpClient.execute(httpGet);
		int status = httpResponse.getStatusLine().getStatusCode();
		return status;
		          	

		
	}
	
private  class MyAsyncTask extends AsyncTask<String, String, String>{
		
		int res;
		  @Override
		  protected String doInBackground(String... params) {
			  
			   try {
				   
				   res = (Httpget(params[0],params[1], params[2]));
				   if(res==401)
					   return "cred";
				   else if (res==404)
					   return "invalid";
				   else if (res == 200)
					   return "successful";
				   
			   } catch (HttpHostConnectException e) {
				   return "refused";
				   
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		       
				return "quit";
		  }
		  
		  
		  protected void onPostExecute(String params){
			  
			 if(params.equals("cred"))
			 {
				 Toast.makeText(getApplicationContext(), "Invalid credentials", Toast.LENGTH_LONG).show();
			 }
			 else if(params.equals("invalid"))
			 {
				 Toast.makeText(getApplicationContext(), "Invalid URL", Toast.LENGTH_LONG).show();
			 }
			 else if(params.equals("refused"))
			 {
				 Toast.makeText(getApplicationContext(), "Connection refused: Wrong URL or server  connection error", Toast.LENGTH_LONG).show();
			 }
			 else if (params.equals("successful"))
			 {
				 buttonClick();
			 }
		  }
		  
		  protected void onProgressUpdate(Integer... progress){
			    
		  }
		  }

	public void onClick(View v) {
		if(user.getText().toString().length()<1 || pwd.getText().toString().length()<1 || add.getText().toString().length()<1 ){
					
					// out of range
					Toast.makeText(this, "please enter complete details", Toast.LENGTH_LONG).show();
				}
		
		else
		{
			switch (v.getId())
			{
			case R.id.button1:
				//buttonClick();

				username = (user.getText().toString());
				 password= (pwd.getText().toString());
				 url=(add.getText().toString());
				 
				new MyAsyncTask().execute(username,password, url);
					break;
					
			}
			
		
		}
				
			}



	

}