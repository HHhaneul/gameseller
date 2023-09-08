package org.shopping.models.files;

import org.shopping.commons.exception.CommonException;
import org.springframework.http.HttpStatus;

public class FileNotFoundException extends CommonException {

    public FileNotFoundException() {
        super(bundleError.getString("NotFound.file"), HttpStatus.BAD_REQUEST);
    }
}
