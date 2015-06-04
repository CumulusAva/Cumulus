package com.github.cumulusava.android.cumulus.customViews;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

/**
 * Created by Magnus on 2015-05-01
 * Observes the ScrollView to notice scroll changes
 * Sends scroll up/down to activity to be able to show and hide toolbar
 */
public class ObservableScrollView extends ScrollView {
    private OnScrollChangedCallback mOnScrollChangedListener;

    public ObservableScrollView(Context context) {
        super(context);
    }

    public ObservableScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ObservableScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (mOnScrollChangedListener == null) return;
        int deltaY = t - oldt; //Scrolling up if positive and opposite for negative

        if (deltaY < 0) mOnScrollChangedListener.onScrollUp();
        else mOnScrollChangedListener.onScrollDown(t);
    }

    public void setOnScrollChangedListener(final OnScrollChangedCallback onScrollChangedCallback){
        mOnScrollChangedListener = onScrollChangedCallback;
    }

    public interface OnScrollChangedCallback{
        void onScrollUp();
        void onScrollDown(int pos);
    }
}