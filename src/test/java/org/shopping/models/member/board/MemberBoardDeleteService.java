package org.shopping.models.member.board;

import lombok.RequiredArgsConstructor;
import org.shopping.entities.MemberBoardData;
import org.shopping.repositories.member.BoardDataRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberBoardDeleteService {
    private final BoardDataRepository repository;

    public void delete(Long id) {
        MemberBoardData memberBoardData = repository.findById(id).orElseThrow(()->new MemberBoardValidationException("등록되지 않은 게시글 입니다."));

        repository.delete(memberBoardData);
        repository.flush();
    }
}
