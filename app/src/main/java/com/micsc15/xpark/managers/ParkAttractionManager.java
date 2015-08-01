package com.micsc15.xpark.managers;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.micsc15.xpark.models.ParkArea;
import com.micsc15.xpark.models.ParkAttraction;
import com.micsc15.xpark.models.enums.AttractionType;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.UUID;

public class ParkAttractionManager {

    // -------------- Objects, Variables -------------- //

    public static ArrayList<ParkArea> ParkAreas;

    // --------------- Public Methods ----------------- //

    public static ArrayList<ParkAttraction> getParkAttractions(Context context) {
        ArrayList<ParkAttraction> pins = new ArrayList<ParkAttraction>();

        for (ParkArea area : GetAttractionsPin(context)) {
            if(area.Attractions != null){
                pins.addAll(area.Attractions);
            }
        }

        return pins;
    }

    public static ArrayList<ParkAttraction> getParkAttractions(Context context, int type) {
        ArrayList<ParkAttraction> pins = new ArrayList<ParkAttraction>();

        for (ParkArea area : GetAttractionsPin(context)) {
            if(area.Attractions != null){
                for (ParkAttraction attraction: area.Attractions) {
                    if(attraction.AttractionType == type)
                        pins.add(attraction);
                }
            }
        }

        return pins;
    }

    public static ParkAttraction getParkAttraction(UUID attractionID){
        ParkAttraction attraction = null;

        for (ParkArea area : ParkAreas) {
            if(area.Attractions != null){
                for (ParkAttraction attr: area.Attractions) {
                    if(attr.AttractionID == attractionID){
                        attraction = attr;
                    }
                }
            }
        }

        return attraction;
    }

    // --------------- Private Methods ---------------- //

    private static ArrayList<ParkArea> GetAttractionsPin(Context context) {
        if (ParkAreas != null) {
            return ParkAreas;
        } else {
            ParkAreas = new ArrayList<ParkArea>();
            String json = null;
            Gson gson = new Gson();

            try {
                InputStream is = context.getAssets().open("map.json");

                int size = is.available();
                byte[] buffer = new byte[size];

                is.read(buffer);
                is.close();

                json = new String(buffer, "UTF-8");
                JsonParser parser = new JsonParser();
                JsonArray jArray = parser.parse(json).getAsJsonArray();

                for (JsonElement jsonObj : jArray) {
                    ParkArea area = gson.fromJson(jsonObj, ParkArea.class);
                    ParkAreas.add(area);
                }
                return ParkAreas;
            } catch (IOException ex) {
                ex.printStackTrace();
                return null;
            }
        }
    }
}
