package com.github.cumulusava.android.cumulus.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.cumulusava.android.cumulus.R;
import com.github.cumulusava.android.cumulus.adapter.ArticlesAdapter;
import com.github.cumulusava.android.cumulus.database.Article;
import com.github.cumulusava.android.cumulus.database.ArticleDataSource;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Magnus on 2015-05-02
 * Fragment to display list with saved articles from database
 * Calls back to activity which handles database changes
 */
public class SavedArticlesFragment extends Fragment
        implements ArticlesAdapter.ArticleAdapterCallback {

    @InjectView(R.id.recycler_view) RecyclerView mRecyclerView;
    @InjectView(R.id.text_no_articles) TextView noArticlesText;
    private ArticleListCallback mCallback;
    private ArticleDataSource mDataSource;

    public static SavedArticlesFragment newInstance(){
        return new SavedArticlesFragment();
    }

    public SavedArticlesFragment(){}

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mCallback = (ArticleListCallback) activity;
        mDataSource = new ArticleDataSource(activity);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View parentView = inflater.inflate(R.layout.fragment_articles, container, false);
        ButterKnife.inject(this, parentView);

        mDataSource.open();
        List<Article> articles = mDataSource.getAllArticles();

        if (articles != null && articles.size() > 0) setUpList(articles);
        else hideRecyclerView();

        return parentView;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mDataSource != null) mDataSource.open();
    }

    @Override
    public void onPause() {
        super.onPause();
        mDataSource.close();
    }

    @Override /** Article selected, activity handles opening it */
    public void onArticleClick(Article article) {
        mCallback.onArticleClick(article);
    }

    @Override /** Removing article selected, activity removes from database */
    public void onRemoveArticle(Article article) {
        mDataSource.deleteArticle(article);
    }

    /** Sets up the recyclerView to show articles */
    private void setUpList(List<Article> articles){
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(new ArticlesAdapter(articles, this));
    }

    /** Hides list and shows text to user that list is empty */
    private void hideRecyclerView(){
        mRecyclerView.setVisibility(View.GONE);
        noArticlesText.setVisibility(View.VISIBLE);
    }

    public interface ArticleListCallback {
        void onArticleClick(Article article);
    }
}