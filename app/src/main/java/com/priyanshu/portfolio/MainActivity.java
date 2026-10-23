package com.priyanshu.portfolio;

import android.app.Activity;
import android.os.Bundle;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
    private static final String HOME_URL = "https://priyanshu431010.github.io/my-portfolio/";
    private FrameLayout root;
    private WebView webView;
    private View loadingScreen;
    private View errorScreen;
    private boolean pageFailed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(Color.rgb(10, 12, 28));
        getWindow().setNavigationBarColor(Color.rgb(10, 12, 28));
        getWindow().getDecorView().setSystemUiVisibility(0);
        buildInterface();
        configureWebView();
        if (savedInstanceState != null) {
            webView.restoreState(savedInstanceState);
        } else {
            webView.loadUrl(HOME_URL);
        }
    }

    private GradientDrawable rounded(int color, int radiusDp) {
        GradientDrawable d = new GradientDrawable();
        d.setColor(color);
        d.setCornerRadius(radiusDp * getResources().getDisplayMetrics().density);
        return d;
    }

    private TextView label(String text, int size, int color, boolean bold) {
        TextView v = new TextView(this);
        v.setText(text);
        v.setTextSize(size);
        v.setTextColor(color);
        v.setGravity(Gravity.CENTER);
        v.setTypeface(bold ? Typeface.DEFAULT_BOLD : Typeface.DEFAULT);
        return v;
    }

    private void buildInterface() {
        root = new FrameLayout(this);
        root.setBackgroundColor(Color.rgb(10, 12, 28));

        webView = new WebView(this);
        root.addView(webView, new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        // Premium launch/loading screen
        LinearLayout loading = new LinearLayout(this);
        loading.setOrientation(LinearLayout.VERTICAL);
        loading.setGravity(Gravity.CENTER);
        loading.setPadding(dp(28), dp(28), dp(28), dp(28));
        loading.setBackground(new GradientDrawable(
                GradientDrawable.Orientation.TL_BR,
                new int[]{Color.rgb(12, 16, 42), Color.rgb(35, 22, 74), Color.rgb(10, 12, 28)}));
        TextView mark = label("P", 36, Color.WHITE, true);
        mark.setBackground(rounded(Color.rgb(112, 76, 255), 24));
        LinearLayout.LayoutParams markLp = new LinearLayout.LayoutParams(dp(76), dp(76));
        markLp.bottomMargin = dp(22);
        loading.addView(mark, markLp);
        TextView title = label("PRIYANSHU", 25, Color.WHITE, true);
        title.setLetterSpacing(0.12f);
        loading.addView(title);
        TextView subtitle = label("PORTFOLIO", 13, Color.rgb(178, 166, 255), true);
        subtitle.setLetterSpacing(0.28f);
        LinearLayout.LayoutParams subLp = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        subLp.topMargin = dp(8);
        subLp.bottomMargin = dp(28);
        loading.addView(subtitle, subLp);
        ProgressBar spinner = new ProgressBar(this);
        loading.addView(spinner, new LinearLayout.LayoutParams(dp(34), dp(34)));
        TextView loadingText = label("Preparing your experience…", 13, Color.rgb(208, 211, 235), false);
        LinearLayout.LayoutParams textLp = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        textLp.topMargin = dp(16);
        loading.addView(loadingText, textLp);
        loadingScreen = loading;
        root.addView(loadingScreen, new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        LinearLayout error = new LinearLayout(this);
        error.setOrientation(LinearLayout.VERTICAL);
        error.setGravity(Gravity.CENTER);
        error.setPadding(dp(30), dp(30), dp(30), dp(30));
        error.setBackgroundColor(Color.rgb(10, 12, 28));
        TextView icon = label("↻", 42, Color.rgb(177, 153, 255), true);
        error.addView(icon, new LinearLayout.LayoutParams(dp(70), dp(70)));
        TextView errorTitle = label("You're offline", 24, Color.WHITE, true);
        LinearLayout.LayoutParams et = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        et.topMargin = dp(18);
        error.addView(errorTitle, et);
        TextView errorMsg = label("Check your internet connection and try again.", 15,
                Color.rgb(190, 194, 218), false);
        LinearLayout.LayoutParams em = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        em.topMargin = dp(10);
        em.bottomMargin = dp(24);
        error.addView(errorMsg, em);
        Button retry = new Button(this);
        retry.setText("TRY AGAIN");
        retry.setTextColor(Color.WHITE);
        retry.setAllCaps(false);
        retry.setTextSize(15);
        retry.setBackground(rounded(Color.rgb(112, 76, 255), 18));
        retry.setOnClickListener(v -> {
            pageFailed = false;
            errorScreen.setVisibility(View.GONE);
            loadingScreen.setVisibility(View.VISIBLE);
            webView.loadUrl(HOME_URL);
        });
        error.addView(retry, new LinearLayout.LayoutParams(dp(180), dp(52)));
        errorScreen = error;
        errorScreen.setVisibility(View.GONE);
        root.addView(errorScreen, new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        setContentView(root);
    }

    private void configureWebView() {
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setLoadsImagesAutomatically(true);
        settings.setLoadWithOverviewMode(true);
        settings.setUseWideViewPort(true);
        settings.setSupportZoom(false);
        settings.setBuiltInZoomControls(false);
        settings.setMediaPlaybackRequiresUserGesture(true);
        webView.setOverScrollMode(View.OVER_SCROLL_NEVER);
        webView.setBackgroundColor(Color.rgb(10, 12, 28));
        webView.setWebChromeClient(new WebChromeClient());
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, android.graphics.Bitmap favicon) {
                pageFailed = false;
                loadingScreen.setVisibility(View.VISIBLE);
                errorScreen.setVisibility(View.GONE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                loadingScreen.setVisibility(View.GONE);
                if (!pageFailed) errorScreen.setVisibility(View.GONE);
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                if (request.isForMainFrame()) {
                    pageFailed = true;
                    loadingScreen.setVisibility(View.GONE);
                    errorScreen.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private int dp(int value) {
        return (int) (value * getResources().getDisplayMetrics().density + 0.5f);
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
        if (webView != null) webView.saveState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        if (webView != null) {
            webView.stopLoading();
            webView.destroy();
        }
        super.onDestroy();
    }
}
