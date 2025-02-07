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
        List<ForbiddenNickname> forbiddenList = forbiddenRepository.findAll();

        for(ForbiddenNickname forbiddenNickname : forbiddenList) {
            if(nickname.toLowerCase().contains(forbiddenNickname.getForbiddenName().toLowerCase())) return true;
        }
        return false;
    }
}
