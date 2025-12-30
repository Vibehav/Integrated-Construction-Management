package com.vaibhav.icms.project.repository;


import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vaibhav.icms.project.entity.Project;


@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

    Optional<Project> findById(Long id);

    boolean existsById(Long id);

    //Spring reads it like Project-> user → id
    List<Project> findByMembersUserId(Long userId);
}
