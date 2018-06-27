package com.tripmaker.demo.controllers;


import com.tripmaker.demo.data.TripGroup;

import java.util.Set;

//TODO Zmienić nazwę tej klasy
public class TripGroupRequestCutter {

    public static Set<TripGroup> limitSetOfGroupsForSearchRequest(Set<TripGroup> tripGroupSet){
        for(TripGroup tripGroup : tripGroupSet){
            tripGroup = limitTripGroupForSearchRequest(tripGroup);
        }
        return tripGroupSet;
    }

    public static TripGroup limitTripGroupForSearchRequest(TripGroup tripGroup){
        tripGroup.setDescription(null);
        tripGroup.setOwner(null);
        tripGroup.setPlaces(null);
        tripGroup.setUsers(null);
        return tripGroup;
    }

    public static TripGroup limitTripGroupForAimedRequest(TripGroup tripGroup){
        tripGroup.setUsers(null);
        tripGroup.setPlaces(null);
        tripGroup.getOwner().setTripGroups(null);
        return tripGroup;
    }

}
