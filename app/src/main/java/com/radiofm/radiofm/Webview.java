package com.radiofm.radiofm;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class Webview extends AppCompatActivity {
    WebView webView;

    String url = "http://rojinegros838907.blogspot.mx/2018/01/privacy-policy-for-mobile-applications.html";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.web);

        webView = (WebView) findViewById(R.id.visor);

        webView.loadUrl("http://rojinegros838907.blogspot.mx/2018/01/privacy-policy-for-mobile-applications.html");

        webView.setWebViewClient(new WebViewClient() {
            public boolean shoulOverrideUrlLoading(WebView view, String url) {
                return false;
            }
        });
    }
}
