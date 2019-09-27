package com.example.zeropayfinder;

import androidx.appcompat.app.AppCompatActivity;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class login extends AppCompatActivity {

    private EditText Login_User_Email;
    private EditText Login_User_Password;
    private Button btnLogin,btnJoin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Login_User_Email = (EditText) findViewById(R.id.Login_User_Email);
        Login_User_Password = (EditText) findViewById(R.id.Login_User_Password);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnJoin = (Button) findViewById(R.id.btnjoin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = Login_User_Email.getText().toString().trim();
                String password = Login_User_Password.getText().toString().trim();

                // 이메일 입력 확인
                if( Login_User_Email.getText().toString().length() == 0 ) {
                    Toast.makeText(login.this, "Email을 입력하세요!", Toast.LENGTH_SHORT).show();
                    Login_User_Email.requestFocus();
                    return;
                }

                // 비밀번호 입력 확인
                if( Login_User_Password.getText().toString().length() == 0 ) {
                    Toast.makeText(login.this, "비밀번호를 입력하세요!", Toast.LENGTH_SHORT).show();
                    Login_User_Password.requestFocus();
                    return;
                }

                if (!email.isEmpty() && !password.isEmpty()) {
                    login.ConnectServer connectServerPost = new login.ConnectServer();
                    connectServerPost.requestPost("http://15.164.118.95/hello/login", email, password);
                } else {
                    Toast.makeText(getApplicationContext(), "필수사항은 모두 입력하세요!", Toast.LENGTH_LONG).show();
                }

            }
        });
        btnJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(getApplicationContext(),join.class);
                startActivity(intent2);//액티비티 띄우기
            }
        });
        // Login Screen 으로 Activity 이동
//        btnLogin.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View view) {
//                startActivity(new Intent(getApplicationContext(), Login.class));
//                finish();
//            }
//        });

    }

    class ConnectServer{
        //Client 생성
        OkHttpClient client = new OkHttpClient();

        public void requestGet(String url, String searchKey){

            //URL에 포함할 Query문 작성 Name&Value
            HttpUrl.Builder urlBuilder = HttpUrl.parse(url).newBuilder();
            urlBuilder.addEncodedQueryParameter("searchKey", searchKey);
            String requestUrl = urlBuilder.build().toString();

            //Query문이 들어간 URL을 토대로 Request 생성
            Request request = new Request.Builder().url(requestUrl).build();

            //만들어진 Request를 서버로 요청할 Client 생성
            //Callback을 통해 비동기 방식으로 통신을 하여 서버로부터 받은 응답을 어떻게 처리 할 지 정의함
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.d("error", "Connect Server Error is " + e.toString());
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    Log.d("aaaa", "Response Body is " + response.body().string());
                }
            });
        }

        class Login_info {

            private final Boolean isSuccess;
            private final String message;
            private final String jwt;

            public Login_info(Boolean isSuccess, int code, String message, String jwt) {
                this.isSuccess = isSuccess;
                this.message = message;
                this.jwt = jwt;
            }
        }

        public void requestPost(String url, String id, String password){

            //Request Body에 서버에 보낼 데이터 작성
            JSONObject postdata = new JSONObject();
            try {
                postdata.put("email", id);
                postdata.put("password", password);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            RequestBody body = RequestBody.create(postdata.toString(), MediaType.parse("application/json; charset=utf-8"));
            //작성한 Body와 데이터를 보낼 url을 Request에 붙임
            Request request = new Request.Builder().url(url).post(body).build();
            //request를 Client에 세팅하고 Server로 부터 온 Response를 처리할 Callback 작성
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.d("error", "Connect Server Error is " + e.toString());
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String result = response.body().string();
                    Gson gson = new Gson();
                    Login_info info = gson.fromJson(result, Login_info.class);

                        if(info.isSuccess == true){
                            Looper.prepare();
                            Toast.makeText(getApplicationContext(), "로그인 성공!", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            intent.putExtra("jwt",info.jwt);
                            MainActivity.setJWT(info.jwt);
                            //startActivity(intent);
                            finish();
                            Looper.loop();
                        } else {
                            Looper.prepare();
                            Toast.makeText(getApplicationContext(), info.message, Toast.LENGTH_LONG).show();
                            Looper.loop();
                        }

                }
            });
        }
    }
}
