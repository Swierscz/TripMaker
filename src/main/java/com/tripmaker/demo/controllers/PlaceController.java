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

    //Temporary disabled

//   @PostMapping("createPlace")
//   public ResponseEntity<Place> createPlace(@RequestBody Place place){
//       placeService.savePlace(place);
//       return new ResponseEntity<Place>(place, HttpStatus.CREATED);
//    }

    @GetMapping("getPlace/{id}")
    public ResponseEntity<Place> getPlace(@PathVariable("id") Long id){
       return new ResponseEntity<Place>( placeService.getPlaceById(id), HttpStatus.OK);
    }

}
