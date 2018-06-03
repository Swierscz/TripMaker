package com.tripmaker.demo.Data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("role_repository")
public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByRole(String role);
}
