package com.github.cumulusava.android.cumulus.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.github.cumulusava.android.cumulus.R;
import com.github.cumulusava.android.cumulus.database.Article;
import com.github.cumulusava.android.cumulus.fragment.SavedArticlesFragment;

/**
 * Created by Elev on 2015-06-04
 */
public class ArticlesActivity extends BaseFragmentActivity
        implements SavedArticlesFragment.ArticleListCallback {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mToolbar.setTitle(R.string.action_read_list);

        getFragmentManager().beginTransaction()
                .add(R.id.fragment_container, SavedArticlesFragment.newInstance())
                .commit();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_articles, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override /** Open link for article in webView */
    public void onArticleClick(Article article) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setData(Uri.parse(article.getLink()));
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        this.finish();
    }
}