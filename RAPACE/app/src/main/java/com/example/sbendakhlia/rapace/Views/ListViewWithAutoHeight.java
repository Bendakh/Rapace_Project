package com.example.sbendakhlia.rapace.Views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ListView;


// Source https://stackoverflow.com/questions/11295080/android-wrap-content-is-not-working-with-listview
public class ListViewWithAutoHeight extends ListView {

    public ListViewWithAutoHeight(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ListViewWithAutoHeight(Context context) {
        super(context);
    }

    public ListViewWithAutoHeight(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = View.MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                View.MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }

}