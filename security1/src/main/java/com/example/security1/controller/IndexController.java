package com.example.security1.controller;

import com.example.security1.model.User;
import com.example.security1.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller //View를 리턴하겠다.
public class IndexController {

    private final UserRepository userRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public IndexController(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }


    @GetMapping({"","/"})
    public String index(){
        //머스테치 src/main/resources/
        //view리졸버 설정:templates (prefix), .mustache(suffix)로 설정해야 한다.
        //mustache 의존성을 주입하면 yml에서 생략할 수 있다.
        return "index"; //src/main/resource/index.mustache
    }

    @GetMapping("/user")
    public String user(){
        return "user";
    }
    @GetMapping("/admin")
    public String admin(){
        return "admin";
    }
    @GetMapping("/manager") //매니저 권한이 있는 사람만 접근 가능하도록 하고 싶다.
    public String manager(){
        return "manager";
    }
    //스프링 시큐리티가 해당 주소를 낚아챔
    //SecurityConfig를 통해서 "login"이 나옴
    @GetMapping("/loginForm")
    public String login(){
        return "loginForm";
    }
    @GetMapping("joinForm")
    public String joinForm(){
        return "joinForm";
    }

    @PostMapping("/join")
    public String join(User user){
        String rawPassword = user.getPassword();
        String encPassword = bCryptPasswordEncoder.encode(rawPassword);
        user.setPassword(encPassword);
        userRepository.save(user);//회원가입 잘됨 비밀번호 1234=>시큐리티 로그인을 할 수 없음. 비밀번호 암호화가 안됐기 때문에 로그인이 안된다.
        return "redirect:/loginForm";
    }
}
