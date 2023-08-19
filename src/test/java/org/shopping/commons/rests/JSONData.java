package org.shopping.commons.rests;

import lombok.*;
import org.springframework.http.HttpStatus;

@Data @Builder
@NoArgsConstructor @AllArgsConstructor
public class JSONData<T> {
    private boolean success;
    private HttpStatus status = HttpStatus.OK; // 200
    private String message;
    private T data;
}