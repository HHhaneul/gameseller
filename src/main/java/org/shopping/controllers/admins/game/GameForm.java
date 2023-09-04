package org.shopping.controllers.admins.game;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.shopping.commons.constants.GameStatus;
import org.shopping.entities.FileInfo;

import java.util.List;
import java.util.UUID;

@Data @Builder
@NoArgsConstructor @AllArgsConstructor
public class GameForm {

    private String mode;

    private Long gameNo;

    private String cateCd;

    @NotBlank
    private String gid = UUID.randomUUID().toString();

    @NotBlank
    private String gameNm;

    private int price;
    private int stock;

    private String status = GameStatus.READY.name();

    private String description;

    private long listOrder;

    /* 상품 메인 이미지 */
    private List<FileInfo> mainImages;

    /* 목록 이미지 */
    private List<FileInfo> listImages;

    /* 에디터 이미지 */
    private List<FileInfo> editorImages;

    private List<Integer> chkNo;
}