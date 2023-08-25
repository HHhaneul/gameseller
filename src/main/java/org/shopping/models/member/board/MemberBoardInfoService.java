package org.shopping.models.member.board;

import lombok.RequiredArgsConstructor;
import org.shopping.commons.MemberUtil;
import org.shopping.entities.MemberBoardData;
import org.shopping.models.board.config.BoardConfigInfoService;
import org.shopping.repositories.member.BoardDataRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberBoardInfoService {

    private final BoardDataRepository boardDataRepository;
    private final BoardConfigInfoService configInfoService;
    private final MemberUtil memberUtil;

    public MemberBoardData get(Long id) {
        return get(id, null, "view");
    }

    public MemberBoardData get(Long id, String location) {
        return get(id, null, location);
    }

    public MemberBoardData get(Long id, String bId, String location) {

        MemberBoardData boardData = boardDataRepository.findById(id).orElseThrow(MemberBoardDataNotExistsException::new);

        // 게시판 설정 조회 + 접근 권한체크
        configInfoService.get(boardData.getBoard().getBId(), location);

        // 게시글 삭제 여부 체크(소프트 삭제)
        if (!memberUtil.isAdmin() && boardData.getDeletedAt() != null) {
            throw new MemberBoardDataNotExistsException();
        }
        return boardData;
    }
}