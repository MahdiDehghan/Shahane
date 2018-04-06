package com.example.mahya.shahane;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class LoginPageActivity extends AppCompatActivity {
    private TextView tv;
    private EditText user,email,pass;
    private Button signbtn;
    private String userName,emailAddress,passWord;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);
        //----------------Signup link -------------------
        tv=(TextView)findViewById(R.id.signuplink);
        tv.setTag(0);
        tv.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                final int status =(Integer) v.getTag();
                EditText et=(EditText)findViewById(R.id.user);
                if(status == 1) {
                    et.setVisibility(View.INVISIBLE);
                    tv.setText(getString(R.string.signup_link));
                    v.setTag(0); //login
                } else {
                    et.setVisibility(View.VISIBLE);
                    tv.setText(getString(R.string.login_link));
                    v.setTag(1); //signup
                }
            }
        });
        signbtn=(Button) findViewById(R.id.signbutn);
        signbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user=(EditText) findViewById(R.id.user);
                email=(EditText) findViewById(R.id.email);
                pass=(EditText) findViewById(R.id.pass);

                final int status =(Integer) tv.getTag();//tv is the bottom link in login page
                userName     = user.getText().toString();
                emailAddress = email.getText().toString();
                passWord     = pass.getText().toString();
                //Email validation
                if(!isValidEmail(emailAddress)) {
                    Toast.makeText(LoginPageActivity.this, "email is not correct", Toast.LENGTH_SHORT).show();
                    return;
                }
                //Password validation
                if(TextUtils.isEmpty(passWord) || passWord.length() < getResources().getInteger(R.integer.password_length))
                {
                    Toast.makeText(LoginPageActivity.this, "You must have "+getResources().getInteger(R.integer.password_length)+" characters in your password", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(status == 1) {
                    //http://127.0.0.1:3000/register
                    netClientPost();
                    Toast.makeText(LoginPageActivity.this, "new user added", Toast.LENGTH_SHORT).show(); //login
                } else {
                    Toast.makeText(LoginPageActivity.this, "you are login", Toast.LENGTH_SHORT).show(); //signup
                }
            }
        });
    }
    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public void netClientPost() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public  void run() {

                try {

                    URL url = new URL("http://192.168.1.114:8080/register");
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setDoOutput(true);
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Content-Type", "application/json");

                    JSONObject data = new JSONObject();
                    try {
                        data.put("name", userName);
                        data.put("email", emailAddress);
                        data.put("password", passWord);
                    } catch (JSONException e) {
                        Toast.makeText(LoginPageActivity.this, "your data format is incorrect!", Toast.LENGTH_SHORT).show();
                    }
                    String input = data.toString();
                   // String input= "{\"name\":100,\"email\":\"blsh@da.com\",\"password\":\"123456\"}";

                    OutputStream os = conn.getOutputStream();
                    os.write(input.getBytes());
                    os.flush();
                    //HTTP_CREATED
                    if (conn.getResponseCode() != HttpURLConnection.HTTP_CREATED) {
                        throw new RuntimeException("Failed : HTTP error code : "
                                + conn.getResponseCode());
                    }

                    BufferedReader br = new BufferedReader(new InputStreamReader(
                            (conn.getInputStream())));

                    String output;
                    System.out.println("Output from Server .... \n");
                    while ((output = br.readLine()) != null) {
                        System.out.println(output);
                    }

                    conn.disconnect();

                } catch ( IOException ex) {
                    ex.printStackTrace();
                }

            }

        });
        thread.start();
    }
}
