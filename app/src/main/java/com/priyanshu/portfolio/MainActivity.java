package com.priyanshu.portfolio;

import android.app.Activity;
import android.os.Bundle;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MainActivity extends Activity {

private WebView webView;
private FrameLayout root;
private View loadingScreen;
private View errorScreen;

private final String website =
        "https://priyanshu431010.github.io/my-portfolio/";

private int dp(int value) {
    return (int) (value * getResources()
            .getDisplayMetrics().density + 0.5f);
}

private GradientDrawable rounded(int color, int radius) {
    GradientDrawable shape = new GradientDrawable();
    shape.setColor(color);
    shape.setCornerRadius(dp(radius));
    return shape;
}

private TextView text(String value, int size, int color, boolean bold) {
    TextView t = new TextView(this);
    t.setText(value);
    t.setTextSize(size);
    t.setTextColor(color);
    t.setGravity(Gravity.CENTER);
    if (bold) t.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
    return t;
}

@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    getWindow().setStatusBarColor(Color.rgb(10, 12, 28));
    getWindow().setNavigationBarColor(Color.rgb(10, 12, 28));

    root = new FrameLayout(this);
    root.setBackgroundColor(Color.rgb(10, 12, 28));

    webView = new WebView(this);
    WebSettings settings = webView.getSettings();
    settings.setJavaScriptEnabled(true);
    settings.setDomStorageEnabled(true);
    settings.setUseWideViewPort(true);
    settings.setLoadWithOverviewMode(true);
    settings.setSupportZoom(false);

    webView.setBackgroundColor(Color.rgb(10, 12, 28));
    webView.setWebViewClient(new WebViewClient() {
        @Override
        public void onPageStarted(WebView view, String url,
                                  android.graphics.Bitmap favicon) {
            loadingScreen.setVisibility(View.VISIBLE);
            errorScreen.setVisibility(View.GONE);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            loadingScreen.setVisibility(View.GONE);
        }

        @Override
        public void onReceivedError(WebView view,
                android.webkit.WebResourceRequest request,
                android.webkit.WebResourceError error) {
            if (request.isForMainFrame()) {
                loadingScreen.setVisibility(View.GONE);
                errorScreen.setVisibility(View.VISIBLE);
            }
        }
    });

    root.addView(webView, new FrameLayout.LayoutParams(
            -1, -1));

    LinearLayout loading = new LinearLayout(this);
    loading.setOrientation(LinearLayout.VERTICAL);
    loading.setGravity(Gravity.CENTER);
    loading.setBackground(new GradientDrawable(
            GradientDrawable.Orientation.TL_BR,
            new int[]{Color.rgb(12, 16, 42),
                      Color.rgb(35, 22, 74),
                      Color.rgb(10, 12, 28)}));

    TextView logo = text("P", 38, Color.WHITE, true);
    logo.setBackground(rounded(Color.rgb(112, 76, 255), 24));
    LinearLayout.LayoutParams logoParams =
            new LinearLayout.LayoutParams(dp(76), dp(76));
    logoParams.bottomMargin = dp(20);
    loading.addView(logo, logoParams);

    loading.addView(text("PRIYANSHU", 25, Color.WHITE, true));
    loading.addView(text("PORTFOLIO", 14,
            Color.rgb(178, 166, 255), true));

    ProgressBar progress = new ProgressBar(this);
    LinearLayout.LayoutParams progressParams =
            new LinearLayout.LayoutParams(dp(36), dp(36));
    progressParams.topMargin = dp(26);
    loading.addView(progress, progressParams);

    loadingScreen = loading;
    root.addView(loadingScreen, new FrameLayout.LayoutParams(-1, -1));

    LinearLayout error = new LinearLayout(this);
    error.setOrientation(LinearLayout.VERTICAL);
    error.setGravity(Gravity.CENTER);
    error.setPadding(dp(28), dp(28), dp(28), dp(28));
    error.setBackgroundColor(Color.rgb(10, 12, 28));

    error.addView(text("No Internet Connection", 23,
            Color.WHITE, true));

    TextView message = text(
            "Check your connection and try again.",
            15, Color.LTGRAY, false);
    LinearLayout.LayoutParams messageParams =
            new LinearLayout.LayoutParams(-1, -2);
    messageParams.topMargin = dp(12);
    messageParams.bottomMargin = dp(20);
    error.addView(message, messageParams);

    Button retry = new Button(this);
    retry.setText("Try Again");
    retry.setTextColor(Color.WHITE);
    retry.setBackground(rounded(Color.rgb(112, 76, 255), 16));
    retry.setOnClickListener(v -> {
        errorScreen.setVisibility(View.GONE);
        loadingScreen.setVisibility(View.VISIBLE);
        webView.loadUrl(website);
    });
    error.addView(retry);

    errorScreen = error;
    errorScreen.setVisibility(View.GONE);
    root.addView(errorScreen, new FrameLayout.LayoutParams(-1, -1));

    setContentView(root);

    if (savedInstanceState != null) {
        webView.restoreState(savedInstanceState);
    } else {
        webView.loadUrl(website);
    }
}

@Override
public void onBackPressed() {
    if (webView != null && webView.canGoBack()) {
        webView.goBack();
    } else {
        super.onBackPressed();
    }
}

@Override
protected void onSaveInstanceState(Bundle outState) {
    webView.saveState(outState);
    super.onSaveInstanceState(outState);
}

@Override
protected void onDestroy() {
    if (webView != null) webView.destroy();
    super.onDestroy();
}

      }
