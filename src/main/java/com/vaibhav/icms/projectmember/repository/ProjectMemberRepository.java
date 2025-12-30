package com.vaibhav.icms.projectmember.repository;

import java.util.List;
import java.util.Optional;

import com.vaibhav.icms.projectmember.enums.ProjectRole;
import org.springframework.data.domain.Limit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.vaibhav.icms.projectmember.entity.ProjectMember;


@Repository
public interface ProjectMemberRepository extends JpaRepository<ProjectMember, Long> {
    
    // Check if a user is already part of a project
boolean existsByProjectIdAndUserId(Long projectId, Long userId);

   // Get all members of a project
List<ProjectMember> findByProjectId(Long projectId);

    // Get a specific project member (for remove/update role)
Optional<ProjectMember> findByProjectIdAndUserId(Long projectId,Long userId);

// get all projects for a user
List<ProjectMember> findByUserId(Long userId);


    @Query("""
    select pm.projectRole
    from ProjectMember pm
    where pm.project.id = :projectId
      and pm.user.id = :userId
""")
    Optional<ProjectRole> findUserRoleInProject(
            @Param("projectId") Long projectId,
            @Param("userId") Long userId
    );

}
