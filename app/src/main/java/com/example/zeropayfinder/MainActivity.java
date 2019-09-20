package com.example.zeropayfinder;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class MainActivity extends AppCompatActivity {

    Button mbtMap,mbtBook,mbtStamp,mbtTutorial;
    Intent mIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        mbtMap= (Button) findViewById(R.id.moveMap);
        mbtBook=(Button)findViewById(R.id.moveBook);
        mbtStamp=(Button)findViewById(R.id.moveStamp);
        mbtTutorial=(Button)findViewById(R.id.moveTutorial);


        mbtMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),Map.class);
                startActivity(intent);//액티비티 띄우기
            }
        });

        mbtBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),moneyBook.class);
                startActivity(intent);//액티비티 띄우기
            }
        });

        mbtStamp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),stamp.class);
                startActivity(intent);//액티비티 띄우기
            }
        });

        mbtTutorial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),tutorial.class);
                startActivity(intent);//액티비티 띄우기
            }
        });


        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.

    }


    private void moveMap() {


    }

}
