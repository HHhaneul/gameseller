package org.shopping.models.files;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.shopping.entities.FileInfo;
import org.springframework.stereotype.Service;

import java.io.*;

@Service
@RequiredArgsConstructor
public class FileDownloadService {

    private final HttpServletResponse response;
    private final FileInfoService infoService;

    public void download(Long id){
        FileInfo item = infoService.get(id);
        String filePath = item.getFilePath();
        File file = new File(filePath);
        if (!file.exists()) throw new FileNotFoundException();

        String fileName = item.getFilename();
        try {
            fileName = new String(fileName.getBytes(), "ISO8859_1");

        } catch (UnsupportedEncodingException e) {}

        try(FileInputStream fis = new FileInputStream(file);
            BufferedInputStream bis = new BufferedInputStream(fis)) {

            OutputStream out = response.getOutputStream();

            response.setHeader("Content-Disposition", "attachment; + fileName=" + fileName);
            response.setHeader("Content-Type", "application/octet-stream");
            response.setHeader("Cache-Control", "must-revalidate");
            response.setHeader("Pragma", "public");
            response.setIntHeader("Expires", 0);
            response.setHeader("Content-length", "" + file.length());

            while (bis.available() > 0){
                out.write(bis.read());

            }
            out.flush();

        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
