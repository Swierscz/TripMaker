package com.tripmaker.demo.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Set;


@Repository("group_repository")
public interface TripGroupRepository extends JpaRepository<TripGroup, Long>{
//    TripGroup findByName(String name);

//    @Query("SELECT tp FROM trip_group tp WHERE LOWER(tp.name) LIKE %:name%")
    @Query("SELECT tp FROM TripGroup tp WHERE LOWER(tp.name) LIKE %:inputname%")
    Set<TripGroup> findLikeName(@Param("inputname") String inputname);

}

