package org.shopping.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;


@Data @Builder @Entity
@NoArgsConstructor @AllArgsConstructor
@Table(indexes = {
        @Index(name = "idx_fileInfo_gid", columnList = "gid"),
        @Index(name = "idx_fileInfo_gid_location", columnList = "gid, location")
})
public class FileInfo extends BaseMemberEntity {

    @Id @GeneratedValue
    private Long id;

    @Column(length = 45, nullable = false)
    private String gid = UUID.randomUUID().toString();

    /* 저장소 위치 */
    @Column(length = 45)
    private String location;

    /* 파일명 */
    @Column(length = 100, nullable = false)
    private String fileName;

    @Column(length = 45)
    private String extension;

    /* 파일 유형 */
    @Column(length = 65)
    private String fileType;

    /* 작업 완료 여부 */
    private boolean done;

    /* 파일 업로드 경로 */
    @Transient
    private String filePath;

    /* 파일 URL */
    @Transient
    private String fileUrl;

    /* 썸네일 이미지 경로 */
    @Transient
    private String[] thumbsPath;

    /* 썸네일 이미지 접속 URL */
    @Transient
    private String[] thumbsUrl;
}
