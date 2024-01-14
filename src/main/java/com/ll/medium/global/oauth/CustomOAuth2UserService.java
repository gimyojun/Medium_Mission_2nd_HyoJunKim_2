package com.ll.medium.global.oauth;

import com.ll.medium.domain.member.member.entity.Member;
import com.ll.medium.domain.member.member.repository.MemberRepository;
import com.ll.medium.global.oauth.provider.KakaoUserInfo;
import com.ll.medium.global.oauth.provider.OAuth2UserInfo;
import com.ll.medium.global.auth.CustomUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
//OAuth2UserService 인터페이스를 구현한 DefaultOAuth2UserService
//카카오 뿐만아니라 다른
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    private final MemberRepository memberRepository;
    @Override
    //public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException가
    //DefaultOAuth2UserService에 구현되어있다.
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        //oAuth2UserRequest에 엑세스 토큰같은 정보들이 들어있다.
        //엑세스 토큰을 이용해 서드파티 서버로부터 사용자 정보를 받아온다.
        OAuth2User oAuth2User = super.loadUser(userRequest);
//        OAuth2MemberInfo memberInfo;
//        String oauthId = oAuth2User.getName();
//        Map<String, Object> attributes = oAuth2User.getAttributes();
//        Map<String, String> properties = (Map<String, String>) attributes.get("properties");
//
//        String nickname = properties.get("nickname");
//        String profileImgUrl = properties.get("profile_image");
//
//        //KAKAO
//        String providerTypeCode = userRequest.getClientRegistration().getRegistrationId().toUpperCase();
//        // username = KAKAO__oAuth2User.getName()
//        String username = String.format("%s__%s", providerTypeCode, oauthId);
//
//        Member member = memberService.whenSocialLogin(providerTypeCode, username, nickname, profileImgUrl).getData();

        //return new CustomOAuth2User(member.getId(), member.getUsername(), member.getPassword(), member.getAuthorities());
        return processOAuth2User(userRequest,oAuth2User);

    }

    private OAuth2User processOAuth2User(OAuth2UserRequest userRequest, OAuth2User oAuth2User) {
        String registrationId = userRequest.getClientRegistration().getRegistrationId().toUpperCase();
        OAuth2UserInfo oAuth2UserInfo = null;
        Member member = null;
        if (registrationId.equals("GOOGLE")) {
            System.out.println("구글 로그인 요청");
            //oAuth2UserInfo = new GoogleUserInfo(oAuth2User.getAttributes());
        } else if (registrationId.equals("FACEBOOK")) {
            System.out.println("페이스북 로그인 요청");
            //oAuth2UserInfo = new FaceBookUserInfo(oAuth2User.getAttributes());
        } else if (registrationId.equals("NAVER")) {
            System.out.println("네이버 로그인 요청");
            //oAuth2UserInfo = new NaverUserInfo((Map)oAuth2User.getAttributes().get("response"));
        } else if (registrationId.equals("KAKAO")) {
            System.out.println("카카오 로그인 요청");
            oAuth2UserInfo = new KakaoUserInfo(oAuth2User.getAttributes());
            member = kakaoMemberJoin(registrationId,oAuth2User,oAuth2UserInfo);
        } else {
            System.out.println("해당 소셜로그인 지원하지 않음");
            throw new RuntimeException("해당 소셜로그인은 지원하지 않습니다.");
        }

        //소셜 로그인(세션)
        return new CustomUser(member.getId(), member.getUsername(),member.getPassword(),member.getAuthorities(), oAuth2User.getAttributes());
    }
    public Member kakaoMemberJoin(String registrationId, OAuth2User oAuth2User, OAuth2UserInfo oAuth2UserInfo){

        String oauthId = oAuth2User.getName();
        // ex . socialLoginUsername = KAKAO__32489097
        String socialLoginUsername = String.format("%s__%s", registrationId, oauthId);
        Optional<Member> op = memberRepository.findByUsername(socialLoginUsername);
        Member member = null;
        if(op.isPresent()){
             member = op.get();
            member.setNickname(oAuth2UserInfo.getNickname());
            memberRepository.save(member);
        }else{
            member = Member.builder()
                    .username(socialLoginUsername)
                    .password("")
                    .isPaid(false)
                    .build();
            memberRepository.save(member);

        }
        return member;
    }


}