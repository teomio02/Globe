package it.uniroma2.ispw.globe.util;

public class ModelFactory {

//    public Account createAccount(CredentialsBean credentials) {
//        if (credentials.getType().equals(AGENCY)) {
//            Agency agency = new Agency();
//            agency.setUsername(credentials.getUsername());
//            agency.setPassword(credentials.getPassword());
//            agency.setType(credentials.getType());
//            agency.setProposals(new ArrayList<>());
//            return agency;
//        } else {
//            User user = new User();
//            user.setUsername(credentials.getUsername());
//            user.setPassword(credentials.getPassword());
//            user.setType(credentials.getType());
//            user.setItineraries(new ArrayList<>());
//            user.setProposals(new ArrayList<>());
//            return user;
//        }
//    }

//    public Itinerary createItinerary(ItineraryBean itineraryBean) {
//        Itinerary itinerary = new Itinerary();
//        List<Day> days = new ArrayList<>();
//
//        Day day0 = createDay(0,itineraryBean.getCities(),itineraryBean.getAttractions());
//        days.add(day0);
//        for (int i=0; i<=itineraryBean.getDuration(); i++) {
//            Day day = createDay(i,new ArrayList<>(),new ArrayList<>());
//            days.add(day);
//        }
//
//        itinerary.setItineraryID("1");
//        itinerary.setName(itineraryBean.getName());
//        itinerary.setDescription(itineraryBean.getDescription());
//        itinerary.setDaysNumber(itineraryBean.getDuration());
//        itinerary.setDays(days);
//        //itinerary.setType(itineraryBean.getType());
//        return itinerary;
//    }

//    public Day createDay(int dayNum, List<String> citiesID, List<String> attractionsID) {
//        Day day = new Day();
//        List<City> cities = new ArrayList<>();
//        List<Attraction> attractions = new ArrayList<>();
//
//        for (String i : citiesID) {
//            City city = createCity(i);
//            cities.add(city);
//        }
//
//        for (String i : attractionsID) {
//            Attraction attraction = createAttraction(i);
//            attractions.add(attraction);
//        }
//
//        day.setDayNum(dayNum);
//        day.setCities(cities);
//        day.setAttractions(attractions);
//
//        return day;
//
//    }
//
//    public City createCity(String cityId) {
//        JsonObject jsonCity;
//        try {
//            jsonCity= new APIClient().getPlaceByID(cityId);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//        return new PlaceAdapter(jsonCity);
//    }
//
//    public Attraction createAttraction(String attractionId) {
//        JsonObject jsonAttraction;
//        try {
//            jsonAttraction = new APIClient().getPlaceByID(attractionId);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//        return new PlaceAdapter(jsonAttraction);
//    }
}
