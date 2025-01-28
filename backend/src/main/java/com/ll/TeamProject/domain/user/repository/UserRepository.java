package com.ll.TeamProject.domain.user.repository;

import com.ll.TeamProject.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    // 필요한 메서드를 임시로 정의 (예: findById)
    // TEST용으로 추 후에 삭제해도 무방 (250127 캘린더생성)
}