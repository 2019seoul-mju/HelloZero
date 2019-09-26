package com.example.zeropayfinder;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class moneyBook extends AppCompatActivity {


    private TextView cost_sum;
    private Button costbtn;
    private EditText edit_content, edit_price;
    DecimalFormat df = new DecimalFormat("#,###원");
    private ListViewAdapter adapter;
    private ListView list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_money_book);
        costbtn = findViewById(R.id.costbtn);
        edit_content = findViewById(R.id.edit_content);
        edit_price = findViewById(R.id.edit_price);
        cost_sum = findViewById(R.id.cost_sum);
        list = findViewById(R.id.account_list);
        adapter = new ListViewAdapter();
        list.setAdapter(adapter);

        Intent intent=new Intent(this.getIntent());

        costbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

                        adapter.additem(contents,price);
                        adapter.notifyDataSetChanged();

                        edit_content.setText("");
                        edit_price.setText("");
                    }
                });
                dialog.show();
            }
        });
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

            Listitem listViewItem = listitems.get(position);

            Contents.setText(listViewItem.getContent());
            Price.setText(df.format(listViewItem.getPrice()));

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
                            listitems.remove(pos);
                            adapter.notifyDataSetChanged();
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
        public void additem(String content, int price){
            Listitem item = new Listitem();
            item.setContent(content);
            item.setPrice(price);

            listitems.add(item);
        }
    }
    public class Listitem{
        private String content;
        private int price;

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
}
