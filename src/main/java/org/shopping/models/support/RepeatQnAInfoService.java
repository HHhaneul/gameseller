package org.shopping.models.support;

import lombok.RequiredArgsConstructor;
import org.shopping.entities.RepeatedQnA;
import org.shopping.repositories.RepeatedQnARepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RepeatQnAInfoService {
    private final RepeatedQnARepository repository;

    public RepeatedQnA get(Long _id) {

        return repository.findById(_id).orElse(null);
    }

    /* 전체 목록 조회 */
    public List<RepeatedQnA> getList(String mode) {

        return repository.getList(mode);
    }


    public List<RepeatedQnA> getListAll() {
        return repository.getList("all");
    }

    public List<RepeatedQnA> myGetList(){

        return null;
    }
}
