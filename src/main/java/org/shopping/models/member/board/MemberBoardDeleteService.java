package org.shopping.models.member.board;

import lombok.RequiredArgsConstructor;
import org.shopping.entities.MemberBoardData;
import org.shopping.repositories.member.BoardDataRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class MemberBoardDeleteService {
    private final BoardDataRepository repository;

    /**
     * 게시글 삭제
     *
     * @param id 게시글 번호
     * @param isComplete : false - 소프트 삭제, true - 완전 삭제
     */
    public void delete(Long id, boolean isComplete) {
        MemberBoardData data = repository.findById(id).orElseThrow(MemberBoardDataNotExistsException::new);

        if (isComplete) { // 완전 삭제
            repository.delete(data);
        } else { // 소프트 삭제
            data.setDeletedAt(LocalDateTime.now());
        }

        repository.flush();
    }

    public void delete(Long id) {
        delete(id, false);
    }
}