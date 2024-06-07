package com.codegym.updownloadfirebase.controllers;

import com.codegym.updownloadfirebase.services.FirebaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/firebase")
public class FirebaseController {
    FirebaseService firebaseService = new FirebaseService();

    @PostMapping("/profile/pic")
    public Object upload(@RequestParam("file") MultipartFile multipartFile) {
//        logger.info("HIT -/upload | File Name : {}",
//                multipartFile.getOriginalFilename());
        return firebaseService.upload(multipartFile);
    }

    @PostMapping("/profile/pic/{fileName}")
    public Object download(@PathVariable String fileName) throws IOException {
//        logger.info("HIT -/download | File Name : {}",
//                fileName);
        return firebaseService.download(fileName);
    }
}
