package com.example.zeropayfinder;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class stamp extends AppCompatActivity {

    private int buttonState = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stamp);

        final Button button1 = findViewById(R.id.button6);
        final Button button2 = findViewById(R.id.button7);
        final Button button3 = findViewById(R.id.button8);
        final Button button4 = findViewById(R.id.button9);
        final Button button5 = findViewById(R.id.button10);

        final Button button6 = findViewById(R.id.button11);
        final Button button7 = findViewById(R.id.button12);
        final Button button8 = findViewById(R.id.button13);
        final Button button9 = findViewById(R.id.button14);
        final Button button10 = findViewById(R.id.button15);

        //Add OnClickEvents to the button that responds to the user event
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("test","dfd");
                if(buttonState % 2 == 0){
                    button1.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

                }
                else{
                    button1.setBackgroundColor(getResources().getColor(R.color.colorAccent));

                }
                buttonState++;
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("test","dfd");
                if(buttonState % 2 == 0){
                    button2.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

                }
                else{
                    button2.setBackgroundColor(getResources().getColor(R.color.colorAccent));

                }
                buttonState++;
            }
        });

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("test","dfd");
                if(buttonState % 2 == 0){
                    button3.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

                }
                else{
                    button3.setBackgroundColor(getResources().getColor(R.color.colorAccent));

                }
                buttonState++;
            }
        });

        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("test","dfd");
                if(buttonState % 2 == 0){
                    button4.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

                }
                else{
                    button4.setBackgroundColor(getResources().getColor(R.color.colorAccent));

                }
                buttonState++;
            }
        });

        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("test","dfd");
                if(buttonState % 2 == 0){
                    button5.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

                }
                else{
                    button5.setBackgroundColor(getResources().getColor(R.color.colorAccent));

                }
                buttonState++;
            }
        });

        button6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("test","dfd");
                if(buttonState % 2 == 0){
                    button6.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

                }
                else{
                    button6.setBackgroundColor(getResources().getColor(R.color.colorAccent));

                }
                buttonState++;
            }
        });

        button7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("test","dfd");
                if(buttonState % 2 == 0){
                    button7.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

                }
                else{
                    button7.setBackgroundColor(getResources().getColor(R.color.colorAccent));

                }
                buttonState++;
            }
        });

        button8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("test","dfd");
                if(buttonState % 2 == 0){
                    button8.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

                }
                else{
                    button8.setBackgroundColor(getResources().getColor(R.color.colorAccent));

                }
                buttonState++;
            }
        });

        button9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("test","dfd");
                if(buttonState % 2 == 0){
                    button9.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

                }
                else{
                    button9.setBackgroundColor(getResources().getColor(R.color.colorAccent));

                }
                buttonState++;
            }
        });

        button10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("test","dfd");
                if(buttonState % 2 == 0){
                    button10.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

                }
                else{
                    button10.setBackgroundColor(getResources().getColor(R.color.colorAccent));

                }
                buttonState++;
            }
        });
    }
}
