package com.ll.TeamProject.domain.user.controller;

import com.ll.TeamProject.domain.user.dto.KakaoUserInfo;
import com.ll.TeamProject.domain.user.entity.SiteUser;
import com.ll.TeamProject.domain.user.service.UserService;
import com.ll.TeamProject.global.rq.Rq;
import com.ll.TeamProject.global.rsData.RsData;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

@Controller
@RequiredArgsConstructor
public class GoogleController {

    private final UserService userService;
    private final Rq rq;

    @Value("${google.client_id}")
    private String client_id;

    @Value("${google.redirect_uri}")
    private String redirect_uri;

    @Value("${google.client_key}")
    private String client_key;


    @GetMapping("/google/login")
    public String socialLogin(Model model) {
        String location = "https://accounts.google.com/o/oauth2/v2/auth?client_id="+client_id+"&redirect_uri="+redirect_uri+"&response_type=code&scope=email profile";
        model.addAttribute("googleLocation", location);
        return "login";
    }

    // 카카오 로그인 성공 확인용
    @GetMapping("/google/index")
    public RsData<Void> index(@RequestParam String code) {
        // Access Token 요청
        // Kakao API에 Access Token을 요청하기 위해 RestTemplate을 사용합니다.
        RestTemplate restTemplate = new RestTemplate();

        // HTTP 요청 헤더를 설정합니다.
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED); // 요청 데이터를 URL-Encoded 형식으로 전달

        // HTTP 요청 파라미터를 설정합니다.
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("client_id", client_id); // Google Cloud의 클라이언트 ID
        params.add("client_secret", client_key); // Google Cloud의 클라이언트 비밀키
        params.add("code", code); // 사용자 인증 후 받은 코드
        params.add("redirect_uri", redirect_uri); // 리디렉션 URI
        params.add("grant_type", "authorization_code"); // 고정값

        // 요청 본문과 헤더를 포함한 HttpEntity 생성
        HttpEntity<MultiValueMap<String, String>> tokenRequest = new HttpEntity<>(params, headers);

        // Kakao API의 Access Token 발급 엔드포인트로 POST 요청
        ResponseEntity<String> tokenResponse = restTemplate.postForEntity(
                "https://oauth2.googleapis.com/token", // Kakao Access Token 발급 API
                tokenRequest, // 요청 본문과 헤더
                String.class // 응답 데이터 타입 (String)
        );



        ///  ===== 백에서 처리해야할 부분 ======
        // 응답 본문에서 Access Token을 파싱합니다.
        JSONObject tokenJson = new JSONObject(tokenResponse.getBody());
        String accessToken = tokenJson.getString("access_token"); // Access Token 추출


        // Access Token을 사용해 Kakao 사용자 정보를 요청합니다.
        KakaoUserInfo userInfo = getUserInfo(accessToken);
        // 회원확인 및 회원가입 로직
        SiteUser siteUser = userService.socialLogin(userInfo, "GOOGLE_");

        // 사용자 정보를 기반으로 JWT(Json Web Token)를 발급합니다.
        String jwt = userService.genAccessToken(siteUser);

        rq.setCookie("accessToken", accessToken);
        rq.setCookie("apiKey", siteUser.getApiKey());


        return new RsData<>(
                "200-1",
                "환영합니다. %s 님".formatted(siteUser.getUsername())
        );
    }

    private KakaoUserInfo getUserInfo(String accessToken) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        HttpEntity<String> userInfoRequest = new HttpEntity<>(headers);
        ResponseEntity<String> userInfoResponse = restTemplate.exchange(
                "https://www.googleapis.com/userinfo/v2/me",
                HttpMethod.GET,
                userInfoRequest,
                String.class
        );

        // 사용자 정보 파싱
        JSONObject userJson = new JSONObject(userInfoResponse.getBody());
        String email = userJson.getString("email");

        return new KakaoUserInfo(email);
    }

}
