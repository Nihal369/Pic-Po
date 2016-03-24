package com.parse.starter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.KeyListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import java.io.IOException;
import java.util.List;

public class LoginActivity extends Activity implements View.OnClickListener{

    boolean loginTag=true;

    public void onTextViewClick(View view)
    {
        TextView textView=(TextView)findViewById(R.id.textView);
        Button button=(Button)findViewById(R.id.button);
        if(loginTag)
        {
            textView.setText("Login");
            button.setText("Sign Up");
            loginTag=false;
        }
        else
        {
            textView.setText("Sign Up");
            button.setText("Login");
            loginTag=true;
        }
    }

    String userName=null;
    String password=null;
    public void onButtonClick(View view)
    {
        //Log.i("Status",String.valueOf(loginTag));
        final Intent toUser=new Intent(getApplicationContext(),UsersList.class);
        EditText userNameField = (EditText) findViewById(R.id.userNameField);
        EditText passwordField = (EditText) findViewById(R.id.passwordField);
/*      userNameField.setKeyListener((KeyListener) this);
        passwordField.setKeyListener((KeyListener) this);*/
        userName = userNameField.getText().toString();
        password = passwordField.getText().toString();
        if(userName.equals("") || password.equals(""))
        {
            Toast.makeText(LoginActivity.this, "Username and password cannot be blank", Toast.LENGTH_SHORT).show();
        }
        else{
                if (loginTag) {
                    ParseUser.logInInBackground(userName, password, new LogInCallback() {
                        @Override
                        public void done(ParseUser user, ParseException e) {
                            if (e == null) {
                                Toast.makeText(LoginActivity.this, "Login Successfull", Toast.LENGTH_SHORT).show();
                                startActivity(toUser);
                                finish();
                            } else {
                                Toast.makeText(LoginActivity.this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        else {
            ParseQuery<ParseObject> parseQuery = ParseQuery.getQuery("User");
            parseQuery.whereEqualTo("username", userName);
            parseQuery.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> objects, ParseException e) {
                    for (ParseObject object : objects) {
                        Log.i("MAVA", String.valueOf(object.get("username")));
                    }
                    if (objects.size() > 0) {
                        Toast.makeText(LoginActivity.this, "Username already exists", Toast.LENGTH_SHORT).show();
                    } else {
                        ParseUser user = new ParseUser();
                        user.setUsername(userName);
                        user.setPassword(password);
                        user.signUpInBackground(new SignUpCallback() {
                            @Override
                            public void done(ParseException e) {
                                if (e == null) {
                                    Toast.makeText(LoginActivity.this, "You have signed up successfully", Toast.LENGTH_SHORT).show();
                                    startActivity(toUser);
                                    finish();
                                } else {
                                    Toast.makeText(LoginActivity.this, "Username already exists", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                }
            });
        }
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        TextView textView=(TextView)findViewById(R.id.textView);
        Button button=(Button)findViewById(R.id.button);
        textView.setText("Sign Up");
        button.setText("Login");
        Intent toUser=new Intent(getApplicationContext(),UsersList.class);
       if(ParseUser.getCurrentUser()!=null)
        {
            startActivity(toUser);
            finish();
        }
    }

    @Override
    public void onClick(View v) {
        RelativeLayout relativeLayout=(RelativeLayout)findViewById(R.id.layout);
        relativeLayout.setOnClickListener(this);
        InputMethodManager inputMethodManager=(InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
    }

}
