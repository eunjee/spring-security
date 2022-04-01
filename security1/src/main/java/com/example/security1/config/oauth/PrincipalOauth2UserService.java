package com.example.security1.config.oauth;

import com.example.security1.config.auth.PrincipalDetails;
import com.example.security1.model.User;
import com.example.security1.model.UserRole;
import com.example.security1.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserRepository userRepository;

    @Autowired
    public PrincipalOauth2UserService(BCryptPasswordEncoder bCryptPasswordEncoder, UserRepository userRepository) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userRepository = userRepository;
    }


    //구글로 부터 받은 userRequest 데이터에 대한 후처리되는 함수
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        System.out.println("userRequest = " + userRequest.getClientRegistration());//registrationId로 어떤 Oauth로 로그인 했는 지 확인가능
        System.out.println("userRequest.getAccessToken() = " + userRequest.getAccessToken().getTokenValue()); //액세스토큰
        System.out.println("userRequest.getClientRegistration().g = " + userRequest.getClientRegistration());//사용자 정보
        OAuth2User oAuth2User = super.loadUser(userRequest);
        //구글로그인 버튼 클릭 -> 구글로그인창 -> 로그인을 완료->code를 리턴(OAuth-Client라이브러리)->AccessToken을 요청
        // userRequest정보 -> 회원프로필을 받아야 함(loadUser함수)-> 구글로부터 회원프로필 받아준다.


        //회원가입을 강제로 진행해볼 예정
        System.out.println("super.loadUser(userRequest).getAttributes() = " + super.loadUser(userRequest).getAttributes());
        String provider = userRequest.getClientRegistration().getRegistrationId();//google
        String providerId = oAuth2User.getAttribute("sub");
        String username = provider+"_"+providerId; //google_sub값
        String password = bCryptPasswordEncoder.encode("겟인데어");
        String email = oAuth2User.getAttribute("email");
        UserRole role = UserRole.ROLE_USER;

        User userEntity = userRepository.findByUsername(username);
        if(userEntity==null) {
            System.out.println("구글로그인이 최초입니다");
            userEntity = User.builder()
                    .username(username)
                    .password(password)
                    .email(email)
                    .role(role)
                    .provider(provider)
                    .provideId(providerId)
                    .createDate(LocalDateTime.now())
                    .build();
            userRepository.save(userEntity);
        }else{
            System.out.println("구글로그인을 이미 한 적이 있습니다. 회원가입이 이미 완료되었습니다.");
        }
        return new PrincipalDetails(userEntity,oAuth2User.getAttributes()); //Authentication 안에 들어간다.
    }
}
