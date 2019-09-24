package com.example.zeropayfinder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

public class moneyBook extends AppCompatActivity {

    private TextView JWT;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_money_book);

        JWT = (TextView) findViewById(R.id.jwt);

        Intent intent=new Intent(this.getIntent());
        final String jwt=intent.getStringExtra("jwt");
        JWT.setText(jwt);
    }
}
