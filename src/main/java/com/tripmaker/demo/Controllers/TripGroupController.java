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
@RequestMapping("api/tripGroup")
public class TripGroupController {

    @Autowired
    TripGroupService tripGroupService;

    @Autowired
    UserService userService;

    @PostMapping("createTripGroup")
    public ResponseEntity<TripGroup> createTripGroup(@RequestBody TripGroup tripGroup) {
        tripGroupService.saveGroup(tripGroup);
        return new ResponseEntity<TripGroup>(tripGroup, HttpStatus.CREATED);
    }

    //TODO sprawdzić czy warunek w if-ie działa poprawnie
    @PostMapping("{name}/addPlace")
    public ResponseEntity<TripGroup> addPlaceToGroup(@PathVariable("name") String name, @RequestBody Place place) {
        TripGroup tripGroup = tripGroupService.findByName(name);
        if (!tripGroup.getPlaces().contains(place))
            tripGroup.addPlaces(place);
        tripGroupService.saveGroup(tripGroup);
        return new ResponseEntity<TripGroup>(tripGroup, HttpStatus.OK);
    }

    @GetMapping("deleteTripGroup/{id}")
    public ResponseEntity deleteTripGroup(@PathVariable("id") Long id) {
        tripGroupService.deleteGroup(id);
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("findTripGroup/{name}")
    public ResponseEntity<TripGroup> findTripGroup(@PathVariable("name") String name) {
        return new ResponseEntity<TripGroup>(tripGroupService.findByName(name), HttpStatus.OK);
    }

    //TODO MOŻE W TEN SPOSÓB?
    @GetMapping("{name}/addUser/{mail}")
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

    @GetMapping("{name}/owner")
    public ResponseEntity<String> getOwnerName(@PathVariable("name") String name) {
        TripGroup tripGroup = tripGroupService.findByName(name);
        if (tripGroup == null) return new ResponseEntity<String>("Cannot find tripGroup ", HttpStatus.NOT_FOUND);
        else {
            return new ResponseEntity<String>(tripGroup.getName(), HttpStatus.OK);
        }
    }

    @GetMapping("getAll")
    public ResponseEntity<List<TripGroup>> getAllGroups() {
        List<TripGroup> groupList = tripGroupService.findAllGroups();
        if (groupList == null) return new ResponseEntity<List<TripGroup>>(groupList, HttpStatus.NOT_FOUND);
        else return new ResponseEntity<List<TripGroup>>(groupList, HttpStatus.OK);
    }
}
