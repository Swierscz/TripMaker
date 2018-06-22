package com.tripmaker.demo.services;

import com.tripmaker.demo.data.TripGroup;

import java.util.List;
import java.util.Set;


public interface TripGroupService {
    void saveGroup(TripGroup tripGroup);
    void deleteGroup(Long id);
    Set<TripGroup> findGroupsByName(String name);
    TripGroup findById(Long id);
    List<TripGroup> findAllGroups();
}
