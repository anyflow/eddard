package com.lge.stark.eddard.smp.session;

public enum NetworkType {
    WIRE(0, "wire"), WIFI(1, "wifi"), CELLULAR(2, "cellular");

    private int id;
    private String description;

    private NetworkType(int id, String description) {
        this.id = id;
        this.description = description;
    }

    public int id() {
        return id;
    }

    public String description() {
        return description;
    }

    public static NetworkType from(int id) {
        for (NetworkType item : values()) {
            if (item.id() == id) { return item; }
        }

        return null;
    }

    public static NetworkType from(String description) {
        for (NetworkType item : values()) {
            if (item.description().equalsIgnoreCase(description)) { return item; }
        }

        return null;
    }
}