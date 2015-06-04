package com.github.cumulusava.android.cumulus.adapter;

import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.cumulusava.android.cumulus.R;

/**
 * Created by Magnus on 2015-05-02
 * ViewHolder for a single item containing link and name of an article saved.
 * Used in {@link ArticlesAdapter}
 */
public class ArticlesAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    final TextView mArticle;
    final TextView mLink;
    private final ViewHolderInterface mCallback;

    public ArticlesAdapterViewHolder(View view, ViewHolderInterface mCallback){
        super(view);
        this.mCallback = mCallback;

        //Views with text
        mArticle = (TextView) view.findViewById(R.id.article_name);
        mLink = (TextView) view.findViewById(R.id.article_link);

        //Underline link-text
        mLink.setPaintFlags((mLink.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG));

        //To delete item
        view.findViewById(R.id.article_delete).setOnClickListener(this);
        //To click to open link
        view.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v instanceof ImageView){ //Remove-button
            mCallback.onRemove(getAdapterPosition());
        } else {
            mCallback.onArticleClick(getAdapterPosition()); //Whole item
        }
    }

    interface ViewHolderInterface {
        void onRemove(int position);
        void onArticleClick(int position);
    }
}