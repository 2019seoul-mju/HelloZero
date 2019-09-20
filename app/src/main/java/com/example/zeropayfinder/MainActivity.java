package com.example.zeropayfinder;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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

//        //액션바 설정하기//
//        //액션바 타이틀 변경하기
//        getSupportActionBar().setTitle("ACTIONBAR");
//        //액션바 배경색 변경
//        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xFF339999));
//        //홈버튼 표시
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//
//        //액션바 숨기기
//        //hideActionBar();

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.bottom_nav_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        //or switch문을 이용하면 될듯 하다.
        if (id == android.R.id.home) {
            Toast.makeText(this, "홈아이콘 클릭", Toast.LENGTH_SHORT).show();
            return true;
        }
        if (id == R.id.navigation_dashboard) {
            Toast.makeText(this, "검색 클릭", Toast.LENGTH_SHORT).show();
            return true;
        }
        if (id == R.id.navigation_notifications) {
            Toast.makeText(this, "액션버튼 클릭", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
