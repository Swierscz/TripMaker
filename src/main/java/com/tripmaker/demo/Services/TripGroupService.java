package com.tripmaker.demo.Services;

import com.tripmaker.demo.Data.TripGroup;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;


public interface TripGroupService {
    void saveGroup(TripGroup tripGroup);
    void deleteGroup(Long id);
    Set<TripGroup> findGroupsByName(String name);
    TripGroup findById(Long id);
    List<TripGroup> findAllGroups();
}
