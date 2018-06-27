package com.tripmaker.demo.controllers;

import com.tripmaker.demo.data.*;
import com.tripmaker.demo.services.PlaceService;
import com.tripmaker.demo.services.TripGroupService;
import com.tripmaker.demo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("api")
public class TripGroupController {

    @Autowired
    TripGroupService tripGroupService;

    @Autowired
    UserService userService;

    @Autowired
    PlaceService placeService;

    @Autowired
    RequestAuthorization rAuth;

    //region CORE
    @PostMapping("tripGroup/createTripGroup")
    public ResponseEntity<TripGroup> createTripGroup(@RequestBody TripGroup tripGroup) {
        User currentUser = userService.getCurrentUser();
        tripGroup.setOwner(currentUser);
        tripGroup.addUser(userService.getCurrentUser());
        tripGroupService.saveGroup(tripGroup);
        return new ResponseEntity<TripGroup>(tripGroup, HttpStatus.CREATED);
    }


    @GetMapping("tripGroup/getTripGroupById/{id}")
    public ResponseEntity<TripGroup> getTripGroupById(@PathVariable("id") Long id) {
        TripGroup tripGroup = tripGroupService.findById(id);
        if(!(rAuth.isCurrentUserAdmin() || rAuth.isCurrentUserMember(tripGroup))){
            tripGroup = TripGroupRequestCutter.limitTripGroupForAimedRequest(tripGroup);
        }
        return new ResponseEntity<TripGroup>(tripGroup, HttpStatus.OK);
    }


    @GetMapping("tripGroup/deleteTripGroup/{id}")
    public ResponseEntity deleteTripGroup(@PathVariable("id") Long id) {
        TripGroup tripGroup = tripGroupService.findById(id);
        if(!(rAuth.isCurrentUserAdmin() || rAuth.isCurrentUserOwner(tripGroup))){
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }
        tripGroupService.deleteGroup(tripGroup.getId());
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("tripGroup/findGroupsByName/{name}")
    public ResponseEntity<Set<TripGroup>> findTripGroupsByName(@PathVariable("name") String name) {
        Set<TripGroup> tripGroups = tripGroupService.findGroupsByName(name);
        tripGroups = TripGroupRequestCutter.limitSetOfGroupsForSearchRequest(tripGroups);
        return new ResponseEntity<Set<TripGroup>>(tripGroups, HttpStatus.OK);
    }


    @GetMapping("tripGroup/findNearestGroupsByName/{name}")
    public ResponseEntity<TripGroup> findNearestGroupsByName(@PathVariable("name") String name) {

        //TODO Zaimplementowac

        return new ResponseEntity<TripGroup>((TripGroup) null, HttpStatus.NOT_IMPLEMENTED);
    }
    //endregion

    //region PLACE
    @PostMapping("tripGroup/{id}/addPlace")
    public ResponseEntity addPlaceToGroup(@PathVariable("id") Long id, @RequestBody Place place) {
        TripGroup tripGroup = tripGroupService.findById(id);
        return (!(rAuth.isCurrentUserAdmin() || rAuth.isCurrentUserMember(tripGroup)))
                ? new ResponseEntity(null, HttpStatus.UNAUTHORIZED)
                : checkIsPlaceAlreadyCreatedIfNotCreate(place, tripGroup);
    }

    public ResponseEntity checkIsPlaceAlreadyCreatedIfNotCreate(Place place, TripGroup tripGroup) {
        if (tripGroup.getPlaces() != null) {
            for (Place placeOnList : tripGroup.getPlaces()) {
                if (placeOnList.getName().equals(place.getName()))
                    return new ResponseEntity<TripGroup>(HttpStatus.CONFLICT);
            }
        }
        tripGroup.addPlaces(place);
        tripGroupService.saveGroup(tripGroup);
        return new ResponseEntity(HttpStatus.OK);
    }


    @GetMapping("tripGroup/{id}/deletePlace/{name}")
    public ResponseEntity deletePlaceFromTripGroupByName(@PathVariable("id") Long id, @PathVariable("name") String name) {
        TripGroup tripGroup = tripGroupService.findById(id);
        if(!(rAuth.isCurrentUserAdmin() || rAuth.isCurrentUserMember(tripGroup))) {
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }
        Place placeToDelete = null;
        if (tripGroup.getPlaces() != null) {
            for (Place place : tripGroup.getPlaces()) {
                if (place.getName().equals(name)) {
                    placeToDelete = place;
                }
            }
            tripGroup.getPlaces().remove(placeToDelete);
        }
        tripGroupService.saveGroup(tripGroup);
        return new ResponseEntity(HttpStatus.OK);
    }

    //endregion

    //TODO sprwadzić czy warunek jest potrzebny
    //region USER
    @GetMapping("tripGroup/{id}/addUser/{mail}")
    public ResponseEntity<TripGroup> addUserToTripGroup(@PathVariable("id") Long id, @PathVariable("mail") String mail) {
        if(!rAuth.isCurrentUserAdmin()) return new ResponseEntity<TripGroup>((TripGroup) null,HttpStatus.UNAUTHORIZED);
        TripGroup tripGroup = tripGroupService.findById(id);
        User user = userService.findUserByEmail(mail);
        return tripGroup.getUsers().contains(user)
                ? new ResponseEntity<TripGroup>((TripGroup) null, HttpStatus.CONFLICT)
                : new ResponseEntity<TripGroup>(tripGroup, HttpStatus.OK);
    }

    @GetMapping("tripGroup/{id}/join")
    public ResponseEntity joinGroup(@PathVariable("id") Long id){
        TripGroup tripGroup = tripGroupService.findById(id);
        tripGroup.getUsers().add(userService.getCurrentUser());
        tripGroupService.saveGroup(tripGroup);
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("tripGroup/{id}/quit")
    public ResponseEntity quitGroup(@PathVariable("id") Long id){
        TripGroup tripGroup = tripGroupService.findById(id);
        tripGroup.getUsers().remove(userService.getCurrentUser());
        tripGroupService.saveGroup(tripGroup);
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("tripGroup/{id}/isMember")
    public ResponseEntity<Boolean> isCurrentUserMember(@PathVariable("id") Long id){
        TripGroup tripGroup = tripGroupService.findById(id);
        for(User user : tripGroup.getUsers()){
            if(user == userService.getCurrentUser()) return new ResponseEntity<Boolean>(true, HttpStatus.OK);
        }
        return new ResponseEntity<Boolean>(false, HttpStatus.OK);
    }

    @GetMapping("tripGroup/getAll")
    public ResponseEntity<Set<TripGroup>> getAllGroups() {
        List<TripGroup> groupList = tripGroupService.findAllGroups();
        Set<TripGroup> outputSet = new HashSet<>(groupList);
        outputSet = TripGroupRequestCutter.limitSetOfGroupsForSearchRequest(outputSet);
        return new ResponseEntity<Set<TripGroup>>(outputSet, HttpStatus.OK);
    }


    //Pomocniczne metody





}
