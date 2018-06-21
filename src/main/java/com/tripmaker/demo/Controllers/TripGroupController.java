package com.tripmaker.demo.Controllers;

import com.tripmaker.demo.Data.*;
import com.tripmaker.demo.Services.TripGroupService;
import com.tripmaker.demo.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api")
public class TripGroupController {

    @Autowired
    TripGroupService tripGroupService;

    @Autowired
    UserService userService;

    @PostMapping("r_user/tripGroup/createTripGroup")
    public ResponseEntity<TripGroup> createTripGroup(@RequestBody TripGroup tripGroup) {
        tripGroup.setOwnerMail(userService.getCurrentUser().getEmail());
        tripGroupService.saveGroup(tripGroup);
        return new ResponseEntity<TripGroup>(tripGroup, HttpStatus.CREATED);
    }

    @PostMapping("r_user/tripGroup/{name}/addPlace")
    public ResponseEntity<TripGroup> addPlaceToGroupAsUser(@PathVariable("name") String name, @RequestBody Place place){
        return addPlaceToGroup(name, place, Role.USER);
    }

    @PostMapping("r_admin/tripGroup/{name}/addPlace")
    public ResponseEntity<TripGroup> addPlaceToGroupAsAdmin(@PathVariable("name") String name, @RequestBody Place place){
        return addPlaceToGroup(name, place, Role.ADMIN);
    }

    public ResponseEntity<TripGroup> addPlaceToGroup(String name, Place place, String role){
        TripGroup tripGroup = tripGroupService.findByName(name);
        if(tripGroup == null) return new ResponseEntity<TripGroup>((TripGroup) null, HttpStatus.NOT_FOUND);

        if(!role.equals(Role.ADMIN)){
            if(!isCurrentUserMember(tripGroup)) return new ResponseEntity<TripGroup>( (TripGroup) null, HttpStatus.UNAUTHORIZED);
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
    public ResponseEntity deleteTripGroupAsUser(@PathVariable("id") Long id){
        return deleteTripGroup(id, Role.USER);
    }

    @GetMapping("r_admin/tripGroup/deleteTripGroup/{id}")
    public ResponseEntity deleteTripGroupAsAdmin(@PathVariable("id") Long id){
        return deleteTripGroup(id, Role.ADMIN);
    }

    public ResponseEntity deleteTripGroup(Long id, String role){
        TripGroup tripGroup = tripGroupService.findById(id);
        if(tripGroup == null) return new ResponseEntity(HttpStatus.NOT_FOUND);

        if(!role.equals(Role.ADMIN)){
            if(!userService.getCurrentUser().getEmail().equals(tripGroup.getOwnerMail())) return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }

        tripGroupService.deleteGroup(tripGroup.getId());
        return new ResponseEntity(HttpStatus.OK);
    }



    @GetMapping("r_user/tripGroup/getTripGroupById/{id}")
    public ResponseEntity<TripGroup> findTripGroupById(@PathVariable("id") Long id) {
        TripGroup tripGroup = tripGroupService.findById(id);
        ResponseEntity<TripGroup> response =
                tripGroup == null
                        ? new ResponseEntity<TripGroup>((TripGroup) null, HttpStatus.NOT_FOUND)
                        : new ResponseEntity<TripGroup>(tripGroup, HttpStatus.OK);
        return response;
    }

    @GetMapping("r_user/tripGroup/findGroupsByName/{name}")
    public ResponseEntity<TripGroup> findTripGroups(@PathVariable("name") String name) {

        //TODO Zaimplementować funkcje w oparciu o wyrażenia regularne

        return new ResponseEntity<TripGroup>((TripGroup) null, HttpStatus.NOT_IMPLEMENTED);
    }
//

    @GetMapping("r_user/tripGroup/findNearestGroupsByName/{name}")
    public ResponseEntity<TripGroup> findNearestGroupsByName(@PathVariable("name") String name){

        //TODO Zaimplementowac

        return new ResponseEntity<TripGroup>((TripGroup) null, HttpStatus.NOT_IMPLEMENTED);
    }


    /*
    *       |
    *       |   Funkcje nie zdefiniowane w dokumentacji. Użycie na własną odpowiedzialność :)
    *       V
    * */

    @GetMapping("r_user/tripGroup/{name}/addUser/{mail}")
    public ResponseEntity<TripGroup> addUserToTripGroup(@PathVariable("name") String name, @PathVariable("mail") String mail) {
        TripGroup tripGroup = tripGroupService.findByName(name);
        User user = userService.findUserByEmail(mail);
        if (tripGroup == null || user == null)
            return new ResponseEntity<TripGroup>((TripGroup) null, HttpStatus.NOT_FOUND);
        else {
            if (tripGroup.getUsers().contains(user))
                return new ResponseEntity<TripGroup>((TripGroup) null, HttpStatus.CONFLICT);
            else {
                tripGroup.addUser(user);
                return new ResponseEntity<TripGroup>(tripGroup, HttpStatus.OK);
            }
        }
    }

    @GetMapping("r_user/tripGroup/{name}/owner")
    public ResponseEntity<String> getOwnerName(@PathVariable("name") String name) {
        TripGroup tripGroup = tripGroupService.findByName(name);
        if (tripGroup == null) return new ResponseEntity<String>("Cannot find tripGroup ", HttpStatus.NOT_FOUND);
        else {
            return new ResponseEntity<String>(tripGroup.getName(), HttpStatus.OK);
        }
    }

    @GetMapping("r_user/tripGroup/getAll")
    public ResponseEntity<List<TripGroup>> getAllGroups() {
        List<TripGroup> groupList = tripGroupService.findAllGroups();
        if (groupList == null) return new ResponseEntity<List<TripGroup>>(groupList, HttpStatus.NOT_FOUND);
        else return new ResponseEntity<List<TripGroup>>(groupList, HttpStatus.OK);
    }


    //Pomocniczne metody
    public boolean isCurrentUserMember(TripGroup tripGroup) {
        User currentUser = userService.getCurrentUser();
        boolean condition = tripGroup.getOwnerMail().equals(currentUser.getEmail());
        return condition ? true : false;
    }
}
