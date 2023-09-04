package org.shopping;

import org.springframework.ui.Model;

public interface CommonProcess {
    default void commonProcess(Model model, String pageTitle){
        model.addAttribute("pageTitle", pageTitle);
    }
}
