package com.example.mob_dev_portfolio.API;

/**
 * This class represents a data model for saving the Giphy API image url response into.
 */
public class DataModel {
    // Private field to store the image URL
    private String imageUrl;

    /**
     * Constructor to initialize a DataModel object with the image URL as a property.
     * @param imageUrl
     */
    public DataModel(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    /**
     * Getter method to retrieve the image URL.
     *
     * @return the image URL as a string from the data model
     */
    public String getImageUrl() {
        return imageUrl;
    }

    /**
     * Setter method to set the image URL.
     *
     * @param the image URL to set
     */
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
