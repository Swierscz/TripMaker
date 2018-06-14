package com.tripmaker.demo.Data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository("group_repository")
public interface TripGroupRepository extends JpaRepository<TripGroup, Long>{
    TripGroup findByName(String name);

}

