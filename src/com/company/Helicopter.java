package com.company;

import com.company.model.Beer;
import com.company.model.Brewery;
import com.company.model.Datasource;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Helicopter {
    private HelperClass helper = new HelperClass();

    private String name;
    private int distanceCapacity = 2000;
    private double homeLat;
    private double homeLong;

    private int traveledDistance = 0;
    private List<Brewery> breweries = new ArrayList<>();
    private List<Beer> beers = new ArrayList<>();
    private List<Brewery> nearestBreweries = new ArrayList<>();
    private List<Brewery> breweriesVisited = new ArrayList<>();
    private List<Beer> beersCollected = new ArrayList<>();

    public Helicopter(String name, Double homeLat, Double homeLong) {
        this.name = name;
        this.homeLat = homeLat;
        this.homeLong = homeLong;
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

    public void collectData() {
        Datasource datasource = new Datasource();
        if (!datasource.open()) {
            System.out.println("Can't open datasource");
            return;
        }
        List<Beer> beers = datasource.queryBeers();
        this.beers.addAll(beers);
        List<Brewery> breweries = datasource.queryBreweriesOnGeocodes();
        this.breweries.addAll(breweries);
        datasource.close();

    }

    public void findNearestBreweries() {
        for (Brewery brewery : breweries) {
            int distance = helper.calculateDistance(homeLat, homeLong, brewery.getLatitude(), brewery.getLongitude());
            if (distance <= distanceCapacity / 2) {
                brewery.setDistance(distance);
                nearestBreweries.add(brewery);
            }
        }
        nearestBreweries.sort(Comparator.comparing(Brewery::getDistance));
    }

    public void addBeersToAssortment() {
        for (Brewery brewery : nearestBreweries) {
            for (Beer beer : beers) {
                if (beer.getBreweryId() == brewery.getId()) {
                    brewery.addBeer(beer);
                }
            }
        }
    }


    public boolean canTravel(double startLat, double startLong, double endLat, double endLong) {
        int distanceBetween = helper.calculateDistance(startLat, startLong, endLat, endLong);
        int distanceToHome = helper.calculateDistance(endLat, endLong, homeLat, homeLong);
        return distanceBetween + distanceToHome <= distanceCapacity-traveledDistance;
    }

    public void travel(int distance, Brewery brewery) {
        traveledDistance += distance;
        breweriesVisited.add(brewery);
        beersCollected.addAll(brewery.getBeerList());
    }

    public void printTravelItinerary() {
        System.out.println("Found " + breweriesVisited.size() + " beer factories:");
        System.out.println("    -> HOME: " + homeLat + " " + homeLong);
        for (Brewery brewery : breweriesVisited) {
            System.out.println("    -> " + "[" + brewery.getId() + "] " + brewery.getName() + " " + brewery.getLatitude() +
                    " " + brewery.getLongitude() + " distance " + brewery.getDistance()+ "km");
        }
        Brewery lastBrewery = breweriesVisited.get(breweriesVisited.size()-1);
        int lastDistance = helper.calculateDistance(homeLat,homeLong,lastBrewery.getLatitude(),lastBrewery.getLongitude());
        traveledDistance += lastDistance;

        System.out.println("    <- HOME: " + homeLat + " " + homeLong + " distance " + lastDistance+"km");

        System.out.println();
        System.out.println("Total distance travelled: " + traveledDistance);

        System.out.println();
        System.out.println("Collected " + beersCollected.size() + " beer types:");
        for(Beer beer : beersCollected) {
            System.out.println("    ->"+beer.getName());
        }
    }


    public void startTravel() {
        collectData();
        findNearestBreweries();
        addBeersToAssortment();
        int i = 0;
        double currentLat = homeLat;
        double currentLong = homeLong;
        Brewery nextBrewery = nearestBreweries.get(i);
        double nextLat = nextBrewery.getLatitude();
        double nextLong = nextBrewery.getLongitude();
        int distance = helper.calculateDistance(currentLat,currentLong,nextLat,nextLong);

        while(canTravel(currentLat,currentLong,nextLat,nextLong)){
            travel(distance, nextBrewery);
            currentLat = nextLat;
            currentLong = nextLong;

            i++;
            nextBrewery = nearestBreweries.get(i);
            nextLat = nextBrewery.getLatitude();
            nextLong = nextBrewery.getLongitude();
            distance = helper.calculateDistance(currentLat,currentLong,nextLat,nextLong);
            nextBrewery.setDistance(distance);
  }
        printTravelItinerary();



    }
}
