package com.androidmpgtracker.activity;

import com.androidmpgtracker.data.DataTransferProvider;

/**
 * Created by joe on 12/31/14.
 */
public class DashboardActivity extends DashboardActivityBuildVar {
    @Override
    protected String getCarsUri() {
        return DataTransferProvider.CARS_PAID_URI;
    }

    @Override
    protected String getFillupsUri() {
        return DataTransferProvider.LOGS_PAID_URI;
    }
}
