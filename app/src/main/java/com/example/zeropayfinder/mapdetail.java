package com.example.zeropayfinder;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.service.voice.AlwaysOnHotwordDetector;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Utf8;

public class mapdetail extends AppCompatActivity {
    private TextView name,reviewdate;
    private Button reviewbtn;
    private EditText edit_review;
    private RatingBar ratingbar;
    private ListView list;
    private ListViewAdapter adapter;
    public String jwt2;
    Intent intent;
    private static int Franchise_no;
    private static String Franchise_name;
    mapdetail.ConnectServer rc = new mapdetail.ConnectServer();
    private static String[] split;
    private String connectuser;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss", Locale.KOREA);
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_infowindow_detail);

        ratingbar = findViewById(R.id.ratingBarInficator);
        name = findViewById(R.id.name);
        reviewdate = findViewById(R.id.reviewdate);
        reviewbtn = findViewById(R.id.reviewbtn);
        edit_review = findViewById(R.id.edit_review);
        list = findViewById(R.id.review_list);

        intent=new Intent(this.getIntent());
        ratingbar.setOnRatingBarChangeListener(new Listener());

        name.setText(Franchise_name);

        adapter = new ListViewAdapter();
        list.setAdapter(adapter);

        final String jwt=intent.getStringExtra("jwt");
        jwt2 = jwt;

        try{
            connectuser = decodeJWT(jwt2);
        } catch (Exception e){

        }
        rc.requestGet("http://15.164.118.95/hello/detailZero/" + Franchise_no, "search");
        adapter.notifyDataSetChanged();


        reviewbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (jwt2 == null) {
                    Toast.makeText(mapdetail.this, "로그인이 필요한 서비스입니다.", Toast.LENGTH_SHORT).show();
                } else {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(mapdetail.this);
                    dialog.setMessage("등록하시겠습니까?");
                    dialog.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    dialog.setPositiveButton("네", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            EditText ec = findViewById(R.id.edit_review);
                            RatingBar ep = findViewById(R.id.ratingBarInficator);

                            String contents = ec.getText().toString();
                            float rating = ep.getRating();
                            rc.requestPost("http://15.164.118.95/hello/addReview",contents,rating,0);


                            edit_review.setText("");
                        }
                    });
                    dialog.show();
                }
            }
        });
    }
    public class payload{
        String date;
        String email;
        String password;
        String userno;
    }
    public static String decodeJWT(String EncodeString) throws Exception{
        String[] spliter = EncodeString.split("\\.");
        Log.d("pay", "Payload" + getJSon(spliter[1]));
        Gson gson = new Gson();
        payload info = gson.fromJson(getJSon(spliter[1]), payload.class);
        return info.email;
    }
    public static String getJSon(String EndcodeString) throws UnsupportedEncodingException{
        byte[] decodebyte = Base64.decode(EndcodeString,Base64.URL_SAFE);
        return new String(decodebyte, "UTF-8");
    }

    class Listener implements RatingBar.OnRatingBarChangeListener
    {
        @Override
        public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
            ratingbar.setRating(rating);
        }
    }
    public class ListViewAdapter extends BaseAdapter {
        private ArrayList<Listitem> listitems = new ArrayList<Listitem>();
        public ListViewAdapter(){
        }

        public int getCount(){
            return listitems.size();
        }

        public View getView(int position, View convertView, ViewGroup parent){
            final int pos = position;
            final Context context = parent.getContext();

            if(convertView == null){
                LayoutInflater inflater = (LayoutInflater) context.getSystemService((Context.LAYOUT_INFLATER_SERVICE));
                convertView = inflater.inflate(R.layout.infowindow_detail_list,parent,false);
            }

            TextView Userid = (TextView) convertView.findViewById((R.id.userid));
            TextView Date = (TextView) convertView.findViewById((R.id.reviewdate));
            RatingBar Ratingbar = (RatingBar) convertView.findViewById((R.id.ratingbar));
            TextView Review_contents = (TextView) convertView.findViewById((R.id.review_contents));
            final Listitem listViewItem = listitems.get(position);

            Userid.setText(listViewItem.getUserid().substring(0,4)+ "****");
            Date.setText(listViewItem.getDate().substring(0,10));
            Ratingbar.setRating(listViewItem.getRating());
            Review_contents.setText(listViewItem.getContent());

            convertView.setOnClickListener(new View.OnClickListener(){

                public void onClick(View view){
                    if(connectuser.equals(listitems.get(pos).getUserid())){
                        AlertDialog.Builder dialog = new AlertDialog.Builder(mapdetail.this);
                        dialog.setMessage("삭제하시겠습니까?");
                        dialog.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        dialog.setPositiveButton("네", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                rc.requestPost("http://15.164.118.95/hello/deleteMyReview",listitems.get(pos).content,listitems.get(pos).getRating()
                                ,listitems.get(pos).getReviewno());
                                listitems.remove(pos);
                                adapter.notifyDataSetChanged();

                            }
                        });
                        dialog.show();
                    } else {

                    }

                }
            });

            return convertView;

        }

        public long getItemId(int position){
            return position;
        }
        public Object getItem(int position){
            return listitems.get(position);
        }
        public void additem(String content, float rating,int rvno,String today){
            Listitem item = new Listitem();
            item.setUserid(connectuser);
            item.setContent(content);
            item.setRating(rating);
            item.setReviewno(rvno);
            item.setDate(today);

            listitems.add(0,item);
        }
        public void addlist(String uid, String content, float rating,int reviewno, String date){
            Listitem item = new Listitem();
            item.setUserid(uid);
            item.setContent(content);
            item.setRating(rating);
            item.setReviewno(reviewno);
            item.setDate(date);

            listitems.add(item);
        }
    }
    public class Listitem{
        private String content;
        private float rating;
        private String userid;
        private int reviewno;
        private String date;
        public void setDate(String date){
            this.date = date;
        }
        public String getDate(){
            return date;
        }
        public void setReviewno(int reviewno){
            this.reviewno = reviewno;
        }
        public int getReviewno(){
            return reviewno;
        }
        public void setUserid(String userid){
            this.userid = userid;
        }
        public String getUserid(){
            return userid;
        }
        public String getContent(){
            return content;
        }
        public void setContent(String content){
            this.content = content;
        }
        public float getRating(){
            return rating;
        }
        public void setRating(float rating){
            this.rating = rating;
        }
    }
    public static void setFranchise_no(int fn){
        Franchise_no = fn;
    }
    public static void setFranchise_name(String  fn){
        Franchise_name = fn;
    }

    class ConnectServer {
        int rvno;


        //Client 생성
        OkHttpClient client = new OkHttpClient();

        public void requestGet(String url, String searchKey) {
            //URL에 포함할 Query문 작성 Name&Value
            HttpUrl.Builder urlBuilder = HttpUrl.parse(url).newBuilder();
            urlBuilder.addEncodedQueryParameter("searchKey", searchKey);

            String requestUrl = urlBuilder.build().toString();
            JSONObject postdata = new JSONObject();
            Log.d("dddd", "DDDD"+Franchise_no);
            try {
                postdata.put("franchiseno", Franchise_no);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            RequestBody body = RequestBody.create(postdata.toString(), MediaType.parse("application/json; charset=utf-8"));
            //Query문이 들어간 URL을 토대로 Request 생성
            Request request;
            if(jwt2 == null){
                request = new Request.Builder()
                        .url(requestUrl)
                        .post(body)
                        .get()
                        .build();
            }
            else {
                request = new Request.Builder()
                        .url(requestUrl)
                        .header("x-access-token", jwt2)
                        .post(body)
                        .get()
                        .build();
            }
            //만들어진 Request를 서버로 요청할 Client 생성
            //Callback을 통해 비동기 방식으로 통신을 하여 서버로부터 받은 응답을 어떻게 처리 할 지 정의함

            client.newCall(request).enqueue(new Callback() {

                @Override
                public void onFailure(Call call, IOException e) {
                    Log.d("error", "Connect Server Error is " + e.toString());
                }

                @Override

                public void onResponse(Call call, Response response) throws IOException {
                    String result = response.body().string();
                    Gson gson = new Gson();
                    reviewGson info = gson.fromJson(result, reviewGson.class);
                    for(int i = 0; i< info.result.size(); i++){
                        adapter.addlist(info.result.get(i).User_Email,info.result.get(i).Review_Content
                                ,Float.parseFloat(info.result.get(i).Review_Star),Integer.parseInt(info.result.get(i).Review_No),info.result.get(i).Review_Date);


                    }
                    list = findViewById(R.id.review_list);
                    list.setAdapter(adapter);
                }

            });
        }

        public void requestPost(String url, final String content, final float rating, int reviewno) {
            if(url.equals("http://15.164.118.95/hello/deleteMyReview")){
                //Request Body에 서버에 보낼 데이터 작성
                JSONObject postdata = new JSONObject();

                try {
                    postdata.put("reviewno", reviewno);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                RequestBody body = RequestBody.create(postdata.toString(), MediaType.parse("application/json; charset=utf-8"));
                //작성한 Body와 데이터를 보낼 url을 Request에 붙임
                Request request = new Request.Builder()
                        .url(url)
                        .header("x-access-token" , jwt2)
                        .post(body)
                        .build();

                //request를 Client에 세팅하고 Server로 부터 온 Response를 처리할 Callback 작성
                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.d("error", "Connect Server Error is " + e.toString());
                    }
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {

                    }
                });
            } else if(url.equals("http://15.164.118.95/hello/addReview")) {
                //Request Body에 서버에 보낼 데이터 작성
                JSONObject postdata = new JSONObject();

                try {
                    postdata.put("franchiseno", Franchise_no);
                    postdata.put("content", content);
                    postdata.put("star",rating);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                RequestBody body = RequestBody.create(postdata.toString(), MediaType.parse("application/json; charset=utf-8"));
                //작성한 Body와 데이터를 보낼 url을 Request에 붙임
                Request request = new Request.Builder()
                        .url(url)
                        .header("x-access-token" , jwt2)
                        .post(body)
                        .build();

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
                        reviewGson postinfo = gson.fromJson(result, reviewGson.class);
                        rvno = (Integer.parseInt(postinfo.result.get(0).Review_No));
                        Date today = new Date();
                        String todays = sdf.format(today);
                        adapter.additem(content,rating,rvno,todays);
                    }
                });
                adapter.notifyDataSetChanged();

            }

        }
    }
    public class reviewGson {
        List<review_info> result;
        String isSuccess;
        String code;
        String message;
    }

    public class review_info {
        String Review_No;
        String Franchise_NO;
        String Review_Star;
        String Review_Content;
        String User_Email;
        String User_No;
        String Review_Date;
    }
}
