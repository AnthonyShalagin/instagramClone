package com.parse.starter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class UserList extends Activity {

	ArrayList<String> usernames;
	ArrayAdapter arrayAdapter;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_list);

		usernames = new ArrayList<String>();
		arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, usernames);

		final ListView userList = (ListView) findViewById(R.id.userList);

		userList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

				Intent i = new Intent(getApplicationContext(), UserFeed.class);
				i.putExtra("username", usernames.get(position));					//Inputs the username that was tapped
				startActivity(i);

			}
		});


		ParseQuery<ParseUser> query = ParseUser.getQuery();

		query.whereNotEqualTo("username",ParseUser.getCurrentUser().getUsername());
		query.addAscendingOrder("username");

		query.findInBackground(new FindCallback<ParseUser>() {
			@Override
			public void done(List<ParseUser> objects, ParseException e) {

				if (e == null) {

					if (objects.size() > 0) {



						for (ParseUser user : objects) { //objects are now a list of Parse Users, not Parse objects
							usernames.add(user.getUsername());
						}
						userList.setAdapter(arrayAdapter);

					}

				}

			}
		});



	}
}
