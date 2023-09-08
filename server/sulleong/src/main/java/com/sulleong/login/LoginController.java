package com.sulleong.login;

import com.sulleong.login.dto.CredentialRequest;
import com.sulleong.login.dto.SessionMember;
import com.sulleong.member.Member;
import com.sulleong.member.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.UUID;

@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class LoginController {

    private final MemberService memberService;
    private final GuestService guestService;

    @Operation(summary = "구글 로그인", description = "로그인 생성시 세션 값을 헤더로 전송")
    @PostMapping("/oauth/login/google")
    public ResponseEntity<Void> google(@RequestBody CredentialRequest credentialRequest, HttpSession session) {
        Member member = memberService.OauthSaveOrUpdate(credentialRequest);
        SessionMember sessionMember = new SessionMember(member);
        String uuid = UUID.randomUUID().toString();
        session.setAttribute(uuid, sessionMember);

        HttpHeaders headers = new HttpHeaders();
        headers.add("authorization", uuid);
        return new ResponseEntity<>(headers, HttpStatus.OK);
    }

    @Operation(summary = "익명 로그인", description = "실제 유저처럼 행동은 가능하지만 좋아요 클릭 불가")
    @GetMapping("/login/guest")
    public ResponseEntity<Void> guest(HttpSession session) {
        String uuid = UUID.randomUUID().toString();
        Member member = guestService.saveOrUpdateGuest(uuid);
        SessionMember sessionMember = new SessionMember(member);
        session.setAttribute(uuid, sessionMember);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", uuid);
        return new ResponseEntity<>(headers, HttpStatus.OK);
    }
}