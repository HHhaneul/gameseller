package org.shopping.models.member;


import lombok.RequiredArgsConstructor;
import org.shopping.controllers.members.JoinForm;
import org.shopping.entities.Member;
import org.shopping.repositories.member.MemberRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
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

    public JoinForm formGet(Long userNo){
        Member member = repository.findById(userNo).orElseThrow(MemberNotFoundException::new);


        JoinForm form = JoinForm.builder()
                .userId(member.getUserId())
                .userNm(member.getUserNm())
                .email(member.getEmail())
                .mobile(member.getMobile())
                .build();

        return form;
    }



    public Member get(Long userNo){
        return repository.findById(userNo).orElseThrow(MemberNotFoundException::new);
    }


    public UserDetails loadUserByUserId(String userId) throws UsernameNotFoundException {

        Member member = repository.findByUserId(userId);
        if (member == null) {
            throw new UsernameNotFoundException(userId);
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

}