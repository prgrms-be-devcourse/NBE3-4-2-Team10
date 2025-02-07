package com.ll.TeamProject.domain.user.service;

import com.ll.TeamProject.domain.user.entity.ForbiddenNickname;
import com.ll.TeamProject.domain.user.repository.ForbiddenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ForbiddenService {
    private final ForbiddenRepository forbiddenRepository;

    public boolean isForbidden(String nickname) {
        // 금지어 명단
        List<ForbiddenNickname> forbiddenList = forbiddenRepository.findAll();

        // 금지어가 들어있는지 확인
        for(ForbiddenNickname forbiddenNickname : forbiddenList) {

            if(nickname.toLowerCase().contains(forbiddenNickname.getName().toLowerCase())) return true;
        }
        return false;
    }
}
