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
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.util.Log;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;





public class join extends AppCompatActivity {

    private EditText Join_User_Email;
    private EditText Join_User_Name;
    private EditText Join_User_Password;
    private EditText Join_User_PasswordConfirm;
    private Button btnJoin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        Join_User_Email = (EditText) findViewById(R.id.Join_User_Email);
//        Join_User_Name = (EditText) findViewById(R.id.Join_User_Email);
        Join_User_Password = (EditText) findViewById(R.id.Join_User_Password);
        Join_User_PasswordConfirm = (EditText) findViewById(R.id.Join_User_PasswordConfirm);
        btnJoin = (Button) findViewById(R.id.btnJoin);
        // 비밀번호 일치 검사
        Join_User_PasswordConfirm.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String password = Join_User_Password.getText().toString();
                String confirm = Join_User_PasswordConfirm.getText().toString();

                if( password.equals(confirm) ) {
                    Join_User_Password.setBackgroundColor(Color.GREEN);
                    Join_User_PasswordConfirm.setBackgroundColor(Color.GREEN);
                } else {
                    Join_User_Password.setBackgroundColor(Color.RED);
                    Join_User_PasswordConfirm.setBackgroundColor(Color.RED);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        btnJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = Join_User_Email.getText().toString().trim();
                String password = Join_User_Password.getText().toString().trim();

                // 이메일 입력 확인
                if( Join_User_Email.getText().toString().length() == 0 ) {
                    Toast.makeText(join.this, "Email을 입력하세요!", Toast.LENGTH_SHORT).show();
                    Join_User_Email.requestFocus();
                    return;
                }

                // 비밀번호 입력 확인
                if( Join_User_Password.getText().toString().length() == 0 ) {
                    Toast.makeText(join.this, "비밀번호를 입력하세요!", Toast.LENGTH_SHORT).show();
                    Join_User_Password.requestFocus();
                    return;
                }

                // 비밀번호 확인 입력 확인
                if( Join_User_PasswordConfirm.getText().toString().length() == 0 ) {
                    Toast.makeText(join.this, "비밀번호 확인을 입력하세요!", Toast.LENGTH_SHORT).show();
                    Join_User_PasswordConfirm.requestFocus();
                    return;
                }

                // 비밀번호 일치 확인
                if( !Join_User_Password.getText().toString().equals(Join_User_PasswordConfirm.getText().toString()) ) {
                    Toast.makeText(join.this, "비밀번호가 일치하지 않습니다!", Toast.LENGTH_SHORT).show();
                    Join_User_Password.setText("");
                    Join_User_PasswordConfirm.setText("");
                    Join_User_Password.requestFocus();
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

            //Request Body에 서버에 보낼 데이터 작성
            JSONObject postdata = new JSONObject();
            try {
                postdata.put("email", id);
                postdata.put("password", password);
                postdata.put("name", id);
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
                    Log.d("aaaa", "Response Body is " + response.body().string());
                }
            });
        }

    }


}
