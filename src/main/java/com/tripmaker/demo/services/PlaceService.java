package com.tripmaker.demo.services;

import com.tripmaker.demo.data.Place;

public interface PlaceService {
    void savePlace(Place place);
    Place getPlaceById(Long id);
}
