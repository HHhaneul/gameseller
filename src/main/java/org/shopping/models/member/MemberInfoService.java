package org.shopping.models.member;


import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.shopping.controllers.members.JoinForm;
import org.shopping.entities.Member;
import org.shopping.entities.QMember;
import org.shopping.repositories.member.MemberRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class MemberInfoService implements UserDetailsService {

    private final MemberRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Member member = repository.findByUserId(username);
        if (member == null) {
            throw new UsernameNotFoundException(username);
        }

        List<GrantedAuthority> authorities
                = Arrays.asList(new SimpleGrantedAuthority(member.getRoles().toString()));

        return MemberInfo.builder()
                .userNo(member.getUserNo())
                .userId(member.getUserId())
                .userPw(member.getUserPw())
                .userNm(member.getUserNm())
                .email(member.getEmail())
                .mobile(member.getMobile())
                .roles(member.getRoles())
                .authorities(authorities)
                .build();

    }
    public UserDetails loadUserByUserId(String userNm) throws UsernameNotFoundException {

        Member member = repository.findByUserId(userNm);
        if (member == null) {
            throw new UsernameNotFoundException(userNm);
        }

        List<GrantedAuthority> authorities
                = Arrays.asList(new SimpleGrantedAuthority(member.getRoles().toString()));

        return MemberInfo.builder()
                .userNo(member.getUserNo())
                .userId(member.getUserId())
                .userPw(member.getUserPw())
                .userNm(member.getUserNm())
                .email(member.getEmail())
                .mobile(member.getMobile())
                .roles(member.getRoles())
                .authorities(authorities)
                .build();

    }

    public JoinForm adminMemberUpdate(){

        JoinForm joinForm = new JoinForm();
        String aa = new String();

        return null;
    }

    class Student{
        private String stuName;
        private Long stuNo;


    }
}