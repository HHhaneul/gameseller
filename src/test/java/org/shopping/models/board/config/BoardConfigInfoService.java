package org.shopping.models.board.config;


import lombok.RequiredArgsConstructor;
import org.shopping.commons.MemberUtil;
import org.shopping.commons.constants.Role;
import org.shopping.entities.Board;
import org.shopping.repositories.BoardRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BoardConfigInfoService {

    private final BoardRepository boardRepository;
    private final MemberUtil memberUtil;

    public Board get(String bId, String location) { // 프론트, 접근 권한 체크
        return get(bId, false, location);
    }

    /**
     * 게시판 설정 조회
     *
     * @param bId
     * @param isAdmin : true - 권한 체크 X
     *                : false - 권한 체크, location으로 목록, 보기, 글쓰기, 답글, 댓글
     *
     * @param location : 기능 위치(list, view, write, reply, comment)
     *
     * @return
     */
    public Board get(String bId, boolean isAdmin, String location) {

        Board board = boardRepository.findById(bId).orElseThrow(BoardConfigNotExistException::new);

        if (!isAdmin) {
            accessCheck(board, location);
        }

        return board;
    }

    public Board get(String bId, boolean isAdmin) {
        return get(bId, isAdmin, null);
    }

    /**
     * 접근 권한 체크
     *
     * @param board
     */
    private void accessCheck(Board board, String location) {
        Role role = Role.ALL;

        /* 목록 접근 권한 */
        if (location.equals("list")) {
            role = board.getListAccessRole();

        /* 게시글 접근 권한 */
        } else if (location.equals("view")) {
            role = board.getViewAccessRole();

        /* 글쓰기 권한 */
        } else if (location.equals("write")) {
            role = board.getWriteAccessRole();

        /* 답글 권한 */
        } else if (location.equals("reply")) {
            role = board.getReplyAccessRole();

        /* 댓글 권한 */
        } else if (location.equals("comment")) {
            role = board.getCommentAccessRole();

        }

        if ((role == Role.USER && !memberUtil.isLogin())
                || (role == Role.ADMIN && !memberUtil.isAdmin())) {
            throw new BoardNotAllowAccessException();
        }
    }
}