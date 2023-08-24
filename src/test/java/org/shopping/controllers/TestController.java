package org.shopping.controllers;

import lombok.RequiredArgsConstructor;
import org.shopping.models.files.FileDownloadService;
import org.shopping.models.files.FileUploadService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/test")
@RequiredArgsConstructor
public class TestController {

    private final FileUploadService uploadService;
    private final FileDownloadService downloadService;

    @GetMapping("/upload")
    public String upload(){
        return "";
    }

    @PostMapping("/upload")
    @ResponseBody
    public void uploadPs(MultipartFile[] files, String gid, String location){
        uploadService.upload(files, gid, location);
    }

    @ResponseBody
    @GetMapping("/download/{id}")
    public void download(@PathVariable Long id){
        downloadService.download(id);
    }
}
