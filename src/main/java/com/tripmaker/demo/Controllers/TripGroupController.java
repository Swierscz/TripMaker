package com.tripmaker.demo.Controllers;

import com.tripmaker.demo.Data.Place;
import com.tripmaker.demo.Data.RoleRepository;
import com.tripmaker.demo.Data.TripGroup;
import com.tripmaker.demo.Data.TripGroupRepository;
import com.tripmaker.demo.Services.TripGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/tripGroup")
public class TripGroupController {

    @Autowired
    TripGroupService tripGroupService;


    @PostMapping("createTripGroup")
    public ResponseEntity<TripGroup> createTripGroup(@RequestBody TripGroup tripGroup){
        tripGroupService.createGroup(tripGroup);
        return new ResponseEntity<TripGroup>(tripGroup, HttpStatus.CREATED);
    }

    @PostMapping("{name}/addPlace")
    public ResponseEntity<TripGroup> addPlaceToGroup(@PathVariable("name") String name, @RequestBody Place place){
        TripGroup tripGroup= tripGroupService.findByName(name);
        tripGroup.addPlaces(place);
        tripGroupService.createGroup(tripGroup);
        return new ResponseEntity<TripGroup>(tripGroup, HttpStatus.ACCEPTED);
    }

    @GetMapping("deleteTripGroup/{name}")
    public ResponseEntity deleteTripGroup(@PathVariable("name") String name){
        tripGroupService.deleteGroup(name);
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("findTripGroup/{name}")
    public ResponseEntity<TripGroup> findTripGroup(@PathVariable("name") String name){
        return new ResponseEntity<TripGroup>(tripGroupService.findByName(name), HttpStatus.FOUND);
    }




}
