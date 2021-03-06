package com.tripmaker.demo.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("place_repository")
public interface PlaceRepository extends JpaRepository<Place, Long> {
    Place findByName(String name);
}
