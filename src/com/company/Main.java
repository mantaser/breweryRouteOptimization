package com.company;


import com.company.model.Beer;
import com.company.model.Brewery;
import com.company.model.Datasource;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


public class Main {
    private final static int AVG_EARTH_RADIUS = 6371; // in km
    private final static int DISTANCE_CAPACITY = 2000;
    private static int distanceTraveled = 0;
    private static List<String> breweriesVisited = new ArrayList<>();
    private static List<String> beersCollected = new ArrayList<>();

    public static void main(String[] args) {

        // Scanner methods

        // Setting home Lat Long
        double homeLati = 51.742503;
        double homeLong = 19.432956;

        // Accessing datasource
        Datasource datasource = new Datasource();
        if (!datasource.open()) {
            System.out.println("Can't open datasource");
            return;
        }

        List<Beer> beers = datasource.queryBeers();
        List<Brewery> breweries = datasource.queryBreweriesOnGeocodes();

        // Adding beers to Brewery List<Beer>
        for (Beer beer : beers) {
            for (Brewery brewery : breweries) {
                if (beer.getBreweryId() == brewery.getId()) {
                    brewery.addBeer(beer);
                }
            }
        }

        System.out.println("======== BREWERY SIZE: " + breweries.size());
        // Route algorithm
        boolean continueTravel = true;
        while (continueTravel) {
            List<Brewery> routeList = new ArrayList<>(breweries);
            double currentLati = homeLati;
            double currentLong = homeLong;
            //Start traveling
            Brewery nextBrewery = pickNearestBrewery(routeList, currentLati, currentLong);
            updateTraveledDistance(nextBrewery);
            addVisitedBrewery(nextBrewery);
            updateBeerList(nextBrewery.getBeerList());
//            currentLati = nextBrewery.getLatitude();
//            currentLati = nextBrewery.getLongitude();

            routeList.remove(nextBrewery);
            System.out.println("======== BREWERY SIZE: " + routeList.size());
            continueTravel = false;
        }

        System.out.println("Breweries visited: ");
        System.out.println(breweriesVisited);

        System.out.println("Beers collected: ");
        System.out.println(beersCollected);

        //  System.out.println(breweries.size());

//        for(Brewery brewery : breweries) {
//            System.out.println(brewery.getId() + " index: " + breweries.indexOf(brewery));
//        }


//        for(Brewery brewery : breweries) {
//            System.out.println("Brewery id: " + brewery.getId() + " Brewery name: " + brewery.getName() + ". BEER LIST:" + brewery.getBeerList());
//        }

        // for testing purposes
//        for(Brewery brewery : joinedBreweries) {
//            System.out.println("id: " + brewery.getId() + " name: " + brewery.getName() + " lat: " +  brewery.getLatitude() +
//                    " long: " + brewery.getLongitude());
//        }

//        for (Brewery brewery : joinedBreweries) {
//            System.out.print("brewery id " + brewery.getId() + " brewery name " + brewery.getName() + ", beers ");
//            for (Beer beer : brewery.getBeerList()) {
//                System.out.print(beer.getId() + ": " + beer.getName() + ", ");
//            }
//            System.out.println();
//        }
        datasource.close();
    }

//    private static void assignBeersToBreweries(List<Brewery> breweries, List<Beer> beers) {
//        int j = 0; // current beer
//        beers.sort(Comparator.comparing(Beer::getBreweryId));
//        for (Brewery brewery : breweries) {
//            for (int i = j; i < beers.size(); i++) {
////                if (brewery.getId() < beers.get(i).getBreweryId()) {
////                    break;
////                }
//                if (beers.get(i).getBreweryId() == brewery.getId()) {
//                    brewery.addBeer(beers.get(i));
//                    j++;
//                } else {
//                    System.out.println("brewery id " + brewery.getId() + ", beer id " + beers.get(i).getId() + ", j = " + j);
//                    break;
//                }
//            }
//        }
//    }

    private static List<Brewery> calculateDistanceFromCurrentPoint(List<Brewery> breweries, double currentLat, double currentLong) {
        for (Brewery brewery : breweries) {
            int distance = calculateDistance(currentLat, currentLong, brewery.getLatitude(), brewery.getLongitude());
            brewery.setDistance(distance);
        }
        return breweries;
    }

    private static int calculateDistance(double startLatitude, double startLongitude,
                                         double endLatitude, double endLongitude) {
        // Haversine formula
        double latDistance = Math.toRadians(startLatitude - endLatitude);
        double longDistance = Math.toRadians(startLongitude - endLongitude);

        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(startLatitude)) * Math.cos(Math.toRadians(endLatitude))
                * Math.sin(longDistance / 2) * Math.sin(longDistance / 2);

        double angle = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return (int) (Math.round(AVG_EARTH_RADIUS * angle));
    }

    private static boolean isEnoughFuel(double currentLat, double currentLong, double nextLat, double nextLong,
                                        double homeLat, double homeLong) {
        int currentToNext = calculateDistance(currentLat, currentLong, nextLat, nextLong);
        int nextToHome = calculateDistance(nextLat, nextLong, homeLat, homeLong);
        int plannedDistance = currentToNext + nextToHome;
        return (distanceTraveled + plannedDistance) <= DISTANCE_CAPACITY;
    }

    private static Brewery pickNearestBrewery(List<Brewery> breweries, Double currentLat, Double currentLong) {
        List<Brewery> breweriesDistance = calculateDistanceFromCurrentPoint(breweries, currentLat, currentLong);
        breweriesDistance.sort(Comparator.comparing(Brewery::getDistance));
        return breweries.get(0);
    }

    public static boolean addVisitedBrewery(Brewery brewery) {
        if (brewery != null) {
            breweriesVisited.add("-> [ID: " + brewery.getId() + "] " + brewery.getName() +" "+ brewery.getLatitude() + ", "
            + brewery.getLongitude() + " DISTANCE: " + brewery.getDistance());
            return true;
        }
        System.out.println("Failed to addVisitedBrewery()");
        return false;
    }

    public static boolean updateBeerList(List<Beer> breweryBeerList) {
        if(breweryBeerList != null) {
            beersCollected.add("-> " + breweryBeerList);
            return true;
        }
        System.out.println("Failed to updateBeerList()");
        return false;
    }

    private static boolean updateTraveledDistance(Brewery brewery) {
        if(brewery != null) {
            distanceTraveled += brewery.getDistance();
            System.out.println("Traveled to: " + brewery.getName());
            return true;
        }
        System.out.println("Something went wrong updateTraveledDistance()");
        return false;
    }
}
