package com.tripmaker.demo.controllers;

import com.tripmaker.demo.data.Place;
import com.tripmaker.demo.services.PlaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/place")
public class PlaceController {

    @Autowired
    PlaceService placeService;


   @PostMapping("createPlace")
   public ResponseEntity<Place> createPlace(@RequestBody Place place){
        placeService.savePlace(place);
       return new ResponseEntity<Place>(place, HttpStatus.CREATED);
    }

    @PostMapping("deletePlace")
    public ResponseEntity deletePlace(@RequestBody String name){
       placeService.deletePlace(name);
       return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("findPlace")
    public ResponseEntity<Place> findPlace(@RequestHeader(value = "name") String name){
       return new ResponseEntity<Place>(placeService.findPlaceByName(name), HttpStatus.OK);
    }

}
