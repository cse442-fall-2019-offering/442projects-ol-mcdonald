package com.cse442.olmcdonald;

import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.io.Serializable;
import java.util.Map;

/**
 * Item Class to hold information about the Item
 */
public class Item implements Serializable {
    private String name;
    private String seller;
    private String species;
    private String harvest_date;
    private String img_data;
    private int zipcode;
    private int amount;
    private float price;
    private int delivery_distance;
    private int total;

    public Item(QueryDocumentSnapshot d){
        Map<String,Object> map_data = d.getData();
        this.price = Float.valueOf(map_data.get("price").toString());
        this.amount = Integer.valueOf(map_data.get("amount").toString());
        this.delivery_distance = Integer.valueOf(map_data.get("delivery").toString());
        this.zipcode = Integer.valueOf(map_data.get("zipcode").toString());
        this.total = Integer.valueOf(map_data.get("total").toString());
        this.harvest_date = map_data.get("harvest").toString();
        this.img_data = map_data.get("img").toString();
        this.name = map_data.get("name").toString();
        this.species = map_data.get("species").toString();
        this.seller = map_data.get("seller").toString();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSeller() {
        return seller;
    }

    public void setSeller(String seller) {
        this.seller = seller;
    }

    public String getSpecies() {
        return species;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    public String getHarvest_date() {
        return harvest_date;
    }

    public void setHarvest_date(String harvest_date) {
        this.harvest_date = harvest_date;
    }

    public String getImg_data() {
        return img_data;
    }

    public void setImg_data(String img_data) {
        this.img_data = img_data;
    }

    public int getZipcode() {
        return zipcode;
    }

    public void setZipcode(int zipcode) {
        this.zipcode = zipcode;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public int getDelivery_distance() {
        return delivery_distance;
    }

    public void setDelivery_distance(int delivery_distance) {
        this.delivery_distance = delivery_distance;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}