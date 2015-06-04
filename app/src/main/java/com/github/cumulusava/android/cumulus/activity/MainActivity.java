package com.github.cumulusava.android.cumulus.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.BaseInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.webkit.WebChromeClient;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.github.cumulusava.android.cumulus.R;
import com.github.cumulusava.android.cumulus.customViews.CumulusWebView;
import com.github.cumulusava.android.cumulus.customViews.ObservableScrollView;
import com.github.cumulusava.android.cumulus.database.ArticleDataSource;
import com.github.cumulusava.android.cumulus.utils.PrefUtils;
import com.parse.ParseAnalytics;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * MainActivity to be the controller over everything
 * Handles fragments, database, menu etc.
 */
public class MainActivity extends AppCompatActivity
        implements CumulusWebView.WebViewCallback, ObservableScrollView.OnScrollChangedCallback {

    @InjectView(R.id.progressBar) ProgressBar mProgressBar;
    @InjectView(R.id.container_video_fullscreen) FrameLayout mVideoFullscreenContainer;
    @InjectView(R.id.scrollView_webView) ObservableScrollView mScrollView;
    @InjectView(R.id.webview_cumulus) CumulusWebView mWebView;
    @InjectView(R.id.toolbar) Toolbar mToolbar;
    @InjectView(R.id.appbar) AppBarLayout mToolbarContainer;
    private boolean shouldHideToolbar = true;
    private boolean isToolbarVisible = true;
    private View mCustomView;
    private WebChromeClient.CustomViewCallback customViewCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ParseAnalytics.trackAppOpenedInBackground(getIntent());
        setContentView(R.layout.activity_website);
        ButterKnife.inject(this);
        shouldHideToolbar = PrefUtils.shouldHideToolbar(this);

        setSupportActionBar(mToolbar);
        setTitle(R.string.app_name);

        mWebView.setUpWebView(this);
        mScrollView.setOnScrollChangedListener(this);

        if (savedInstanceState == null){
            Uri linkOpened = getIntent().getData();
            if (linkOpened != null) mWebView.loadUrl(linkOpened.toString());
            else mWebView.loadUrl(CumulusWebView.URL_CUMULUS);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_website, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_refresh:
                mWebView.reload();
                return true;
            case R.id.action_save:
                saveArticle(this, mWebView.getTitle(), mWebView.getUrl());
                Toast.makeText(this, R.string.toast_saved_article, Toast.LENGTH_SHORT).show();
                return true;
            case R.id.action_read_list:
                startActivity(new Intent(this, ArticlesActivity.class));
                return true;
            case R.id.action_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override /** Check for back presses to go back in webView or remove fragment  */
    public boolean onKeyUp(int keyCode, @NonNull KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            if (mWebView.canGoBack()){
                mWebView.goBack();
            } else onBackPressed();

            return true;
        }
        return super.onKeyUp(keyCode, event);
    }

    @Override /** WebView started loading */
    public void onStartLoading() {
        mProgressBar.setVisibility(View.VISIBLE);
        setTitle(R.string.loading);
        mScrollView.setScrollY(0);
    }

    @Override /** Progress update from webView */
    public void onProgress(int progress) {
        mProgressBar.setProgress(progress);
    }

    @Override /** WebView with site finished loading */
    public void onFinishedSite() {
        mProgressBar.setVisibility(View.GONE);
        setTitle(R.string.app_name);
    }

    @Override /** Show fullscreen video */
    public void onShowCustomView(View view, WebChromeClient.CustomViewCallback callback) {
        // if a view already exists then immediately terminate the new one
        if (mCustomView != null) {
            callback.onCustomViewHidden();
            return;
        }

        mCustomView = view;
        mToolbarContainer.setVisibility(View.GONE);
        mVideoFullscreenContainer.setVisibility(View.VISIBLE);
        mVideoFullscreenContainer.addView(view);
        customViewCallback = callback;
    }

    @Override /** Hide fullscreen video */
    public void onHideCustomView() {
        if (mCustomView == null) return;

        mToolbarContainer.setVisibility(View.VISIBLE);
        mVideoFullscreenContainer.setVisibility(View.GONE);

        //Hide the custom view
        mCustomView.setVisibility(View.GONE);

        //Remove the custom view from its container
        mVideoFullscreenContainer.removeView(mCustomView);
        customViewCallback.onCustomViewHidden();

        mCustomView = null;
    }

    @Override /** WebView is scrolling up */
    public void onScrollUp() {
        if (!isToolbarVisible) animateToolbar(0, new DecelerateInterpolator());
    }

    @Override /** WebView is scrolling down */
    public void onScrollDown(int pos) {
        //Checks if toolbar is visible but also if not scrolling to high up to hide toolbar
        if (isToolbarVisible && pos > 300) animateToolbar(-mToolbarContainer.getHeight(),
                new AccelerateInterpolator());
    }

    /** Animates toolbar up and down on scroll */
    private void animateToolbar(float translationYValue, BaseInterpolator interpolator){
        if (shouldHideToolbar){
            mToolbarContainer.animate().translationY(translationYValue).setInterpolator(interpolator);
            isToolbarVisible = !isToolbarVisible;
        }
    }

    /**
     * Save an article to the database
     */
    private static void saveArticle(Context context, String title, String url){
        ArticleDataSource source = new ArticleDataSource(context);
        source.open();
        source.createArticle(title, url);
        source.close();
    }
}