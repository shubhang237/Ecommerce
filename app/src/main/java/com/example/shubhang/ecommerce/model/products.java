package com.example.shubhang.ecommerce.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class products {

    @SerializedName("name")
    private String name;
    @SerializedName("description")
    private String description;
    @SerializedName("brand")
    private String brand;
    @SerializedName("price")
    private double price;
    @SerializedName("currency")
    private String currency;
    @SerializedName("in_stock")
    private boolean in_stock;
    @SerializedName("images")
    private ArrayList<String> images;

    public products(String name, String description, String brand, double price, String currency, boolean in_stock,ArrayList<String> images) {
        this.name = name;
        this.description = description;
        this.brand = brand;
        this.price = price;
        this.currency = currency;
        this.in_stock = in_stock;
        this.images = images;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getBrand() {
        return brand;
    }

    public double getPrice() {
        return price;
    }

    public String getCurrency() {
        return currency;
    }

    public boolean isIn_stock() {
        return in_stock;
    }

    public ArrayList<String> getImages() {
        return images;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public void setIn_stock(boolean in_stock) {
        this.in_stock = in_stock;
    }

    public void setImages(ArrayList<String> images) {
        this.images = images;
    }
}
