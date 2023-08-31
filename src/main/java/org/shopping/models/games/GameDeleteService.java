package org.shopping.models.games;

import lombok.RequiredArgsConstructor;
import org.shopping.commons.Utils;
import org.shopping.commons.validators.RequiredValidator;
import org.shopping.controllers.admins.game.CategoryForm;
import org.shopping.controllers.admins.game.GameForm;
import org.shopping.entities.Category;
import org.shopping.entities.Game;
import org.shopping.repositories.CategoryRepository;
import org.shopping.repositories.GameRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GameDeleteService implements RequiredValidator {
    private final GameRepository repository;
    private final Utils utils;

    /**
     * 분류 삭제
     *
     * @param gameNo
     */
    public void delete(Long gameNo) {
        Game item = repository.findById(gameNo).orElse(null);
        if (item != null) {
            repository.delete(item);
            repository.flush();
        }

    }

    /**
     *  목록 삭제 처리
     *
     */
    public void deleteList(GameForm form) {
        List<Game> items = new ArrayList<>();
        List<Integer> chks = form.getChkNo();
        nullCheck(chks, utils.getMessage("NotSelected.delete", "validation"));

        for (Integer chk : chks) {
            String gameNo = utils.getParam("gameNo_" + chk);
            Game item = repository.findById(Long.valueOf(gameNo)).orElse(null);
            if (item == null) continue;
            items.add(item);
        }

        repository.deleteAll(items);
        repository.flush();
    }
}