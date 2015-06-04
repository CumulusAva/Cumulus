package com.github.cumulusava.android.cumulus.customViews;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.github.cumulusava.android.cumulus.R;

/**
 * Created by Magnus on 2015-05-13
 * A WebView to display cumulus page
 * Custom clients to provide progress feedback, fullscreen video etc.
 */
public class CumulusWebView extends WebView{
    private WebViewCallback mCallback;
    public static final String URL_CUMULUS = "http://cumulusava.com";

    public CumulusWebView(Context context) {
        super(context);
        setSettings();
    }

    public CumulusWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setSettings();
    }

    public CumulusWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setSettings();
    }

    public void setUpWebView(WebViewCallback callback){
        this.mCallback = callback;
        setWebViewClient(new CumulusWebViewClient());
        setWebChromeClient(new CumulusChromeClient());
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void setSettings(){
        WebSettings settings = getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setBuiltInZoomControls(true);
        settings.setDisplayZoomControls(false);
    }

    /**
     * Created by Magnus on 2015-04-17
     * Overrides WebViewClient to make it custom.
     * A lot passed back to activity and webView to reload, show progress etc.
     */
    public class CumulusWebViewClient extends WebViewClient {
        private static final String CUMULUS_URL_BASE = "cumulusava.com";

        @Override /** Open links inside app instead of eg. Chrome when link is from Cumulus */
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (Uri.parse(url).getHost().equals(CUMULUS_URL_BASE)){
                loadUrl(url);
                return false;
            }
            getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
            return true;
        }

        @Override /** Displaying dialog to user when error from loading website */
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            Log.e(this.getClass().getName(), "Error: " + description);
            new AlertDialog.Builder(getContext())
                    .setTitle(R.string.error)
                    .setMessage(description)
                    .setPositiveButton(R.string.try_again, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            reload(); //Reloading the webView
                            dialog.dismiss();
                        }
                    })
                    .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create().show();
            super.onReceivedError(view, errorCode, description, failingUrl);
        }

        @Override /** When started loading site */
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            mCallback.onStartLoading();
            super.onPageStarted(view, url, favicon);
        }

        @Override /** Finished loading the site */
        public void onPageFinished(WebView view, String url) {
            mCallback.onFinishedSite();
            super.onPageFinished(view, url);
        }
    }

    /**
     * Created by Magnus on 2015-04-18
     * Overrides chromeClient to be able to get progress changes
     * Progress displayed in progressBar when site is updating
     * Also enables fullscreen video
     */
    private class CumulusChromeClient extends WebChromeClient {

        @Override /** Progress updating for loading website */
        public void onProgressChanged(WebView view, int newProgress) {
            mCallback.onProgress(newProgress);
            super.onProgressChanged(view, newProgress);
        }

        @Override
        public void onShowCustomView(View view, CustomViewCallback callback) {
            mCallback.onShowCustomView(view, callback);
        }

        @Override
        public void onHideCustomView() {
            super.onHideCustomView();
            mCallback.onHideCustomView();
        }
    }

    /**
     * Interface to be able to interact when changes are made in clients
     */
    public interface WebViewCallback{
        void onStartLoading();
        void onProgress(int progress);
        void onFinishedSite();

        void onShowCustomView(View view, WebChromeClient.CustomViewCallback callback);
        void onHideCustomView();
    }
}