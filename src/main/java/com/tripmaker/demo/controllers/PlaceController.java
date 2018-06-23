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

//   @PostMapping("createPlace")
//   public ResponseEntity<Place> createPlace(@RequestBody Place place){
//       placeService.savePlace(place);
//       return new ResponseEntity<Place>(place, HttpStatus.CREATED);
//    }

    @GetMapping("deletePlace/{id}")
    public ResponseEntity deletePlace(@PathVariable("id") Long id){
       placeService.deletePlace(id);
       return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("getPlace/{id}")
    public ResponseEntity<Place> getPlace(@PathVariable("id") Long id){
       Place place = placeService.getPlaceById(id);
       return place == null
               ? new ResponseEntity<Place>( (Place) null, HttpStatus.NOT_FOUND)
               : new ResponseEntity<Place>( place, HttpStatus.OK);
    }

}
