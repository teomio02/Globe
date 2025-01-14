package it.uniroma2.ispw.globe.controller.applicationcontroller;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;



public class NominatimAPIClient {

    private static final String BASE_URL = "https://nominatim.openstreetmap.org/";
    private static final Gson gson = new Gson();
    private final OkHttpClient httpClient;

    private static final String TYPE = "addresstype";

    public NominatimAPIClient() {
        this.httpClient = new OkHttpClient();
    }

    public List<JsonObject> getPlaces(String query, String type) throws IOException {
        String url = String.format("%ssearch?q=%s&format=json&addressdetails=1", BASE_URL, query.replace(" ", "+"));
        return getPlace(url, type);
    }

    public JsonObject getPlaceByID(String id) throws IOException {
        String url = String.format("%slookup?osm_ids=%s&format=json&addressdetails=1", BASE_URL, id);
        List<JsonObject> places = getPlace(url,"id");
        if (!places.isEmpty()) {
            return places.get(0);
        }
        return null;
    }

    public List<JsonObject> getPlace(String url, String type) throws IOException {
        List<JsonObject> places = new ArrayList<>();

        Request request = new Request.Builder().url(url).header("User-Agent", "Globe/1.0").build();

        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }

            String responseBody = response.body().string();

            JsonArray results = gson.fromJson(responseBody, JsonArray.class);

            for (int i = 0; i < results.size() && i < 10; i++) {
                JsonObject place = results.get(i).getAsJsonObject();
                if (type.equals("id")) {
                    places.add(place);
                } else if (type.equals("administrative")) {
                    if (place.get(TYPE).getAsString().equals("city")||place.get(TYPE).getAsString().equals("town")||place.get(TYPE).getAsString().equals("village")) {
                        places.add(place);
                    }
                } else {
                    if (!place.get("type").getAsString().equals(type)) {
                        places.add(place);
                    }
                }
            }
        }
        return places;
    }
}
