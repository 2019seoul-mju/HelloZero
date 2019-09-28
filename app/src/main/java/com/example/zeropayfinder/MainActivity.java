package com.example.zeropayfinder;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
    TextView tvLogin;
    Button mbtMap,mbtBook,mbtSearchMap,mbtTutorial,mbtLogin,mbtAccount;
    static String jwttemp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



       // findViewById(R.id.btn_popupmenu).setOnClickListener(this);
        tvLogin = (TextView) findViewById(R.id.tvUserlogin);

        if(jwttemp==null){
            tvLogin.setText("로그인");
        }else{
            tvLogin.setText("로그아웃");
        }
        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(jwttemp == null){
                    Intent intent = new Intent(getApplicationContext(),login.class);
                    startActivity(intent);//액티비티 띄우기
                }else{
                    tvLogin.setText("로그인");
                    Toast.makeText(MainActivity.this, "로그아웃 되었습니다.", Toast.LENGTH_LONG).show();
                    jwttemp=null;
                }
            }
        });

        Intent intent=new Intent(this.getIntent());
        final String jwt=intent.getStringExtra("jwt");
        jwttemp = jwt;

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
        mbtSearchMap=(Button)findViewById(R.id.moveSearchMap);
        mbtTutorial=(Button)findViewById(R.id.moveTutorial);
        mbtLogin= (Button) findViewById(R.id.Login);
        mbtAccount= (Button) findViewById(R.id.Accountbtn);



        mbtMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),Map.class);
                intent.putExtra("jwt",jwttemp);
                startActivity(intent);//액티비티 띄우기
            }
        });

        mbtBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),moneyBook.class);
                intent.putExtra("jwt",jwttemp);
                if(jwttemp == null){
                    Toast.makeText(MainActivity.this, "로그인이 필요한 서비스입니다.", Toast.LENGTH_SHORT).show();
                }
                else startActivity(intent);//액티비티 띄우기
            }
        });

        mbtSearchMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),SearchMap.class);
                intent.putExtra("jwt",jwttemp);
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
        mbtLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),login.class);
                startActivity(intent);//액티비티 띄우기
            }
        });

        mbtAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),stamp.class);
                startActivity(intent);//액티비티 띄우기
            }
        });

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.

    }
    public static void setJWT(String jwt){
        MainActivity.jwttemp = jwt;

    }

    @Override
    public void onClick(View v){
        switch( v.getId() ){
//            case R.id.btn_popupmenu:
//
//                //PopupMenu객체 생성.
//                //생성자함수의 첫번재 파라미터 : Context
//                //생성자함수의 두번째 파라미터 : Popup Menu를 붙일 anchor 뷰
//                PopupMenu popup= new PopupMenu(this, v);//v는 클릭된 뷰를 의미
//
//                //Popup Menu에 들어갈 MenuItem 추가.
//                //이전 포스트의 컨텍스트 메뉴(Context menu)처럼 xml 메뉴 리소스 사용
//                //첫번재 파라미터 : res폴더>>menu폴더>>mainmenu.xml파일 리소스
//                //두번재 파라미터 : Menu 객체->Popup Menu 객체로 부터 Menu 객체 얻어오기
//
//                getMenuInflater().inflate(R.menu.bottom_nav_menu, popup.getMenu());
//
//                //Popup Menu의 MenuItem을 클릭하는 것을 감지하는 listener 설정
//                popup.setOnMenuItemClickListener(listener);
//                if(jwttemp != null){
//                    findViewById(R.id.Login).setVisibility(View.GONE);
//                    findViewById(R.id.Join).setVisibility(View.GONE);
//                    findViewById(R.id.Logout).setVisibility(View.VISIBLE);
//                }
//                /*else {
//                    findViewById(R.id.Login).setVisibility(View.VISIBLE);
//                    findViewById(R.id.Join).setVisibility(View.VISIBLE);
//                    findViewById(R.id.Logout).setVisibility(View.VISIBLE);
//                }*/
//                popup.show();//Popup Menu 보이기
//
//                break;
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
                    Intent intent = new Intent(getApplicationContext(),login.class);
                    startActivity(intent);//액티비티 띄우기
                    break;
                case R.id.Join:
                   Intent intent2 = new Intent(getApplicationContext(),join.class);
                     startActivity(intent2);//액티비티 띄우기
                    break;
                case R.id.Logout:
                    Intent intent3 = new Intent(getApplicationContext(),MainActivity.class);
                  startActivity(intent3);//액티비티 띄우기
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
        //Login
        if (id == R.id.Login) {

            return true;
        }
        //Join
        if (id == R.id.Join) {

            return true;
        }
        //회원가입
        if (id == R.id.navigation_dashboard) {
//            Intent intent = new Intent(getApplicationContext(),Map.class);
//            startActivity(intent);//액티비티 띄우기
//            startActivity(intent);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if(MainActivity.jwttemp != null){
            tvLogin.setText("로그아웃");
        }else{
            tvLogin.setText("로그인");
        }
    }
}
