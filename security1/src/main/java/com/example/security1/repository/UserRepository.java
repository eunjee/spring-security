package com.example.security1.repository;

import com.example.security1.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

//CRUD 함수를 Jpa Repository가 들고 있음
//@Repository 어노테이션이 없어도 IoC된다.
//JpaRepository를 상속받았기 때문
public interface UserRepository extends JpaRepository<User,Integer> {
    //findBy규칙 - username은 문법
    //select * from user where username=?
    public User findByUsername(String username); //Jpa 쿼리메서드

    //select * from user where email=?
    public User findByEmail(String email);
}
