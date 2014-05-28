package com.androidmpgtracker.data.entities;

public class CommunityMpg extends MpgApiEntity {
    private Float mpg;
    private Integer count;

    public Float getMpg() {
        return mpg;
    }

    public void setMpg(Float mpg) {
        this.mpg = mpg;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
