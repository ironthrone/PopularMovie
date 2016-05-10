package com.example.popularmovies.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by Administrator on 2016/5/10.
 */
public class TwoToThreeImageView extends ImageView {
    public TwoToThreeImageView(Context context) {
        super(context);
    }

    public TwoToThreeImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TwoToThreeImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(width *3/2,MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
