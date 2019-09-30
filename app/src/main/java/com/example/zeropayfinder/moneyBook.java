package com.example.zeropayfinder;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DecimalFormat;
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

public class moneyBook extends AppCompatActivity {


    private TextView cost_sum;
    private Button costbtn;
    private EditText edit_content, edit_price;
    DecimalFormat df = new DecimalFormat("#,###원");
    private ListViewAdapter adapter;
    private ListView list;
    Intent intent;
    public String jwt2;
    moneyBook.ConnectServer mc = new moneyBook.ConnectServer();
    int sum = 0;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss", Locale.KOREA);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_money_book);
        costbtn = findViewById(R.id.costbtn);
        edit_content = findViewById(R.id.edit_content);
        edit_price = findViewById(R.id.edit_price);
        cost_sum = findViewById(R.id.cost_sum);
        list = findViewById(R.id.account_list);

        adapter = new ListViewAdapter();
        //list.setAdapter(adapter);

        intent=new Intent(this.getIntent());

        final String jwt=intent.getStringExtra("jwt");
        jwt2 = jwt;
        mc.requestGet("http://15.164.118.95/hello/listMyPay", "search");
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        list.setAdapter(adapter);
        adapter.notifyDataSetChanged();




        costbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edit_content.getText().toString().length() == 0 || edit_price.getText().toString().length() == 0) {
                    Toast.makeText(moneyBook.this, "값을 넣어주세요", Toast.LENGTH_LONG).show();
                } else {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(moneyBook.this);
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
                            EditText ec = findViewById(R.id.edit_content);
                            EditText ep = findViewById(R.id.edit_price);

                            String contents = ec.getText().toString();
                            int price = Integer.parseInt(ep.getText().toString());
                            Date currentTime = new Date();
                            String Today_day = sdf.format(currentTime);
                            mc.requestPost("http://15.164.118.95/hello/addMyPay", contents, price, Today_day, 0);

                            InputMethodManager mInputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            mInputMethodManager.hideSoftInputFromWindow(edit_price.getWindowToken(), 0);

                            sum += price;
                            cost_sum.setText(df.format(sum));

                            edit_content.setText("");
                            edit_price.setText("");
                        }
                    });
                    dialog.show();
                }
            }
        });
    }
    public void onResume(Bundle savedInstanceState){

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
                convertView = inflater.inflate(R.layout.activity_money_book_detail,parent,false);
            }

            TextView Contents = (TextView) convertView.findViewById((R.id.item_content));
            TextView Price = (TextView) convertView.findViewById((R.id.item_cost));
            TextView Date = (TextView) convertView.findViewById((R.id.item_date));
            final Listitem listViewItem = listitems.get(position);

            Contents.setText(listViewItem.getContent());
            Price.setText(df.format(listViewItem.getPrice()));
            Date.setText(listViewItem.getDate());

            convertView.setOnClickListener(new View.OnClickListener(){
                public void onClick(View view){
                    AlertDialog.Builder dialog = new AlertDialog.Builder(moneyBook.this);
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
                            sum -= listitems.get(pos).getPrice();

                            mc.requestPost("http://15.164.118.95/hello/deleteMyPay",listitems.get(pos).getContent(),listitems.get(pos).getPrice()
                                    ,listitems.get(pos).getDate(),listitems.get(pos).getSpendno());
                            listitems.remove(pos);
                            adapter.notifyDataSetChanged();

                            cost_sum.setText(df.format(sum));
                        }
                    });
                    dialog.show();
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
        public void additem(String content, int price,String Today_day,int spendno){
            Listitem item = new Listitem();
            item.setContent(content);
            item.setPrice(price);
            item.setDate(Today_day);
            item.setSpendno(spendno);

            listitems.add(0,item);
        }
        public void addlist(String content, int price,String Today_day,int spendno){
            Listitem item = new Listitem();
            item.setContent(content);
            item.setPrice(price);
            item.setDate(Today_day);
            item.setSpendno(spendno);

            listitems.add(item);

        }
    }
    public class Listitem{
        private String content;
        private int price;
        private String date;
        private int spendno;
        public void setSpendno(int spendno){
            this.spendno = spendno;
        }
        public int getSpendno(){
            return spendno;
        }
        public void setDate(String date){
            this.date = date;
        }
        public String getDate(){
            return date;
        }
        public String getContent(){
            return content;
        }
        public void setContent(String content){
            this.content = content;
        }
        public int getPrice(){
            return price;
        }
        public void setPrice(int price){
            this.price = price;
        }
    }

     class ConnectServer
{
        int spno;

        //Client 생성
        OkHttpClient client = new OkHttpClient();

        public void requestGet(String url, String searchKey) {
            //URL에 포함할 Query문 작성 Name&Value
            HttpUrl.Builder urlBuilder = HttpUrl.parse(url).newBuilder();
            urlBuilder.addEncodedQueryParameter("searchKey", searchKey);

            String requestUrl = urlBuilder.build().toString();

            //Query문이 들어간 URL을 토대로 Request 생성
            Request request = new Request.Builder()
                    .url(requestUrl)
                    .get()
                    .header("x-access-token" , jwt2)
                    .build();

            //만들어진 Request를 서버로 요청할 Client 생성
            //Callback을 통해 비동기 방식으로 통신을 하여 서버로부터 받은 응답을 어떻게 처리 할 지 정의함

            client.newCall(request).enqueue(new Callback() {

                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override

                public void onResponse(Call call, Response response) throws IOException {
                    String result = response.body().string();
                    Gson gson = new Gson();
                    moneyGson info = gson.fromJson(result, moneyGson.class);
                    if (info.result.size() == 0) {
                        cost_sum.setText(df.format(sum));
                    }
                    for (int i = 0; i < info.result.size(); i++) {
                        if (Integer.parseInt(info.result.get(i).Spend_Status) == 1) {
                            adapter.addlist(info.result.get(i).Spend_Title, Integer.parseInt(info.result.get(i).Spend_Won)
                                    , info.result.get(i).Spend_Date, Integer.parseInt(info.result.get(i).Spend_No));
                            sum += Integer.parseInt(info.result.get(i).Spend_Won);
                            cost_sum.setText(df.format(sum));
                        }
                    }

                }

            });

        }

        public void requestPost(String url, final String content, final int price, final String date, int spendno) {
            if(url.equals("http://15.164.118.95/hello/deleteMyPay")){
                //Request Body에 서버에 보낼 데이터 작성
                JSONObject postdata = new JSONObject();

                try {
                    postdata.put("spendno", spendno);
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

                    }
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {

                    }
                });
            } else if(url.equals("http://15.164.118.95/hello/addMyPay")) {
                //Request Body에 서버에 보낼 데이터 작성
                JSONObject postdata = new JSONObject();

                try {
                    postdata.put("title", content);
                    postdata.put("money", price);
                    postdata.put("spenddate",date);
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

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {

                        String result = response.body().string();

                        Gson gson = new Gson();
                        moneyGson postinfo = gson.fromJson(result, moneyGson.class);
                        spno = (Integer.parseInt(postinfo.result.get(0).Spend_No));

                        adapter.additem(content,price,date,spno);

                    }
                });
                adapter.notifyDataSetChanged();
            }

        }
    }
    public class moneyGson {
        List<money_info> result;
        String isSuccess;
        String code;
        String message;
    }

    public class money_info {
        String Spend_No;
        String User_No;
        String Spend_Title;
        String Spend_Won;
        String Spend_Date;
        String Spend_Status;
    }
}
