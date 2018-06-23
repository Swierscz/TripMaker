package com.tripmaker.demo.services;

import com.tripmaker.demo.data.Place;
import com.tripmaker.demo.data.PlaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PlaceServiceImpl implements PlaceService {

    @Autowired
    PlaceRepository placeRepository;

    @Autowired
    TripGroupService tripGroupService;

    @Override
    public void savePlace(Place place) {
        placeRepository.save(place);
    }

    @Override
    public Place getPlaceById(Long id) {
        return placeRepository.findById(id).get();
    }
}
