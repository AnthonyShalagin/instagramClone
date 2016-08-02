package com.parse.starter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class UserFeed extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_feed);

		//Enable tapping the User and get the username associated with it

		Intent i = getIntent();
		String activeUsername = i.getStringExtra("username");

		Log.i("AppInfo", activeUsername);

	}
}
