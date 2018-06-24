package com.tripmaker.demo.data;


import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class TripGroupJson implements Serializable {

    private Long id;

    private String name;

    private String description;

    private Long dateFrom;

    private Long dateTo;

    private User owner;

    private Set<Place> places;

    private Set<User> users;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(Long dateFrom) {
        this.dateFrom = dateFrom;
    }

    public Long getDateTo() {
        return dateTo;
    }

    public void setDateTo(Long dateTo) {
        this.dateTo = dateTo;
    }

    public Set<Place> getPlaces() {
        return places;
    }

    public void setPlaces(Set<Place> places) {
        this.places = places;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    public void addPlaces(Place place){
        if(places == null) places = new HashSet<Place>();
        places.add(place);
    }

    public void addUser(User user){
        if(users == null) users = new HashSet<User>();
        users.add(user);
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public String toString(){
        return id + " " + name + " " + description + " " + dateFrom + " " + dateTo;
    }


}
