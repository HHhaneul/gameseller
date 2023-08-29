package org.shopping.models.games;

import lombok.RequiredArgsConstructor;
import org.shopping.commons.constants.GameStatus;
import org.shopping.controllers.admins.game.GameForm;
import org.shopping.entities.Game;
import org.shopping.models.categories.CategoryInfoService;
import org.shopping.repositories.FileInfoRepository;
import org.shopping.repositories.GameRepository;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class GameSaveService {

    private final GameRepository gameRepository;
    private final FileInfoRepository fileInfoRepository;
    private final CategoryInfoService categoryInfoService;

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

        String cateCd = form.getCateCd();
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
}

