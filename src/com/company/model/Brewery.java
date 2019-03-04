package com.company.model;

import java.util.ArrayList;
import java.util.List;

public class Brewery {

    private int id;
    private String name;
    private double latitude;
    private double longitude;
    private int distance;
    private List<Beer> beerList = new ArrayList<>();



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

    public List<Beer> getBeerList() {
        return beerList;
    }

    public void setBeerList(List<Beer> beerList) {
        this.beerList = beerList;
    }

    public void addBeer(Beer beer) {
        this.beerList.add(beer);
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public int getDistance() {
        return distance;
    }

    @Override
    public String toString() {
        return "Brewery{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", beerList=" + beerList +
                '}';
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }




}