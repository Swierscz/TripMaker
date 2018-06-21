package com.tripmaker.demo.Services;

import com.tripmaker.demo.Data.TripGroup;
import org.springframework.stereotype.Service;

import java.util.List;


public interface TripGroupService {
    void saveGroup(TripGroup tripGroup);
    void deleteGroup(Long id);
    TripGroup findByName(String name);
    TripGroup findById(Long id);
    List<TripGroup> findAllGroups();
}
