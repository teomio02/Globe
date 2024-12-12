package it.uniroma2.ispw.globe.util.adapter;

import com.google.gson.JsonObject;
import it.uniroma2.ispw.globe.model.Attraction;
import it.uniroma2.ispw.globe.model.City;

public class PlaceAdapter implements Attraction, City {
    private JsonObject place;

    public PlaceAdapter(JsonObject place) {
        this.place = place;
    }

    public String getPlaceID() {
        String placeID= (place.get("osm_type").getAsString()).charAt(0)+place.get("osm_id").getAsString();
        return placeID;
    }

    public String getName() {
        return place.get("name").getAsString();
    }

    public String getCountry() {
        JsonObject address = place.getAsJsonObject("address");
        try {
            return address.get("country").getAsString();
        } catch (NullPointerException e) {
            return "UNKOWN";
        }
    }

    public String getCity() {
        try {
            JsonObject address = place.getAsJsonObject("address");
            return address.get("city").getAsString();
        } catch (NullPointerException e) {
            return "UNKOWN";
        }
    }

    public String getAddress() {
        JsonObject address = place.getAsJsonObject("address");
        try {
            return address.get("road").getAsString();
        } catch (NullPointerException e) {
            return "UNKOWN";
        }
    }

    public double getLatitude() {
        return place.get("lat").getAsDouble();
    }

    public double getLongitude() {
        return place.get("lon").getAsDouble();
    }
}
