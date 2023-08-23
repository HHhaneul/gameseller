package org.shopping.models.member.board;

import lombok.RequiredArgsConstructor;
import org.shopping.entities.MemberBoardData;
import org.shopping.repositories.member.BoardDataRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberBoardInfoService {
    private final BoardDataRepository repository;

    public MemberBoardData get(Long id) {
        MemberBoardData data = repository.findById(id).orElseThrow(()->new MemberBoardValidationException("잘못된 요청입니다."));
        return data;
    }
}
