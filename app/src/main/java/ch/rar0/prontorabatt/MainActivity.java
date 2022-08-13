package ch.rar0.prontorabatt;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    public String url = "https://bon.coop-pronto.ch/de/start/newsletter";

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ConnectivityManager conManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = conManager.getActiveNetworkInfo();
        if (networkInfo!=null){ if (networkInfo.isConnected()){ Toast.makeText(MainActivity.this, getResources().getString(R.string.DownloadToast), Toast.LENGTH_LONG).show(); }
        } else { Toast.makeText(MainActivity.this, getResources().getString(R.string.DownloadToastNoConnection), Toast.LENGTH_LONG).show(); }

        WebView webView = findViewById(R.id.web_content);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        webView.loadUrl(url);
        webView.setWebViewClient(new WebViewClient(){
            public void onPageFinished(WebView webView, String url){
                webView.loadUrl("javascript:document.getElementById('edit-submit-direct').click()");
                webView.setWebViewClient(new WebViewClient(){
                    public void onPageFinished(WebView webView, String url){
                        webView.evaluateJavascript(
                                "(function() { return ('<html>'+document.getElementById('download').lastChild.href); })();",
                                href -> {
                                    String downloadURL = href.replace("\\u003Chtml>", "").replace("\"", "");
                                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(downloadURL)));
                                }
                        );
                    }
                });
            }
        });
    }

}