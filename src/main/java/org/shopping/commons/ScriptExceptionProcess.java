package org.shopping.commons;

import jakarta.servlet.http.HttpServletResponse;
import org.shopping.commons.exception.AlertBackException;
import org.shopping.commons.exception.AlertException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;

public interface ScriptExceptionProcess {

    @ExceptionHandler(AlertException.class)
    default String scriptHandler(AlertException e, Model model, HttpServletResponse response) {

        response.setStatus(e.getStatus().value());
        String script = String.format("alert('%s');", e.getMessage());
        if (e instanceof AlertBackException) {
            script += "history.back();";
        }

        model.addAttribute("script", script);

        return "commons/_execute_script";
    }
}