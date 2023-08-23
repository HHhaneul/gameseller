package org.shopping.models.member.board;

import lombok.RequiredArgsConstructor;
import org.shopping.entities.MemberBoardData;
import org.shopping.repositories.member.BoardDataRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberBoardListService {
    private final BoardDataRepository repository;
    public List<MemberBoardData> gets() {
        return repository.findAll(Sort.by(Sort.Order.desc("regDt")));
    }
}
