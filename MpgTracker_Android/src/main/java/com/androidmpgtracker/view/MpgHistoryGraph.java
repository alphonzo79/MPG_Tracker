package com.androidmpgtracker.view;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;

import com.androidmpgtracker.R;
import com.androidmpgtracker.data.entities.FillUp;

import java.util.ArrayList;
import java.util.List;

public class MpgHistoryGraph extends LinearLayout {
    private List<FillUp> fillUps;
    private Context context;
    private float maxMpg = 0;
    private List<Float> mpgList;

    public MpgHistoryGraph(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public MpgHistoryGraph(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    private void init() {
        if(context != null && fillUps != null) {
            setOrientation(HORIZONTAL);
            setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL);

            int height = getHeight();

            removeAllViews();

            Resources r = getResources();
            int twentyFive = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 25, r.getDisplayMetrics());
            int three = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 3, r.getDisplayMetrics());

            for(Float mpg : mpgList) {
                View bar = new View(context);
                bar.setBackgroundResource(R.drawable.mpg_history_graph_bar);

                float ratio = (mpg / maxMpg);
                Float barHeight = height * ratio;
                LinearLayout.LayoutParams params = null;
                if(mpgList.size() > 5) {
                    params = new LayoutParams(0, barHeight.intValue(), 1.0f);
                } else {
                    params = new LayoutParams(twentyFive, barHeight.intValue());
                }
                params.rightMargin = three;
                params.leftMargin = three;

                bar.setLayoutParams(params);

                addView(bar);
            }
        }
    }

    public void setFillUps(List<FillUp> fillups) {
        this.fillUps = fillups;
        maxMpg = 0;
        mpgList = new ArrayList<Float>(this.fillUps.size());
        for(FillUp fillup : fillups) {
            if(fillup.getGallons() != null && fillup.getMiles() != null) {
                float mpg = fillup.getMiles() / fillup.getGallons();
                mpgList.add(mpg);
                if(mpg > maxMpg) {
                    maxMpg = mpg;
                }
            }
        }
        init();
    }
}
