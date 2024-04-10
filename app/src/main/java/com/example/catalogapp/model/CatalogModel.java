package com.example.catalogapp.model;

public class CatalogModel {
    private int id;
    private String name;
    private String desc;
    private int price;
    private String color;
    private String image;

    public CatalogModel() {
    }

    public CatalogModel(int id, String name, String desc, int price, String color, String image) {
        this.id = id;
        this.name = name;
        this.desc = desc;
        this.price = price;
        this.color = color;
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
