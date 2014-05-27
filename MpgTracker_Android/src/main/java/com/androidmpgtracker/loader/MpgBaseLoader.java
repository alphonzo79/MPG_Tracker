package com.androidmpgtracker.loader;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

public abstract class MpgBaseLoader<D> extends AsyncTaskLoader<D> {
    protected D result;
    protected Context context;

    public MpgBaseLoader(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onStartLoading() {
        if(result != null)
            deliverResult(result);

        if(takeContentChanged() || result == null)
            forceLoad();
    }

    @Override
    protected void onReset() {
        stopLoading();

        if (result != null)
        {
            result=null;
        }
    }

    @Override
    public void deliverResult(D data) {
        result = data;
        if(isReset()){
            if(result != null){
                result = null;
                return;
            }
        }
        if(isStarted())
            super.deliverResult(result);
    }
}
