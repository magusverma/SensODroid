package com.example.mhealth;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

public class MyBroadcastReceiver extends BroadcastReceiver {

	
	
	
	
	@Override
	 public void onReceive(Context context, Intent intent) {
	  Bundle extras = intent.getExtras();
	     if (extras != null) {
	      if(extras.containsKey("value")){
	       System.out.println("Value is:"+extras.get("value"));
	      //String i1=extras.getString("value");
	     Object o=extras.get("value");
	     int n1=extras.getInt("value");
	     //  Toast.makeText(context,"Value Returned by app is"+ n1,Toast.LENGTH_SHORT).show();
	       if(n1!=0)
	       {
	    	   Intent i = new Intent(context,GetInput.class);
	    	 i.addFlags	(Intent.FLAG_ACTIVITY_NEW_TASK);
	     i.putExtra("Here",n1);
	   context.startActivity(i);
	       
	      }
	     }
	 }
	}
	}
