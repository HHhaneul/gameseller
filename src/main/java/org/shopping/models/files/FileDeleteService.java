package org.shopping.models.files;

import lombok.RequiredArgsConstructor;
import org.shopping.commons.exception.AuthorizationException;
import org.shopping.commons.MemberUtil;
import org.shopping.entities.FileInfo;
import org.shopping.models.member.MemberInfo;
import org.shopping.repositories.FileInfoRepository;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Arrays;

@Service
@RequiredArgsConstructor
public class FileDeleteService {
    private final MemberUtil memberUtil;
    private final FileInfoService infoService;
    private final FileInfoRepository repository;

    public void delete(Long id) {
        FileInfo item = infoService.get(id);
        /** 파일 삭제 권한 체크 S */
        String createdBy = item.getCreatedBy(); // 파일 업로드한 사용자 아이디
        MemberInfo member = memberUtil.getMember();
        if (createdBy != null && !createdBy.isBlank() && !memberUtil.isAdmin()
                && (!memberUtil.isLogin()
                || (memberUtil.isLogin() && !member.getUserId().equals(createdBy)))) {

            throw new AuthorizationException("UnAuthorized.delete.file");
        }
        /** 파일 삭제 권한 체크 E */

        /**
         * 1. 파일 삭제
         * 2. thumbs 삭제
         * 3. 파일 정보 삭제
         */

        File file = new File(item.getFilePath());
        if (file.exists()) file.delete();

        String[] thumbsPath = item.getThumbsPath();
        if (thumbsPath != null && thumbsPath.length > 0) {
            Arrays.stream(thumbsPath).forEach(p -> {
                File thumb = new File(p);
                if (thumb.exists()) thumb.delete();
            });
        }

        repository.delete(item);
        repository.flush();
    }
}
