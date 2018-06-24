package com.tripmaker.demo.controllers;

import com.tripmaker.demo.data.*;
import com.tripmaker.demo.services.PlaceService;
import com.tripmaker.demo.services.TripGroupService;
import com.tripmaker.demo.services.UserService;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.swing.table.TableRowSorter;
import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Set;

import static com.tripmaker.demo.utils.JsonUtils.convertToJson;

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
    public ResponseEntity<String> getTripGroup(@PathVariable("id") Long id) {
        TripGroup tripGroup = tripGroupService.findById(id);
        tripGroup.getOwner().setTripGroups(null);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Type", "application/json;charset=UTF-8");
        return new ResponseEntity<String>(convertToJson(tripGroup), httpHeaders, HttpStatus.OK);

    }

    @GetMapping("r_user/tripGroup/getTripGroupById_woOwner/{id}")
    public ResponseEntity<TripGroup> findTripGroupById(@PathVariable("id") Long id) {
        return new ResponseEntity<TripGroup>(tripGroupService.findById(id), HttpStatus.OK);
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
        return new ResponseEntity<Set<TripGroup>>(tripGroupService.findGroupsByName(name), HttpStatus.OK);
    }


    @GetMapping("r_user/tripGroup/findNearestGroupsByName/{name}")
    public ResponseEntity<TripGroup> findNearestGroupsByName(@PathVariable("name") String name) {

        //TODO Zaimplementowac

        return new ResponseEntity<TripGroup>((TripGroup) null, HttpStatus.NOT_IMPLEMENTED);
    }
    //endregion

    //region PLACE
    @PostMapping("r_user/tripGroup/{id}/addPlace")
    public ResponseEntity<TripGroup> addPlaceToGroupAsUser(@PathVariable("id") Long id, @RequestBody Place place) {
        return addPlaceToGroup(id, place, Role.USER);
    }

    @PostMapping("r_admin/tripGroup/{id}/addPlace")
    public ResponseEntity<TripGroup> addPlaceToGroupAsAdmin(@PathVariable("id") Long id, @RequestBody Place place) {
        return addPlaceToGroup(id, place, Role.ADMIN);
    }

    public ResponseEntity<TripGroup> addPlaceToGroup(Long id, Place place, String role) {
        TripGroup tripGroup = tripGroupService.findById(id);

        return (!isAuthorizedToUpdateGroup(role, tripGroup))
                ? new ResponseEntity<TripGroup>((TripGroup) null, HttpStatus.UNAUTHORIZED)
                : checkIsPlaceAlreadyCreatedIfNotCreate(place, tripGroup);
    }

    public ResponseEntity<TripGroup> checkIsPlaceAlreadyCreatedIfNotCreate(Place place, TripGroup tripGroup) {
        if (tripGroup.getPlaces() != null) {
            for (Place placeOnList : tripGroup.getPlaces()) {
                if (placeOnList.getName().equals(place.getName()))
                    return new ResponseEntity<TripGroup>(HttpStatus.CONFLICT);
            }
        }
        tripGroup.addPlaces(place);
        tripGroupService.saveGroup(tripGroup);
        return new ResponseEntity<TripGroup>(tripGroup, HttpStatus.OK);
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
        if (!isAuthorizedToUpdateGroup(role, tripGroup)) return new ResponseEntity(HttpStatus.UNAUTHORIZED);

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

    //region USER
    @GetMapping("r_admin/tripGroup/{id}/addUser/{mail}")
    public ResponseEntity<TripGroup> addUserToTripGroup(@PathVariable("id") Long id, @PathVariable("mail") String mail) {
        TripGroup tripGroup = tripGroupService.findById(id);
        User user = userService.findUserByEmail(mail);
        return tripGroup == null || user == null
                ? new ResponseEntity<TripGroup>((TripGroup) null, HttpStatus.NOT_FOUND)
                : addUserToGroup(user, tripGroup);
    }

    private ResponseEntity<TripGroup> addUserToGroup(User user, TripGroup tripGroup) {
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


    //endregion


    /*
     *       |
     *       |   Funkcje nie zdefiniowane w dokumentacji. Użycie na własną odpowiedzialność :)
     *       V
     * */


//    @GetMapping("r_user/tripGroup/{name}/owner")
//    public ResponseEntity<String> getOwnerName(@PathVariable("name") String name) {
//        TripGroup tripGroup = tripGroupService.findByName(name);
//        if (tripGroup == null) return new ResponseEntity<String>("Cannot find tripGroup ", HttpStatus.NOT_FOUND);
//        else {
//            return new ResponseEntity<String>(tripGroup.getName(), HttpStatus.OK);
//        }
//    }
//
//    @GetMapping("r_user/tripGroup/getAll")
//    public ResponseEntity<List<TripGroup>> getAllGroups() {
//        List<TripGroup> groupList = tripGroupService.findAllGroups();
//        if (groupList == null) return new ResponseEntity<List<TripGroup>>(groupList, HttpStatus.NOT_FOUND);
//        else return new ResponseEntity<List<TripGroup>>(groupList, HttpStatus.OK);
//    }


    //Pomocniczne metody

    public boolean isAuthorizedToUpdateGroup(String role, TripGroup tripGroup) {
        if (!role.equals(Role.ADMIN)) {
            if (!isCurrentUserMember(tripGroup))
                return false;
        }
        return true;
    }

    public boolean isCurrentUserMember(TripGroup tripGroup) {
        User currentUser = userService.getCurrentUser();
        boolean condition = tripGroup.getOwner().equals(currentUser.getEmail());
        return condition ? true : false;
    }
}
