package com.example.zeropayfinder;

import androidx.appcompat.app.AppCompatActivity;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;




public class join extends AppCompatActivity {

    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");
    private EditText User_Email;
    private EditText User_Name;
    private EditText User_Password;
    private EditText User_PasswordConfirm;
    private Button btnJoin;
    private Button btnCancel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        User_Email = (EditText) findViewById(R.id.User_Email);
        User_Name = (EditText) findViewById(R.id.User_Email);
        User_Password = (EditText) findViewById(R.id.User_Password);
        User_PasswordConfirm = (EditText) findViewById(R.id.User_PasswordConfirm);
        btnJoin = (Button) findViewById(R.id.btnJoin);
        // 비밀번호 일치 검사
        User_PasswordConfirm.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String password = User_Password.getText().toString();
                String confirm = User_PasswordConfirm.getText().toString();

                if( password.equals(confirm) ) {
                    User_Password.setBackgroundColor(Color.GREEN);
                    User_PasswordConfirm.setBackgroundColor(Color.GREEN);
                } else {
                    User_Password.setBackgroundColor(Color.RED);
                    User_PasswordConfirm.setBackgroundColor(Color.RED);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        btnJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = User_Email.getText().toString().trim();
                String password = User_Password.getText().toString().trim();

                // 이메일 입력 확인
                if( User_Email.getText().toString().length() == 0 ) {
                    Toast.makeText(join.this, "Email을 입력하세요!", Toast.LENGTH_SHORT).show();
                    User_Email.requestFocus();
                    return;
                }

                // 비밀번호 입력 확인
                if( User_Password.getText().toString().length() == 0 ) {
                    Toast.makeText(join.this, "비밀번호를 입력하세요!", Toast.LENGTH_SHORT).show();
                    User_Password.requestFocus();
                    return;
                }

                // 비밀번호 확인 입력 확인
                if( User_PasswordConfirm.getText().toString().length() == 0 ) {
                    Toast.makeText(join.this, "비밀번호 확인을 입력하세요!", Toast.LENGTH_SHORT).show();
                    User_PasswordConfirm.requestFocus();
                    return;
                }

                // 비밀번호 일치 확인
                if( !User_Password.getText().toString().equals(User_PasswordConfirm.getText().toString()) ) {
                    Toast.makeText(join.this, "비밀번호가 일치하지 않습니다!", Toast.LENGTH_SHORT).show();
                    User_Password.setText("");
                    User_PasswordConfirm.setText("");
                    User_Password.requestFocus();
                    return;
                }

                if (!email.isEmpty() && !password.isEmpty()) {
                    ConnectServer connectServerPost = new ConnectServer();
                    connectServerPost.requestPost("http://15.164.118.95/hello/user", email, password);
                    Log.d("test", email);
                    Log.d("test", password);

                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "필수사항은 모두 입력하세요!", Toast.LENGTH_LONG).show();
                }

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

        public void requestPost(String url, String id, String password){
            MediaType MEDIA_TYPE = MediaType.parse("application/json");

            //Request Body에 서버에 보낼 데이터 작성
            RequestBody requestBody = new FormBody.Builder().add("email", "aaa@naver.com").add("password", password).add("name", id).build();
//            JSONObject postdata = new JSONObject();
//
//            RequestBody body = RequestBody.create(MEDIA_TYPE, postdata.toString());
//            String json = "email :"+id +"password :"+password + "name :" + id;
            Log.d("test",requestBody.toString());
            //작성한 Request Body와 데이터를 보낼 url을 Request에 붙임
            Request request = new Request.Builder().url(url).post(requestBody).build();
            Log.d("test", request.toString());
            //request를 Client에 세팅하고 Server로 부터 온 Response를 처리할 Callback 작성
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

    }


}
