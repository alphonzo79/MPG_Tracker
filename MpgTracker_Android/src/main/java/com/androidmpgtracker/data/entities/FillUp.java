package com.androidmpgtracker.data.entities;

public class FillUp extends MpgApiEntity {
    static final long serialVersionUID = -3883041330017649176L;

    private Long carId;
    private Long date;
    private Float miles;
    private Float gallons;
    private Float pricePerGallon;
    private Float totalCost;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Long getCarId() {
        return carId;
    }

    public void setCarId(Long carId) {
        this.carId = carId;
    }

    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }

    public Float getMiles() {
        return miles;
    }

    public void setMiles(Float miles) {
        this.miles = miles;
    }

    public Float getGallons() {
        return gallons;
    }

    public void setGallons(Float gallons) {
        this.gallons = gallons;
    }

    public Float getPricePerGallon() {
        return pricePerGallon;
    }

    public void setPricePerGallon(Float pricePerGallon) {
        this.pricePerGallon = pricePerGallon;
    }

    public Float getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(Float totalCost) {
        this.totalCost = totalCost;
    }
}
