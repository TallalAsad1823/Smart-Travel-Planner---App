package com.example.smarttravelplanner;

public class PlaceModel {
    private String name;
    private String description;
    private String wikiUrl;

    // Constructor
    public PlaceModel(String name, String description, String wikiUrl) {
        this.name = name;
        this.description = description;
        this.wikiUrl = wikiUrl;
    }

    // Getters (Requirement 5.1 - Encapsulation)
    public String getName() { return name; }
    public String getDescription() { return description; }
    public String getWikiUrl() { return wikiUrl; }
}