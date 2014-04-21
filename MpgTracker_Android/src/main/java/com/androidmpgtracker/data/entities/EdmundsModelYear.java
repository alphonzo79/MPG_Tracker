package com.androidmpgtracker.data.entities;

import java.util.List;

public class EdmundsModelYear extends MpgApiEntity {
    static final long serialVersionUID = -5735530497791388168L;

    private Integer id;
    private Integer year;
    private List<EdmundsStyle> styles;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public List<EdmundsStyle> getStyles() {
        return styles;
    }

    public void setStyles(List<EdmundsStyle> styles) {
        this.styles = styles;
    }
}
