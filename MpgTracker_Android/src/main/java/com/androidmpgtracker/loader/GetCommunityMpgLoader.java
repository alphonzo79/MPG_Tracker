package com.androidmpgtracker.loader;

import android.content.Context;

import com.androidmpgtracker.data.Method;
import com.androidmpgtracker.data.MpgApiRequest;
import com.androidmpgtracker.data.NetworkCallExecutor;
import com.androidmpgtracker.data.entities.AverageMpg;
import com.google.gson.Gson;

public class GetCommunityMpgLoader extends MpgBaseLoader<AverageMpg> {
    static final long serialVersionUID = 2518321080928711926L;
    private Long vehicleTrimId;

    public GetCommunityMpgLoader(Context context) {
        super(context);
    }

    @Override
    public AverageMpg loadInBackground() {
        if(vehicleTrimId != null) {
            MpgApiRequest request = new MpgApiRequest(context, Method.GET_COMMUNITY_MPG);
            request.addParam("car_id", String.valueOf(vehicleTrimId));

            request = new NetworkCallExecutor().sendMpgGetAndWait(request);
            if(request.getResponse() != null) {
                result = new Gson().fromJson(request.getResponse(), AverageMpg.class);
            }
        }

        return result;
    }

    public void setVehicleTrimId(Long vehicleTrimId) {
        this.vehicleTrimId = vehicleTrimId;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }
}
