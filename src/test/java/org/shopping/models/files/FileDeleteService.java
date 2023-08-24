package org.shopping.models.files;

import lombok.RequiredArgsConstructor;
import org.shopping.commons.MemberUtil;
import org.shopping.entities.FileInfo;
import org.shopping.models.member.MemberInfo;
import org.shopping.repositories.FileInfoRepository;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Arrays;

@Service
@RequiredArgsConstructor
public class FileDeleteService {
    private final MemberUtil memberUtil;
    private final FileInfoService infoService;
    private final FileInfoRepository repository;

    public void delete(Long id){
        FileInfo item = infoService.get(id);

        /* 파일 삭제 권한 체크 S - 업로드한 사용자 아이디 */
        String createdBy = item.getCreatedBy();
        MemberInfo memberInfo = memberUtil.getMember();
        if (createdBy != null
                && !createdBy.isBlank()
                && !memberUtil.isAdmin()
                && (!memberUtil.isLogin()
                || (memberUtil.isLogin() &&
                memberInfo.getUserId().equals(createdBy)))){

            throw new AuthorizationServiceException("UnAuthorized.delete.file");
        }

        /* 파일 삭제 권한 체크 E */

        /**
         * 1. 파일 삭제
         * 2. thumbs 삭제
         * 3. 파일 정보 삭제
         */

        /* 파일 삭제 */
        File file = new File(item.getFilePath());
        if (file.exists()) file.delete();

        /* thumbs 삭제 */
        String[] thumbsPath = item.getThumbsPath();
        if (thumbsPath != null && thumbsPath.length > 0){
            Arrays.stream(thumbsPath).forEach(p -> {
                File thumb = new File(p);
                if (thumb.exists()) thumb.delete();
            });
        }

        /* 파일 정보 삭제 */
        repository.delete(item);
        repository.flush();
    }
}
