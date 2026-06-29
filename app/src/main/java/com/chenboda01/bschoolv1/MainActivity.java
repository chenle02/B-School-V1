package com.chenboda01.bschoolv1;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebChromeClient;
import android.webkit.JavascriptInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.widget.Toast;

public class MainActivity extends Activity {
    private WebView webView;
    public class AndroidBridge {
        @JavascriptInterface
        public void openApp(String pkg, String cls, String label) {
            runOnUiThread(() -> {
                try {
                    PackageManager pm = getPackageManager();
                    Intent launch = pm.getLaunchIntentForPackage(pkg);
                    if (launch == null && cls != null && cls.length() > 0) {
                        launch = new Intent(Intent.ACTION_MAIN);
                        launch.addCategory(Intent.CATEGORY_LAUNCHER);
                        launch.setClassName(pkg, cls);
                    }
                    if (launch != null) startActivity(launch);
                    else Toast.makeText(MainActivity.this, label + " is not installed yet.", Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    Toast.makeText(MainActivity.this, "Could not open " + label + ".", Toast.LENGTH_LONG).show();
                }
            });
        }
    }
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        webView = new WebView(this);
        setContentView(webView);
        WebSettings s = webView.getSettings();
        s.setJavaScriptEnabled(true);
        s.setDomStorageEnabled(true);
        s.setAllowFileAccess(true);
        s.setAllowContentAccess(true);
        webView.setWebChromeClient(new WebChromeClient());
        webView.addJavascriptInterface(new AndroidBridge(), "AndroidBridge");
        webView.loadUrl("file:///android_asset/index.html");
    }
    public void onBackPressed() {
        webView.evaluateJavascript("window.bschoolBack && window.bschoolBack()", null);
    }
}
