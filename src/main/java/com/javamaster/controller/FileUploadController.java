package com.javamaster.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@RestController
public class FileUploadController {

    @RequestMapping(value = "/file/upload", method = RequestMethod.POST)
    public ResponseEntity<String> uploadFile(@RequestParam("name") String name,
                                             @RequestParam("file") MultipartFile file){
        if(!file.isEmpty()){
            try {
                byte[] bytes = file.getBytes();
                BufferedOutputStream oStream = new BufferedOutputStream(
                        new FileOutputStream(new File("C:\\SomethingWrong\\src\\main\\resources\\image\\", file.getOriginalFilename()))
                );
                oStream.write(bytes);
                oStream.close();

                return new ResponseEntity<>("File is uploaded", HttpStatus.OK);
            } catch (IOException e) {
                return new ResponseEntity<String>("File is not upload cuz' " + e.getMessage(),
                        HttpStatus.CONFLICT);
            }
        }
        else
            return new ResponseEntity<String>("File is not upload cuz' file is empty", HttpStatus.NO_CONTENT);

    }
}
