package com.tripmaker.demo.Services;

import com.tripmaker.demo.Data.Place;
import com.tripmaker.demo.Data.TripGroup;

public interface PlaceService {
    void savePlace(Place place);
    void deletePlace(String name);
    Place findPlaceByName(String name);
}
