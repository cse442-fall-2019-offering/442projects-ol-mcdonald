package com.cse442.olmcdonald;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.io.Serializable;
import java.util.Map;

import static com.cse442.olmcdonald.ConstantClass.DB_AMOUNT;
import static com.cse442.olmcdonald.ConstantClass.DB_DELIVERY;
import static com.cse442.olmcdonald.ConstantClass.DB_HARVEST;
import static com.cse442.olmcdonald.ConstantClass.DB_ID;
import static com.cse442.olmcdonald.ConstantClass.DB_IMG;
import static com.cse442.olmcdonald.ConstantClass.DB_NAME;
import static com.cse442.olmcdonald.ConstantClass.DB_PRICE;
import static com.cse442.olmcdonald.ConstantClass.DB_SELLER;
import static com.cse442.olmcdonald.ConstantClass.DB_SPECIES;
import static com.cse442.olmcdonald.ConstantClass.DB_TOTAL;
import static com.cse442.olmcdonald.ConstantClass.DB_ZIPCODE;

/**
 * Item Class to hold information about the Item
 */
public class Item implements Parcelable {
    private String id;
    private String name;
    private String seller;
    private String species;
    private String harvest_date;
    private Bitmap img_data;
    private int zipcode;
    private int amount;
    private float price;
    private int delivery_distance;
    private int total;

    public Item(QueryDocumentSnapshot d){
        Map<String,Object> map_data = d.getData();
        this.price = Float.valueOf(map_data.get(DB_PRICE).toString());
        this.amount = Integer.valueOf(map_data.get(DB_AMOUNT).toString());
        this.delivery_distance = Integer.valueOf(map_data.get(DB_DELIVERY).toString());
        this.zipcode = Integer.valueOf(map_data.get(DB_ZIPCODE).toString());
        this.total = Integer.valueOf(map_data.get(DB_TOTAL).toString());
        this.harvest_date = map_data.get(DB_HARVEST).toString();
        this.img_data = itemManager.base64ToBitmap(map_data.get(DB_IMG).toString());
        this.name = map_data.get(DB_NAME).toString();
        this.species = map_data.get(DB_SPECIES).toString();
        this.seller = map_data.get(DB_SELLER).toString();
        this.id = d.getId();
    }

    public Item(Parcel parcel) {
        String[] data = new String[11];
        parcel.readStringArray(data);
        this.id = data[10];
        this.price = Float.valueOf(data[9]);
        this.amount = Integer.valueOf(data[8]);
        this.delivery_distance = Integer.valueOf(data[7]);
        this.zipcode = Integer.valueOf(data[6]);
        this.total = Integer.valueOf(data[5]);
        this.harvest_date = data[4];
        this.img_data = itemManager.base64ToBitmap(data[3]);
        this.name = data[2];
        this.species =data[1];
        this.seller = data[0];
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public Bitmap getImg_data() {
        return img_data;
    }

    public void setImg_data(Bitmap img_data) {
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeStringArray(new String[]{this.getSeller(),getSpecies(),getName(),
                itemManager.bitmapToBase64(getImg_data()),getHarvest_date(), String.valueOf(getTotal()),
                String.valueOf(getZipcode()), String.valueOf(getDelivery_distance()), String.valueOf(getAmount()), String.valueOf(getPrice()),getId()});
    }


    public static final Parcelable.Creator<Item> CREATOR = new Creator<Item>(){

        @Override
        public Item createFromParcel(Parcel parcel) {
            return new Item(parcel);
        }

        @Override
        public Item[] newArray(int i) {
            return new Item[i];
        }
    };
}