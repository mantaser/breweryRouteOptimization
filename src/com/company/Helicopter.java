package com.company;

import com.company.model.Beer;
import com.company.model.Brewery;
import com.company.model.Datasource;

import java.util.LinkedList;
import java.util.List;

public class Helicopter {
    private HelperClass helper = new HelperClass();
    private Datasource datasource = new Datasource();

    private String name;
    private int distanceCapacity;
    private double homeLat = 51.742503;
    private double homeLong = 19.432956;

    private List<Brewery> breweries = new LinkedList<>();
    private List<Beer> beers = new LinkedList<>();

    public Helicopter(String name, int distanceCapacity) {
        this.name = name;
        this.distanceCapacity = distanceCapacity;
//        this.homeLat = homeLat;
//        this.homeLong = homeLong;
    }

    public String getName() {
        return name;
    }

    public int getDistanceCapacity() {
        return distanceCapacity;
    }

    public double getHomeLat() {
        return homeLat;
    }

    public double getHomeLong() {
        return homeLong;
    }

    private boolean setConnection() {
        // Accessing datasource
        Datasource datasource = new Datasource();
        if (!datasource.open()) {
            System.out.println("Can't open datasource");
            return false;
        }

        return true;
    }

    private void collectData(){
        List<Beer> beers = datasource.queryBeers();
        this.beers.addAll(beers);
        List<Brewery> breweries = datasource.queryBreweriesOnGeocodes();
        this.breweries.addAll(breweries);
        datasource.close();
    }

    private void pickNextPoint(){

    }

    private void addBeerToBrewery(){
        for (Beer beer : beers) {
            for (Brewery brewery : breweries) {
                if (beer.getBreweryId() == brewery.getId()) {
                    brewery.addBeer(beer);
                }
            }
        }
    }

    private boolean canTravelTo(double startLat, double startLong, double endLat, double endLong){
        int distanceBetween = helper.calculateDistance(startLat,startLong,endLat, endLong);
        int distanceToHome = helper.calculateDistance(endLat,endLong, homeLat, homeLong);
        return distanceBetween + distanceToHome <= distanceCapacity;
    }



}
