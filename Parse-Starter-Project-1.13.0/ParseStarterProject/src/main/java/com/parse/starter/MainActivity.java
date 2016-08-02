/*
 * Copyright (c) 2015-present, Parse, LLC.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */
package com.parse.starter;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;


public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnKeyListener{

    EditText usernameField;
    EditText passwordField;
	TextView changeSignUpModeTextView;
	Button signUpButton;
	ImageView logo;
	RelativeLayout relativeLayout;

	Boolean signUpModeActive;


	@Override
	public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {

		if (keyCode == keyEvent.KEYCODE_ENTER && keyEvent.getAction() == KeyEvent.ACTION_DOWN) {	//Factor in only the down action of pressing enter
			signUpOrLogin(view);
		}

		return false;


	}

	@Override
	public void onClick(View view) {	//Called whenever an onClickListener is called

		if(view.getId() == R.id.changeSignUpMode) {	//Check when the changeSignUpMode button was clicked

			if( signUpModeActive == true) {	// Sign up mode to login mode

				signUpModeActive = false;
				changeSignUpModeTextView.setText("Sign Up");
				signUpButton.setText("Login");

			} else {						//Login mode to sign up mode
				signUpModeActive = true;
				changeSignUpModeTextView.setText("Login");
				signUpButton.setText("Sign Up");
			}
		}

		else if (view.getId() == R.id.logo || view.getId()== R.id.relativeLayout) {	//Disable keyboard if user clicks on logo/white screen
			InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
			//imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken());
			imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
		}

	}

    public void signUpOrLogin(View view) {		//Sign Up/Login Button Logic

		if (signUpModeActive == true) {


			ParseUser user = new ParseUser();
			user.setUsername(String.valueOf(usernameField.getText()));
			user.setPassword(String.valueOf(passwordField.getText()));

			user.signUpInBackground(new SignUpCallback() {
				@Override
				public void done(ParseException e) {
					if (e == null) {
						Log.i("AppInfo", "The sign up was successful");
						showUserList();

					} else {
						//Error message handling, get substring after java.util.*
						Toast.makeText(getApplicationContext(), e.getMessage().substring(e.getMessage().indexOf(" ")), Toast.LENGTH_LONG).show();

					}
				}
			});
		}
			else {
					ParseUser.logInInBackground(String.valueOf(usernameField.getText()), String.valueOf(passwordField.getText()), new LogInCallback() {
						@Override
						public void done(ParseUser user, ParseException e) {
							if (user != null) {
								Log.i("AppInfo", "THE LOGIN WAS SUCCESSFUL");

								showUserList();


							} else {
								Toast.makeText(getApplicationContext(), e.getMessage().substring(e.getMessage().indexOf(" ")), Toast.LENGTH_LONG).show();
							}
						}
					});
			}
    }


	public void showUserList () {
		Intent i = new Intent(getApplicationContext(), UserList.class);
		startActivity(i);
	}

  @Override
  protected void onCreate(Bundle savedInstanceState) {
     super.onCreate(savedInstanceState);
     setContentView(R.layout.activity_main);

	  if (ParseUser.getCurrentUser() != null) {
		  if (ParseUser.getCurrentUser() != null) {

			  showUserList();

		  }

	  }

	  signUpModeActive = true;

	  usernameField = (EditText) findViewById(R.id.username);
	  passwordField = (EditText) findViewById(R.id.password);
	  changeSignUpModeTextView = (TextView) findViewById(R.id.changeSignUpMode);
	  signUpButton = (Button) findViewById(R.id.signUpButton);
	  logo = (ImageView) findViewById(R.id.logo);
	  relativeLayout = (RelativeLayout) findViewById(R.id.relativeLayout);


	  changeSignUpModeTextView.setOnClickListener(this);
	  logo.setOnClickListener(this);
	  relativeLayout.setOnClickListener(this);

	  usernameField.setOnKeyListener(this);
	  passwordField.setOnKeyListener(this);

      /*
      ParseUser user = new ParseUser();
      user.setUsername("antonio");
      user.setPassword("password");

      user.signUpInBackground(new SignUpCallback() {
          @Override
          public void done(ParseException e) {
              if (e == null) {
                  Log.i("Sign Up", "SUCCESS!!");
              }
              else {
                  Log.i("Sign Up", "Fail!!");

                  e.printStackTrace();
              }
          }
      });

        */


//      ParseUser.logInInBackground("antonio", "password", new LogInCallback() {
//          @Override
//          public void done(ParseUser user, ParseException e) {
//              if (user != null) {
//                  Log.i("Sign In", "successful");
//              } else {
//                  Log.i("Sign In", "Could not sign in");
//                  e.printStackTrace();
//
//              }
//          }
//      });

      ParseUser.logOut();

      if (ParseUser.getCurrentUser() != null) {
          Log.i("currentUser","User logged in");
      } else {
          Log.i("currentUser", "Not logged in");
      }



      ParseAnalytics.trackAppOpenedInBackground(getIntent());
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu_main, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();

    //noinspection SimplifiableIfStatement
    if (id == R.id.share) {

		Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		startActivityForResult(i, 1);

      	return true;
    }

    return super.onOptionsItemSelected(item);
  }

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		super.onActivityResult(requestCode, resultCode, data); //Called whenever we go to an external activity and then come back to our app

		if (requestCode == 1 && resultCode == RESULT_OK && data != null) {

			Uri selectedImage = data.getData();

			try {

				Bitmap bitmapImage = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);

				Log.i("AppInfo", "Image received");

				//Using ByteArray to store the image
				ByteArrayOutputStream stream = new ByteArrayOutputStream();
				bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, stream);
				byte[] byteArray = stream.toByteArray();

				ParseFile file = new ParseFile("image.png", byteArray);

				ParseObject object = new ParseObject("images");
				object.put("username", ParseUser.getCurrentUser().getUsername());
				object.put("image", file);

				object.saveInBackground(new SaveCallback() {
					@Override
					public void done(ParseException e) {

						if (e == null) {
							Toast.makeText(getApplication().getBaseContext(), "Your image has been posted", Toast.LENGTH_LONG).show();
						}

						else {
							Toast.makeText(getApplication().getBaseContext(), "There was error. Please try again.", Toast.LENGTH_LONG).show();

						}


					}
				});





			} catch (IOException e) {
				e.printStackTrace();
			}


		}

	}


}
