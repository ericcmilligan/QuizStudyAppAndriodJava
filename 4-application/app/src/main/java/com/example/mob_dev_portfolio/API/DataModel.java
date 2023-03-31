package com.example.mob_dev_portfolio.API;

public class DataModel {
    private String imageUrl;

    public DataModel(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
