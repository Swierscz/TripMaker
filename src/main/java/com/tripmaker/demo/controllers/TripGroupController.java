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


    //region CORE
    @PostMapping("r_user/tripGroup/createTripGroup")
    public ResponseEntity<TripGroup> createTripGroup(@RequestBody TripGroup tripGroup) {
        User currentUser = userService.getCurrentUser();
        tripGroup.setOwner(currentUser);
        tripGroup.addUser(userService.getCurrentUser());
        tripGroupService.saveGroup(tripGroup);
        return new ResponseEntity<TripGroup>(tripGroup, HttpStatus.CREATED);
    }


    @GetMapping("r_user/tripGroup/getTripGroupById/{id}")
    public ResponseEntity<TripGroup> getTripGroupByIdAsUser(@PathVariable("id") Long id) {
        return getTripGroupById(id, Role.USER);
    }

    @GetMapping("r_admin/tripGroup/getTripGroupById/{id}")
    public ResponseEntity<TripGroup> getTripGroupByIdAsAdmin(@PathVariable("id") Long id){
        return getTripGroupById(id, Role.ADMIN);
    }

    public ResponseEntity<TripGroup> getTripGroupById(Long id, String role){
        TripGroup tripGroup = tripGroupService.findById(id);
        if(!isAdminOrMember(role, tripGroup)){
            tripGroup = TripGroupLimiter.limitTripGroupForAimedRequest(tripGroup);
        }
        return new ResponseEntity<TripGroup>(tripGroup, HttpStatus.OK);
    }

    @GetMapping("r_user/tripGroup/deleteTripGroup/{id}")
    public ResponseEntity deleteTripGroupAsUser(@PathVariable("id") Long id) {
        return deleteTripGroup(id, Role.USER);
    }

    @GetMapping("r_admin/tripGroup/deleteTripGroup/{id}")
    public ResponseEntity deleteTripGroupAsAdmin(@PathVariable("id") Long id) {
        return deleteTripGroup(id, Role.ADMIN);
    }

    public ResponseEntity deleteTripGroup(Long id, String role) {
        TripGroup tripGroup = tripGroupService.findById(id);

        if (!role.equals(Role.ADMIN)) {
            if (!(userService.getCurrentUser() == tripGroup.getOwner()))
                return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }
        tripGroupService.deleteGroup(tripGroup.getId());
        return new ResponseEntity(HttpStatus.OK);
    }


    @GetMapping("r_user/tripGroup/findGroupsByName/{name}")
    public ResponseEntity<Set<TripGroup>> findTripGroupsByName(@PathVariable("name") String name) {
        Set<TripGroup> tripGroups = tripGroupService.findGroupsByName(name);
        tripGroups = TripGroupLimiter.limitSetOfGroupsForSearchRequest(tripGroups);
        return new ResponseEntity<Set<TripGroup>>(tripGroups, HttpStatus.OK);
    }


    @GetMapping("r_user/tripGroup/findNearestGroupsByName/{name}")
    public ResponseEntity<TripGroup> findNearestGroupsByName(@PathVariable("name") String name) {

        //TODO Zaimplementowac

        return new ResponseEntity<TripGroup>((TripGroup) null, HttpStatus.NOT_IMPLEMENTED);
    }
    //endregion

    //region PLACE
    @PostMapping("r_user/tripGroup/{id}/addPlace")
    public ResponseEntity addPlaceToGroupAsUser(@PathVariable("id") Long id, @RequestBody Place place) {
        return addPlaceToGroup(id, place, Role.USER);
    }

    @PostMapping("r_admin/tripGroup/{id}/addPlace")
    public ResponseEntity addPlaceToGroupAsAdmin(@PathVariable("id") Long id, @RequestBody Place place) {
        return addPlaceToGroup(id, place, Role.ADMIN);
    }

    public ResponseEntity addPlaceToGroup(Long id, Place place, String role) {
        TripGroup tripGroup = tripGroupService.findById(id);

        return (!isAdminOrMember(role, tripGroup))
                ? new ResponseEntity<TripGroup>((TripGroup) null, HttpStatus.UNAUTHORIZED)
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


    @GetMapping("r_admin/tripGroup/{id}/deletePlace/{name}")
    public ResponseEntity deletePlaceFromTripGroupByNameAsAdmin(@PathVariable("id") Long id, @PathVariable("name") String name) {
        return deletePlaceFromGroup(id, name, Role.ADMIN);
    }

    @GetMapping("r_user/tripGroup/{id}/deletePlace/{name}")
    private ResponseEntity deletePlaceFromTripGroupByNameAsUser(@PathVariable("id") Long id, @PathVariable("name") String name) {
        return deletePlaceFromGroup(id, name, Role.USER);
    }

    public ResponseEntity deletePlaceFromGroup(Long id, String name, String role) {
        TripGroup tripGroup = tripGroupService.findById(id);
        if (!isAdminOrMember(role, tripGroup)) return new ResponseEntity(HttpStatus.UNAUTHORIZED);

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
    @GetMapping("r_admin/tripGroup/{id}/addUser/{mail}")
    public ResponseEntity<TripGroup> addUserToTripGroup(@PathVariable("id") Long id, @PathVariable("mail") String mail) {
        TripGroup tripGroup = tripGroupService.findById(id);
        User user = userService.findUserByEmail(mail);
        return tripGroup.getUsers().contains(user)
                ? new ResponseEntity<TripGroup>((TripGroup) null, HttpStatus.CONFLICT)
                : new ResponseEntity<TripGroup>(tripGroup, HttpStatus.OK);
    }

    @GetMapping("r_user/tripGroup/{id}/join")
    public ResponseEntity joinGroup(@PathVariable("id") Long id){
        TripGroup tripGroup = tripGroupService.findById(id);
        tripGroup.getUsers().add(userService.getCurrentUser());
        tripGroupService.saveGroup(tripGroup);
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("r_user/tripGroup/{id}/quit")
    public ResponseEntity quitGroup(@PathVariable("id") Long id){
        TripGroup tripGroup = tripGroupService.findById(id);
        tripGroup.getUsers().remove(userService.getCurrentUser());
        tripGroupService.saveGroup(tripGroup);
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("r_user/tripGroup/{id}/isMember")
    public ResponseEntity<Boolean> isCurrentUserMember(@PathVariable("id") Long id){
        TripGroup tripGroup = tripGroupService.findById(id);
        for(User user : tripGroup.getUsers()){
            if(user == userService.getCurrentUser()) return new ResponseEntity<Boolean>(true, HttpStatus.OK);
        }
        return new ResponseEntity<Boolean>(false, HttpStatus.OK);
    }

    @GetMapping("r_user/tripGroup/getAll")
    public ResponseEntity<Set<TripGroup>> getAllGroups() {
        List<TripGroup> groupList = tripGroupService.findAllGroups();
        Set<TripGroup> outputSet = new HashSet<>(groupList);
        outputSet = TripGroupLimiter.limitSetOfGroupsForSearchRequest(outputSet);
        return new ResponseEntity<Set<TripGroup>>(outputSet, HttpStatus.OK);
    }


    //Pomocniczne metody

    private boolean isAdminOrMember(String role, TripGroup tripGroup) {
        if (!role.equals(Role.ADMIN)) {
            if (!isCurrentUserMember(tripGroup))
                return false;
        }
        return true;
    }

    private boolean isCurrentUserMember(TripGroup tripGroup) {
        User currentUser = userService.getCurrentUser();
        for(User user : tripGroup.getUsers()){
            if(user.getEmail().equals(currentUser.getEmail())) return true;
        }
        return false;
    }



}
