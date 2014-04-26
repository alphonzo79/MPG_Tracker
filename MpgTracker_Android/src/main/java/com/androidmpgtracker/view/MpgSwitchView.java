package com.androidmpgtracker.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.androidmpgtracker.R;

public class MpgSwitchView extends RelativeLayout {
    private String positiveText;
    private String negativeText;
    private int positiveColor;
    private int negativeColor;
    private int backgroundColor;

    private boolean selected;

    private TextView switchNo;
    private TextView switchYes;
    private View background;

    private Animation selectStart;
    private Animation selectFinish;
    private Animation deselectStart;
    private Animation deselectFinish;

    public MpgSwitchView(Context context) {
        super(context);
        init(null, 0);
    }

    public MpgSwitchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public MpgSwitchView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        // Load attributes
        final TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.MpgSwitchView, defStyle, 0);

        if(a.hasValue(R.styleable.MpgSwitchView_positiveText)) {
            positiveText = a.getString(R.styleable.MpgSwitchView_positiveText);
        } else {
            positiveText = getResources().getString(R.string.yes);
        }

        if(a.hasValue(R.styleable.MpgSwitchView_positiveText)) {
            negativeText = a.getString(R.styleable.MpgSwitchView_negativeText);
        } else {
            negativeText = getResources().getString(R.string.no);
        }

        positiveColor = a.getInteger(R.styleable.MpgSwitchView_positiveColor, getResources().getColor(R.color.btn_enabled));
        negativeColor = a.getInteger(R.styleable.MpgSwitchView_negativeColor, getResources().getColor(R.color.btn_disabled));
        backgroundColor = a.getInteger(R.styleable.MpgSwitchView_backgroundColor, getResources().getColor(R.color.black));

        selected = a.getBoolean(R.styleable.MpgSwitchView_initialSelected, true);

        a.recycle();

        inflate(getContext(), R.layout.control_switch, this);

        background = findViewById(R.id.background);
        background.setBackgroundColor(backgroundColor);

        switchNo = (TextView)findViewById(R.id.switch_no);
        switchYes = (TextView)findViewById(R.id.switch_yes);

        switchYes.setText(positiveText);
        switchYes.setBackgroundColor(positiveColor);

        switchNo.setText(negativeText);
        switchNo.setBackgroundColor(negativeColor);

        if(selected) {
            switchYes.setVisibility(View.VISIBLE);
            switchNo.setVisibility(View.INVISIBLE);
        } else {
            switchYes.setVisibility(View.INVISIBLE);
            switchNo.setVisibility(View.VISIBLE);
        }

        selectStart = AnimationUtils.loadAnimation(getContext(), R.anim.mpg_switch_negative_to_center);
        selectFinish = AnimationUtils.loadAnimation(getContext(), R.anim.mpg_switch_positive_from_center);

        deselectStart = AnimationUtils.loadAnimation(getContext(), R.anim.mpg_switch_positive_to_center);
        deselectFinish = AnimationUtils.loadAnimation(getContext(), R.anim.mpg_switch_negative_from_center);

        selectStart.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                switchYes.setVisibility(View.INVISIBLE);
                switchNo.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                switchYes.startAnimation(selectFinish);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        selectFinish.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                switchYes.setVisibility(View.VISIBLE);
                switchNo.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        deselectStart.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                switchYes.setVisibility(View.VISIBLE);
                switchNo.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                switchNo.startAnimation(deselectFinish);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        deselectFinish.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                switchNo.setVisibility(View.VISIBLE);
                switchYes.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    public void setPositiveText(String positiveText) {
        this.positiveText = positiveText;
        if(switchYes != null) {
            switchYes.setText(positiveText);
        }
    }

    public String getPositiveText() {
        return positiveText;
    }

    public void setNegativeText(String negativeText) {
        this.negativeText = negativeText;
        if(switchNo != null) {
            switchNo.setText(negativeText);
        }
    }

    public String getNegativeText() {
        return negativeText;
    }

    public int getPositiveColor() {
        return positiveColor;
    }

    public void setPositiveColor(int positiveColor) {
        this.positiveColor = positiveColor;
        if(switchYes != null) {
            switchYes.setBackgroundColor(positiveColor);
        }
    }

    public int getNegativeColor() {
        return negativeColor;
    }

    public void setNegativeColor(int negativeColor) {
        this.negativeColor = negativeColor;
        if(switchNo != null) {
            switchNo.setBackgroundColor(negativeColor);
        }
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        if(switchNo != null && switchYes != null && this.selected != selected) {
            if(selected) {
                switchNo.startAnimation(selectStart);
            } else {
                switchYes.startAnimation(deselectStart);
            }
        }
        this.selected = selected;
    }

    public void setSelectedNoAnimate(boolean selected) {
        if(switchNo != null && switchYes != null && this.selected != selected) {
            if(selected) {
                switchNo.setVisibility(View.INVISIBLE);
                switchYes.setVisibility(View.VISIBLE);
            } else {
                switchNo.setVisibility(View.VISIBLE);
                switchYes.setVisibility(View.INVISIBLE);
            }
        }
        this.selected = selected;
    }

    @Override
    public void setBackgroundColor(int color) {
        backgroundColor = color;
        if(background != null) {
            background.setBackgroundColor(color);
        }
    }
}
