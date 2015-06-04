package com.github.cumulusava.android.cumulus.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.github.cumulusava.android.cumulus.R;
import com.github.cumulusava.android.cumulus.database.Article;

import java.util.List;

/**
 * Created by Magnus on 2015-05-02
 * Adapter for displaying list of articles saved. Ability to remove and click items
 * Used in {@link com.github.cumulusava.android.cumulus.fragment.SavedArticlesFragment}
 */
public class ArticlesAdapter extends RecyclerView.Adapter<ArticlesAdapterViewHolder>
        implements ArticlesAdapterViewHolder.ViewHolderInterface {

    private final List<Article> mArticles;
    private final ArticleAdapterCallback mCallback;

    public ArticlesAdapter(List<Article> mArticles, ArticleAdapterCallback mCallback){
        this.mArticles = mArticles;
        this.mCallback = mCallback;
    }

    @Override
    public ArticlesAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ArticlesAdapterViewHolder(LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.view_list_item_article, viewGroup, false), this);
    }

    @Override
    public void onBindViewHolder(ArticlesAdapterViewHolder viewHolder, int i) {
        Article article = mArticles.get(i);
        viewHolder.mArticle.setText(shortenTitle(article.getTitle()));
        viewHolder.mLink.setText(article.getLink());
    }

    @Override
    public int getItemCount() {
        return mArticles.size();
    }

    @Override /** Removing item from list and database in fragment */
    public void onRemove(int position) {
        mCallback.onRemoveArticle(mArticles.get(position));
        mArticles.remove(position);
        notifyItemRemoved(position);
    }

    @Override /** To open link with article */
    public void onArticleClick(int position) {
        mCallback.onArticleClick(mArticles.get(position));
    }

    /** Shortens the title to 26 letters if its too long */
    private String shortenTitle(String title){
        if (title.length() > 25) return title.substring(0, 25) + "...";
        return title;
    }

    public interface ArticleAdapterCallback{
        void onRemoveArticle(Article article);
        void onArticleClick(Article article);
    }
}