package com.tripmaker.demo.Services;

import com.tripmaker.demo.Data.Place;
import com.tripmaker.demo.Data.TripGroup;
import com.tripmaker.demo.Data.TripGroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Service
public class TripGroupServiceImpl implements TripGroupService {

    @Autowired
    TripGroupRepository tripGroupRepository;


    @Override
    public void saveGroup(TripGroup tripGroup) {
        tripGroupRepository.save(tripGroup);
    }

    @Override
    public void deleteGroup(String name) {
        tripGroupRepository.delete(tripGroupRepository.findByName(name));
    }

    @Override
    public TripGroup findByName(String name) {
        return tripGroupRepository.findByName(name);
    }

    @Override
    public List<TripGroup> findAllGroups() {
        return tripGroupRepository.findAll();
    }

}
