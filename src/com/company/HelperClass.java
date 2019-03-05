package com.company;


public class HelperClass {
    private final static int AVG_EARTH_RADIUS = 6371; // in km

    public int calculateDistance(double startLatitude, double startLongitude,
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
}
