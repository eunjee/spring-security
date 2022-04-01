package com.example.security1.config.auth;
//시큐리티가 /login 주소 요청이 오면 낚아채서 로그인을 진행시킨다.
//로그인 진행이 완료가 되면 시큐리티 session을 만들어준다.
// (SecurityContextHolder 키 값에 세션 정보를 저장한다.)
// 시큐리티에 들어갈 수 있는 오브젝트는 정해져있다. => Authentication 객체
//Authentication 안에 User정보가 있어야 한다.
//User오브젝트의 타입 => UserDetails 타입 객체

import com.example.security1.model.User;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

//시큐리티 세션 영역에 정보를 저장해줄 때=> Authentication => UserDetails(PrincipalDetails)
@Data
public class PrincipalDetails implements UserDetails, OAuth2User {

    private User user; //컴포지션
    private Map<String,Object> attributes;

    //일반 로그인
    public PrincipalDetails(User user) {
        this.user = user;
    }
    //OAuth 로그인
    public PrincipalDetails(User user,Map<String,Object> attributes){
        this.user = user;
        this.attributes=attributes;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public String getName() {
        return null;
    }
    //--------------------------------------
    //해당 유저의 권한을 리턴하는 곳
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        //user.getRole();=> 이넘에서 정해진 타입으로 바꿔줘야 한다.
        Collection<GrantedAuthority> collect = new ArrayList<>();
        collect.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return user.getRole().toString();
            }
        });
        return collect;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;//만료 안됨
    }

    //계정 잠금 확인
    @Override
    public boolean isAccountNonLocked() {
        return true; //아니요
    }


    @Override
    public boolean isCredentialsNonExpired() {
        return true;//계정 비밀번호 오래됐니?  아니요
    }

    @Override
    public boolean isEnabled() {
        //우리 사이트에서 1년동안 회원이 로그인을 안하면 휴면계정으로 전환
        //로그인 날짜를 저장하고 현재시간과 최근 로그인 시간을 비교해서 리턴을 하는 방식으로 구현할 수 있다.
        return true;//계정 활성화 되어있니?  아니요
    }

}
