package com.example.mahya.shahane;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class LoginPageActivity extends AppCompatActivity {
    private TextView tv;
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
    }
}
