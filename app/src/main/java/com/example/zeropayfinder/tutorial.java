package com.example.zeropayfinder;

import androidx.appcompat.app.AppCompatActivity;

import android.webkit.WebView;
import android.os.Bundle;

public class tutorial extends AppCompatActivity {
    private WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);

        webView = (WebView)findViewById(R.id.webview);
        webView.loadUrl("file:///android_asset/www/index.html");
    }
}
