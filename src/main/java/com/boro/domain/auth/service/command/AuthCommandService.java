package com.boro.domain.auth.service.command;

import com.boro.domain.auth.converter.AuthConverter;
import com.boro.domain.auth.converter.OAuthConverter;
import com.boro.domain.auth.dto.request.AuthRequestDTO;
import com.boro.domain.auth.dto.response.AuthResponseDTO;
import com.boro.domain.auth.dto.response.OAuth2ResponseDTO;
import com.boro.domain.auth.dto.util.TokenDTO;
import com.boro.domain.auth.entity.Social;
import com.boro.domain.auth.factory.GoogleUserLoader;
import com.boro.domain.auth.repository.SocialRepository;
import com.boro.domain.auth.service.query.RedisStorageQueryService;
import com.boro.domain.member.entity.Member;
import com.boro.domain.member.entity.enums.SocialType;
import com.boro.domain.member.repository.MemberRepository;
import com.boro.global.error.code.status.AuthErrorCode;
import com.boro.global.error.code.status.MemberErrorCode;
import com.boro.global.error.exception.handler.AuthException;
import com.boro.global.error.exception.handler.MemberException;
import com.boro.global.security.domain.CustomUserDetails;
import com.boro.global.security.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class AuthCommandService {

    private final GoogleUserLoader googleUserLoader;
    private final SocialRepository socialRepository;
    private final MemberRepository memberRepository;
    private final TokenCommandService tokenCommandService;
    private final RedisStorageCommandService redisStorageCommandService;
    private final RedisStorageQueryService redisStorageQueryService;
    private final JwtUtil jwtUtil;

    public OAuth2ResponseDTO.Login loginWithOAuth(HttpServletRequest request, HttpServletResponse response,
                                                  String code) {
        OAuth2ResponseDTO.GetUserInfo userInfo = googleUserLoader.loadUser(code);
        Optional<Social> socialOptional = socialRepository.findBySocialTypeAndProviderId(
                        SocialType.GOOGLE, userInfo.providerId()
        );

        // 회원가입 이력이 있으면 로그인
        if (socialOptional.isPresent()) {
            Member member = socialOptional.get().getMember();

            CustomUserDetails customUserDetails = new CustomUserDetails(member);
            AuthResponseDTO.TokenResult loginToken = tokenCommandService.createLoginToken(customUserDetails);
            redisStorageCommandService.addRefreshToken(member.getId(), loginToken.refreshToken());
            return OAuthConverter.toLogin(loginToken.accessToken(), loginToken.refreshToken());
        }

        // 회원 가입 이력 없으면 회원 가입용 토큰 발급
        String signUpToken = jwtUtil.createSignUpToken(
                SocialType.GOOGLE, userInfo.providerId(), userInfo.email(), userInfo.name()
        );

        return OAuthConverter.toNeedSignUp(signUpToken, userInfo.email(), userInfo.name());
    }

    public AuthResponseDTO.TokenResult signUp(AuthRequestDTO.SignUp request){
        TokenDTO.SignUpTokenPayload payload = jwtUtil.getSignUpPayload(request.signUpToken());
        validateSignUp(payload.email());

        Member member = memberRepository.save(AuthConverter.toMember(payload, request));
        Social social = socialRepository.save(AuthConverter.toSocial(SocialType.GOOGLE, payload));
        member.addSocial(social);

        CustomUserDetails customUserDetails = new CustomUserDetails(member);
        AuthResponseDTO.TokenResult loginToken = tokenCommandService.createLoginToken(customUserDetails);
        redisStorageCommandService.addRefreshToken(member.getId(), loginToken.refreshToken());
        return loginToken;
    }

    public AuthResponseDTO.AccessTokenResult reissue(HttpServletRequest request, HttpServletResponse response){
        String refreshToken = JwtUtil.resolveToken(request);

        // 토큰 파싱 가능 여부
        if (refreshToken == null || !jwtUtil.isValid(refreshToken)) {
            throw new AuthException(AuthErrorCode.INVALID_REFRESH_TOKEN);
        }
        Long memberId = jwtUtil.getMemberId(refreshToken);
        String savedRefreshToken = redisStorageQueryService.getRefreshToken(memberId);
        // 기존에 있는 토큰 동일 여부
        if (savedRefreshToken == null || !savedRefreshToken.equals(refreshToken)) {
            throw new AuthException(AuthErrorCode.INVALID_REFRESH_TOKEN);
        }

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));
        CustomUserDetails customUserDetails = new CustomUserDetails(member);
        String accessToken = tokenCommandService.reissueAccessToken(customUserDetails);
        return AuthConverter.toAccessTokenResult(member.getId(), accessToken);
    }

    // 로그아웃
    public void logout (HttpServletRequest request, Long memberId){
        String refreshToken = redisStorageQueryService.getRefreshToken(memberId);
        String accessToken = JwtUtil.resolveToken(request);
        // 리프레쉬, 액세스 토큰 그냥 싹다 안 됨 처리 - 블랙 리스트 추가, refresh에서 삭제
        redisStorageCommandService.deleteRefreshToken(memberId);
        redisStorageCommandService.addBlackList(refreshToken);
        redisStorageCommandService.addBlackList(accessToken);
    }

    public void withdraw(Long memberId){
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));
        memberRepository.delete(member);
    }

    private void validateSignUp(String email){
        if (memberRepository.existsByEmail(email)){
            throw new MemberException(MemberErrorCode.ALREADY_EXIST_EMAIL);
        }
    }
}
