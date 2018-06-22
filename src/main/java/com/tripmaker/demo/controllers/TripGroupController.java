package com.tripmaker.demo.controllers;

import com.tripmaker.demo.data.*;
import com.tripmaker.demo.services.TripGroupService;
import com.tripmaker.demo.services.UserService;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Set;

import static com.tripmaker.demo.utils.JsonUtils.convertToJson;

@RestController
@RequestMapping("api")
public class TripGroupController {

    @Autowired
    TripGroupService tripGroupService;

    @Autowired
    UserService userService;

    @PostMapping("r_user/tripGroup/createTripGroup")
    public ResponseEntity<TripGroup> createTripGroup(@RequestBody TripGroup tripGroup) {
        User currentUser = userService.getCurrentUser();
        tripGroup.setOwner(currentUser);
        tripGroupService.saveGroup(tripGroup);
        currentUser.getTripGroups().add(tripGroup);
        userService.saveUser(currentUser);
        return new ResponseEntity<TripGroup>(tripGroup, HttpStatus.CREATED);
    }

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
        if (tripGroup == null) return new ResponseEntity<TripGroup>((TripGroup) null, HttpStatus.NOT_FOUND);

        if (!role.equals(Role.ADMIN)) {
            if (!isCurrentUserMember(tripGroup))
                return new ResponseEntity<TripGroup>((TripGroup) null, HttpStatus.UNAUTHORIZED);
        }

        return checkIsPlaceAlreadyCreatedIfNotCreate(place, tripGroup);
    }

    public ResponseEntity<TripGroup> checkIsPlaceAlreadyCreatedIfNotCreate(Place place, TripGroup tripGroup) {
        if (tripGroup.getPlaces().contains(place)) {
            return new ResponseEntity<TripGroup>((TripGroup) null, HttpStatus.CONFLICT);
        } else {
            tripGroup.addPlaces(place);
            tripGroupService.saveGroup(tripGroup);
            return new ResponseEntity<TripGroup>(tripGroup, HttpStatus.OK);
        }
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
        if (tripGroup == null) return new ResponseEntity(HttpStatus.NOT_FOUND);

        if (!role.equals(Role.ADMIN)) {
            if (!(userService.getCurrentUser() == tripGroup.getOwner()))
                return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }

        tripGroupService.deleteGroup(tripGroup.getId());
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("r_user/tripGroup/getTripGroupById/{id}")
    public ResponseEntity<String> getTripGroup(@PathVariable("id") Long id) {

        TripGroup tripGroup = tripGroupService.findById(id);
        tripGroup.setOwner(tripGroup.getOwner());

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Type", "application/json;charset=UTF-8");
        return new ResponseEntity<String>(convertToJson(tripGroup), httpHeaders, HttpStatus.OK);

    }

    @GetMapping("r_user/tripGroup/getTripGroupById_woOwner/{id}")
    public ResponseEntity<TripGroup> findTripGroupById(@PathVariable("id") Long id) {
        TripGroup tripGroup = tripGroupService.findById(id);
        ResponseEntity<TripGroup> response =
                tripGroup == null
                        ? new ResponseEntity<TripGroup>((TripGroup) null, HttpStatus.NOT_FOUND)
                        : new ResponseEntity<TripGroup>(tripGroup, HttpStatus.OK);
        return response;
    }

    //TODO Zaimplementować funkcje w oparciu o wyrażenia regularne
    @GetMapping("r_user/tripGroup/findGroupsByName/{name}")
    public ResponseEntity<Set<TripGroup>> findTripGroupsByName(@PathVariable("name") String name) {
        Set<TripGroup> listOfFoundGroups = tripGroupService.findGroupsByName(name);
        return listOfFoundGroups == null
                ? new ResponseEntity<Set<TripGroup>>((Set<TripGroup>) null, HttpStatus.NOT_FOUND)
                : new ResponseEntity<Set<TripGroup>>( listOfFoundGroups, HttpStatus.OK);
    }


    @GetMapping("r_user/tripGroup/findNearestGroupsByName/{name}")
    public ResponseEntity<TripGroup> findNearestGroupsByName(@PathVariable("name") String name) {

        //TODO Zaimplementowac

        return new ResponseEntity<TripGroup>((TripGroup) null, HttpStatus.NOT_IMPLEMENTED);
    }

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


    public boolean isCurrentUserMember(TripGroup tripGroup) {
        User currentUser = userService.getCurrentUser();
        boolean condition = tripGroup.getOwner().equals(currentUser.getEmail());
        return condition ? true : false;
    }
}
