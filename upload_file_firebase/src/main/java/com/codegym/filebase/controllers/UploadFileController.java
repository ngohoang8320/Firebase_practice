package com.codegym.filebase.controllers;

import com.codegym.filebase.services.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/app")
public class UploadFileController {

    private final FileService fileService;

    @PostMapping
    public String upload(@RequestParam("file") MultipartFile multipartFile) {
        return fileService.upload(multipartFile);
    }
}
