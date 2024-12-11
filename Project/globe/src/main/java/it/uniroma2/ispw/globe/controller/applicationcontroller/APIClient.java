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



public class APIClient {

    private static final String BASE_URL = "https://nominatim.openstreetmap.org/";
    private static final Gson gson = new Gson();
    private final OkHttpClient httpClient;

    public APIClient() {
        this.httpClient = new OkHttpClient();
    }

    public List<JsonObject> getPlaces(String query, String type) throws IOException {
        String url = String.format("%ssearch?q=%s&format=json&addressdetails=1", BASE_URL, query.replace(" ", "+"));
        List<JsonObject> places = getPlace(url, type);
        return places;
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

        // Esegui la richiesta
        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }

            // Leggi il corpo della risposta
            String responseBody = response.body().string();

            // Parsea il JSON
            JsonArray results = gson.fromJson(responseBody, JsonArray.class);

            // Stampa i risultati
            for (int i = 0; i < results.size() && i < 10; i++) {
                JsonObject place = results.get(i).getAsJsonObject();;
                if (type.equals("id")) {
                    places.add(place);
                } else if (type.equals("administrative")) {
                    if (place.get("addresstype").getAsString().equals("city")||place.get("addresstype").getAsString().equals("town")||place.get("addresstype").getAsString().equals("village")) {
                        places.add(place);
//                        System.out.println("API CLIENT ATTR -> "+place.get("name").getAsString());
                    }
                } else {
                    if (!place.get("type").getAsString().equals(type)) {
                        places.add(place);
//                        System.out.println("API CLIENT ATTR -> "+place.get("name").getAsString());
                    }
                }
            }
        }
        return places;
    }
}
