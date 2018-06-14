package com.tripmaker.demo.Services;

import com.tripmaker.demo.Data.TripGroup;
import org.springframework.stereotype.Service;


public interface TripGroupService {
    void createGroup(TripGroup tripGroup);
    void deleteGroup(String name);
    TripGroup findByName(String name);
}
