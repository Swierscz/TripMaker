package com.tripmaker.demo.Controllers;

import com.tripmaker.demo.Data.*;
import com.tripmaker.demo.Services.TripGroupService;
import com.tripmaker.demo.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("api/tripGroup")
public class TripGroupController {

    @Autowired
    TripGroupService tripGroupService;

    @Autowired
    UserService userService;

    @PostMapping("createTripGroup")
    public ResponseEntity<TripGroup> createTripGroup(@RequestBody TripGroup tripGroup){
        tripGroupService.createGroup(tripGroup);
        return new ResponseEntity<TripGroup>(tripGroup, HttpStatus.CREATED);
    }


    //TODO zastanowić się nad możliwością konfiktów przy dodawaniu nowego miejsca
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

    //MOŻE W TEN SPOSÓB?
    @GetMapping("{name}/addUser/{mail}")
    public ResponseEntity<TripGroup> addUserToTripGroup(@PathVariable("name") String name, @PathVariable("mail") String mail ){
        TripGroup tripGroup = tripGroupService.findByName(name);
        User user = userService.findUserByEmail(mail);
        if(tripGroup == null || user == null) return new ResponseEntity<TripGroup>((TripGroup) null, HttpStatus.NOT_FOUND);
        else{
            if(tripGroup.getUsers().contains(user)) return new ResponseEntity<TripGroup>((TripGroup) null, HttpStatus.CONFLICT);
            else{
                tripGroup.addUser(user);
                return new ResponseEntity<TripGroup>(tripGroup, HttpStatus.ACCEPTED);
            }
        }
    }

    @GetMapping("{name}/owner")
    public ResponseEntity<User> getOwner(@PathVariable("name") String name){
        TripGroup tripGroup = tripGroupService.findByName(name);
        if(tripGroup == null) return new ResponseEntity<User>((User) null, HttpStatus.NOT_FOUND);
        else{
            User user = tripGroup.getOwner();
            if(user == null) return new ResponseEntity<User>((User) null, HttpStatus.NOT_FOUND);
            else return new ResponseEntity<User>(user, HttpStatus.FOUND);
        }
    }

    @GetMapping("getAll")
    public ResponseEntity<List<TripGroup>> getAllGroups(){
        List<TripGroup> groupList = tripGroupService.findAllGroups();
        if(groupList==null) return new ResponseEntity<List<TripGroup>>(groupList, HttpStatus.NOT_FOUND);
        else return new ResponseEntity<List<TripGroup>>(groupList, HttpStatus.FOUND);
    }
}
