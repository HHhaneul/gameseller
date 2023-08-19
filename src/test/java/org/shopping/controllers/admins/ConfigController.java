package org.shopping.controllers.admins;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.shopping.commons.configs.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Log
@Controller
@RequestMapping("/admin/config")
@RequiredArgsConstructor
public class ConfigController {

    private final ConfigSaveService saveService;
    private final ConfigInfoService infoService;
    private String code = "siteConfig";

    @GetMapping
    public String config(Model model) {
        commonProcess(model);
        ConfigForm configForm = infoService.get(code, org.shopping.controllers.admins.ConfigForm.class);

        model.addAttribute("configForm", configForm == null ? new ConfigForm() : configForm);
        return "admin/config";
    }

    @PostMapping
    public String configPs(ConfigForm configForm, Model model) {
        commonProcess(model);

        saveService.save(code, configForm);

        model.addAttribute("message", "설정이 저장되었습니다.");

        return "admin/config";
    }

    private void commonProcess(Model model) {
        String title = "사이트 설정";
        String menuCode = "config";
        model.addAttribute("pageTitle", title);
        model.addAttribute("title", title);
        model.addAttribute("menuCode", menuCode);
    }
}