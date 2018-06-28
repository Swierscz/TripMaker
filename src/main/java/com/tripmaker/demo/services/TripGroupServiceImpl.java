package com.tripmaker.demo.services;

import com.tripmaker.demo.data.TripGroup;
import com.tripmaker.demo.data.TripGroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class TripGroupServiceImpl implements TripGroupService {

    @Autowired
    TripGroupRepository tripGroupRepository;

    @Override
    public void saveGroup(TripGroup tripGroup) {
        tripGroupRepository.save(tripGroup);
    }

    @Override
    public void deleteGroup(Long id) {
        tripGroupRepository.deleteById(id);
    }

    @Override
    public Set<TripGroup> findGroupsByName(String name) {
        return tripGroupRepository.findLikeName(name.toLowerCase());
    }


    @Override
    public TripGroup findById(Long id) {
        return tripGroupRepository.findById(id).get();
    }

    @Override
    public List<TripGroup> findAllGroups() {
        return tripGroupRepository.findAll();
    }

}
