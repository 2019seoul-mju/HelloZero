package com.example.zeropayfinder;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button mbtMap,mbtBook,mbtStamp,mbtTutorial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        findViewById(R.id.btn_popupmenu).setOnClickListener(this);
        Intent intent=new Intent(this.getIntent());
        final String jwt=intent.getStringExtra("jwt");

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
                intent.putExtra("jwt",jwt);
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

    @Override
    public void onClick(View v){
        switch( v.getId() ){
            case R.id.btn_popupmenu:

                //PopupMenu객체 생성.
                //생성자함수의 첫번재 파라미터 : Context
                //생성자함수의 두번째 파라미터 : Popup Menu를 붙일 anchor 뷰
                PopupMenu popup= new PopupMenu(this, v);//v는 클릭된 뷰를 의미

                //Popup Menu에 들어갈 MenuItem 추가.
                //이전 포스트의 컨텍스트 메뉴(Context menu)처럼 xml 메뉴 리소스 사용
                //첫번재 파라미터 : res폴더>>menu폴더>>mainmenu.xml파일 리소스
                //두번재 파라미터 : Menu 객체->Popup Menu 객체로 부터 Menu 객체 얻어오기

                getMenuInflater().inflate(R.menu.bottom_nav_menu, popup.getMenu());

                //Popup Menu의 MenuItem을 클릭하는 것을 감지하는 listener 설정
                popup.setOnMenuItemClickListener(listener);
                popup.show();//Popup Menu 보이기
                break;
        }
    }

    //Popup Menu의 MenuItem을 클릭하는 것을 감지하는 listener 객체 생성
    //import android.widget.PopupMenu.OnMenuItemClickListener 가 되어있어야 합니다.
    //OnMenuItemClickListener 클래스는 다른 패키지에도 많기 때문에 PopupMenu에 반응하는 패키지를 임포트하셔야 합니다.

    PopupMenu.OnMenuItemClickListener listener= new PopupMenu.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            // TODO Auto-generated method stub
            switch( item.getItemId() ){//눌러진 MenuItem의 Item Id를 얻어와 식별
                case R.id.Login:
                    Toast.makeText(MainActivity.this, "SAVE", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.Join:
                    Toast.makeText(MainActivity.this, "SEARCH", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.Logout:
                    Toast.makeText(MainActivity.this, "SETTING", Toast.LENGTH_SHORT).show();
                    break;
            }

            return false;
        }
    };

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
        //로그인
        if (id == R.id.navigation_home) {

            Intent intent = new Intent(getApplicationContext(),login.class);
            startActivity(intent);

            return true;
        }
        //회원가입
        if (id == R.id.navigation_dashboard) {
            Intent intent = new Intent(getApplicationContext(),join.class);
            startActivity(intent);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
