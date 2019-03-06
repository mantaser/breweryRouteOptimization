package com.company.model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Datasource {
    public static final String DB_NAME = "breweries.db";
    public static final String CONNECTION_STRING = "jdbc:sqlite:/Users/admin/Desktop/breweryRouteOptimization/src/resources/" +
            DB_NAME;

    public static final String TABLE_BEERS = "beers";
    public static final String COLUMN_BEERS_ID = "id";
    public static final String COLUMN_BEERS_BREWERY_ID = "brewery_id";
    public static final String COLUMN_BEERS_NAME = "name";
    public static final int INDEX_BEERS_ID = 1;
    public static final int INDEX_BEERS_BREWERY_ID = 2;
    public static final int INDEX_BEERS_NAME = 3;

    public static final String TABLE_BREWERIES = "breweries";
    public static final String COLUMN_BREWERIES_ID = "id";
    public static final String COLUMN_BREWERIES_NAME = "name";
    public static final String COLUMN_BREWERIES_ADDRESS = "address";
    public static final String COLUMN_BREWERIES_CITY = "city";
    public static final String COLUMN_BREWERIES_COUNTRY = "country";
    public static final int INDEX_BREWERIES_ID = 1;
    public static final int INDEX_BREWERIES_NAME = 2;
    public static final int INDEX_BREWERIES_ADDRESS = 3;
    public static final int INDEX_BREWERIES_CITY = 4;
    public static final int INDEX_BREWERIES_COUNTRY = 5;

    public static final String TABLE_GEOCODES = "geocodes";
    public static final String COLUMN_GEOCODES_ID = "id";
    public static final String COLUMN_GEOCODES_BREWERY_ID = "brewery_id";
    public static final String COLUMN_GEOCODES_LATITUDE = "latitude";
    public static final String COLUMN_GEOCODES_LONGITUDE = "longitude";
    public static final int INDEX_GEOCODES_ID = 1;
    public static final int INDEX_GEOCODES_BREWERY_ID = 2;
    public static final int INDEX_GEOCODES_LATITUDE = 3;
    public static final int INDEX_GEOCODES_LONGITUDE = 4;

    //select beers.name FROM beers INNER JOIN breweries ON beers.brewery_id = breweries.id WHERE breweries.id = 1;
    public static final String QUERY_BEERS_BY_BREWERY = "SELECT " + TABLE_BEERS + "." + COLUMN_BEERS_NAME + " FROM " +
            TABLE_BEERS + " INNER JOIN " + TABLE_BREWERIES + " ON " + TABLE_BEERS + "." + COLUMN_BEERS_BREWERY_ID +
            " = " + TABLE_BREWERIES + "." + COLUMN_BREWERIES_ID + " WHERE " + TABLE_BREWERIES + "." + COLUMN_BREWERIES_ID
            + " = ";

    // SELECT breweries.id, breweries.name, geocodes.latitude, geocodes.longitude FROM breweries INNER JOIN geocodes ON
    // breweries.id = geocodes.brewery_id
    public static final String QUERY_BREWERIES_ON_GEOCODES = "SELECT " + TABLE_BREWERIES + "." + COLUMN_BREWERIES_ID + ", "
            + TABLE_BREWERIES + "." + COLUMN_BREWERIES_NAME + ", " + TABLE_GEOCODES + "." + COLUMN_GEOCODES_LATITUDE + ", "
            + TABLE_GEOCODES + "." + COLUMN_GEOCODES_LONGITUDE +
            " FROM " + TABLE_BREWERIES + " INNER JOIN " + TABLE_GEOCODES + " ON " + TABLE_BREWERIES + "." +
            COLUMN_BREWERIES_ID + " = " + TABLE_GEOCODES + "." + COLUMN_GEOCODES_BREWERY_ID;

    private Connection conn;

    public boolean open() {
        try {
            conn = DriverManager.getConnection(CONNECTION_STRING);
            return true;
        } catch (SQLException e) {
            System.out.println("Couldn't connect to database: " + e.getMessage());
            return false;
        }
    }

    public void close() {
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            System.out.println("Couldn't close connection: " + e.getMessage());
        }
    }


    public List<Beer> queryBeers() {
        try (Statement statement = conn.createStatement();
             ResultSet results = statement.executeQuery("SELECT * FROM " + TABLE_BEERS)) {

            List<Beer> beers = new ArrayList<>();
            while (results.next()) {
                Beer beer = new Beer();
                beer.setId(results.getInt(INDEX_BEERS_ID));
                beer.setBreweryId(results.getInt(INDEX_BEERS_BREWERY_ID));
                beer.setName(results.getString(INDEX_BEERS_NAME));
                beers.add(beer);
            }
            return beers;
        } catch (SQLException e) {
            System.out.println("Query failed: " + e.getMessage());
            return null;
        }
    }

    public List<Brewery> queryBreweries() {
        try (Statement statement = conn.createStatement();
             ResultSet results = statement.executeQuery("SELECT * FROM " + TABLE_BREWERIES)) {

            List<Brewery> breweries = new ArrayList<>();
            while (results.next()) {
                Brewery brewery = new Brewery();
                brewery.setId(results.getInt(INDEX_BREWERIES_ID));
                brewery.setName(results.getString(INDEX_BREWERIES_NAME));
                breweries.add(brewery);
            }
            return breweries;
        } catch (SQLException e) {
            System.out.println("Query failed: " + e.getMessage());
            return null;
        }
    }

    public List<Brewery> queryBreweriesOnGeocodes() {
        try (Statement statement = conn.createStatement();
             ResultSet results = statement.executeQuery(QUERY_BREWERIES_ON_GEOCODES)) {

            List<Brewery> breweries = new ArrayList<>();
            while (results.next()) {
                Brewery brewery = new Brewery();
                brewery.setId(results.getInt(COLUMN_BREWERIES_ID));
                brewery.setName(results.getString(INDEX_BREWERIES_NAME));
                brewery.setLatitude(results.getDouble(COLUMN_GEOCODES_LATITUDE));
                brewery.setLongitude(results.getDouble(COLUMN_GEOCODES_LONGITUDE));
                breweries.add(brewery);
            }
            return breweries;
        } catch (SQLException e) {
            System.out.println("Query failed: " + e.getMessage());
            return null;
        }
    }
}