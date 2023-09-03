package org.shopping.models.board.config;

import lombok.RequiredArgsConstructor;
import org.shopping.commons.Utils;
import org.shopping.commons.validators.RequiredValidator;
import org.shopping.entities.Board;
import org.shopping.entities.QBoard;
import org.shopping.repositories.BoardRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardConfigDeleteService implements RequiredValidator {

    private final BoardRepository boardRepository;
    private final Utils utils;

    public void delete(String bId){
        Board board = boardRepository.findById(bId).orElseThrow(BoardNotAllowDeleteException::new);
        boardRepository.delete(board);
        boardRepository.flush();
    }

    public void delete(String[] bIds) {
        QBoard board = QBoard.board;
        if (bIds == null || bIds.length == 0) return;

        List<Board> items = (List<Board>)boardRepository.findAll(board.bId.in(bIds));

        if (items == null || items.isEmpty()) return;

        boardRepository.deleteAll(items);
        boardRepository.flush();
    }

    public void delete(Board board){
        List<Board> items = new ArrayList<>();
        List<Integer> chks = board.getChkNo();
        nullCheck(chks, utils.getMessage("NotSelected.delete", "validation"));

        for (Integer chk : chks) {
            String bId = utils.getParam("bId_" + chk);
            Board item = boardRepository.findById(bId).orElse(null);
            if (item == null) continue;
            items.add(item);
        }

        boardRepository.deleteAll(items);
        boardRepository.flush();
    }
}
