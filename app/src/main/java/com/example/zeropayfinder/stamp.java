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

        final Button button = findViewById(R.id.button6);

        //Add OnClickEvents to the button that responds to the user event
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("test","dfd");
                if(buttonState % 2 == 0){
                    button.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    Toast.makeText(getBaseContext(), "Button background color green", Toast.LENGTH_SHORT).show();
                }
                else{
                    button.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                    Toast.makeText(getBaseContext(), "Button background color accent", Toast.LENGTH_SHORT).show();
                }
                buttonState++;
            }
        });
    }
}
