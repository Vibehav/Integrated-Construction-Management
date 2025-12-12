package com.vaibhav.icms.project.repository;



import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vaibhav.icms.project.entity.Projects;


@Repository
public interface ProjectRepository extends JpaRepository<Projects, Long> {

    Optional<Projects> findByProjectCode(String projectCode);
    Optional<Projects> findById(Long id);

    boolean existsByName(String name); 
    boolean existsByProjectCode(String projectCode);
}
