package com.qadr.bankapi.model;

import java.util.HashMap;
import java.util.Map;

public class Continent {
    public static final Map<String, String> continent;
    static {
        continent = new HashMap<>();
        continent.put("af", "AFRICA");
        continent.put("as", "ASIA");
        continent.put("eu", "EUROPE");
        continent.put("na", "NORTH AMERICA");
        continent.put("sa", "SOUTH_AMERICA");
        continent.put("au", "AUSTRALIA");
        continent.put("at", "ANTARCTICA");
    }

}
