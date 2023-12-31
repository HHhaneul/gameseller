package org.shopping.models.games;

import lombok.RequiredArgsConstructor;
import org.shopping.commons.Utils;
import org.shopping.commons.configs.ConfigSaveService;
import org.shopping.commons.constants.GameStatus;
import org.shopping.commons.validators.RequiredValidator;
import org.shopping.controllers.admins.game.GameForm;
import org.shopping.entities.Game;
import org.shopping.models.categories.CategoryInfoService;
import org.shopping.repositories.FileInfoRepository;
import org.shopping.repositories.GameRepository;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class GameSaveService implements RequiredValidator {

    private final GameRepository gameRepository;
    private final FileInfoRepository fileInfoRepository;
    private final CategoryInfoService categoryInfoService;
    private final ConfigSaveService configSaveService;
    private final Utils utils;

    public void save(GameForm form) {
        String gid = form.getGid();
        Long gameNo = form.getGameNo();
        Game game = null;
        if (gameNo != null) {
            game = gameRepository.findById(gameNo).orElseThrow(GameNotFoundException::new);
        } else {
            game = new Game();
            game.setGid(gid); // 그룹 ID는 처음 추가할때만 저장, 그 이후는 변경 불가
        }

        game.setCategory(categoryInfoService.get(form.getCateCd()));
        game.setGameNm(form.getGameNm());
        game.setPrice(form.getPrice());
        game.setStock(form.getStock());
        game.setDescription(form.getDescription());
        game.setStatus(GameStatus.valueOf(form.getStatus()));
        System.out.println(game);
        gameRepository.saveAndFlush(game);
        form.setGameNo(game.getGameNo());

        /** 파일 업로드 완료 처리 */
        fileInfoRepository.processDone(gid);
    }
    public void saveList(GameForm form) {
        List<Integer> chks = form.getChkNo();
        for (int chk : chks) {
            String gameNo = utils.getParam("gameNo_" + chk);
            Game item = gameRepository.findById(Long.valueOf(gameNo)).orElse(null);
            item.setStatus(GameStatus.valueOf(utils.getParam("status_" + chk)));
            item.setStock(Integer.parseInt(utils.getParam("stock_" + chk)));
            item.setListOrder(Long.parseLong(utils.getParam("listOrder_" + chk)));
        }
        gameRepository.flush();
    }
}

