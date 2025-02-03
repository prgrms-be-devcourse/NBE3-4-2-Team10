package com.ll.TeamProject.domain.user.repository;

import com.ll.TeamProject.domain.user.entity.SiteUser;
import com.ll.TeamProject.domain.user.enums.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<SiteUser, Long> {
    Optional<SiteUser> findByUsername(String username);

    Optional<SiteUser> findByApiKey(String apiKey);

    Page<SiteUser> findByRoleNot(Role role, PageRequest pageRequest);

    Page<SiteUser> findByRoleAndEmailLike(Role role, String emailLike, PageRequest pageRequest);

    Page<SiteUser> findByRoleAndUsernameLike(Role role, String usernameLike, PageRequest pageRequest);
}
