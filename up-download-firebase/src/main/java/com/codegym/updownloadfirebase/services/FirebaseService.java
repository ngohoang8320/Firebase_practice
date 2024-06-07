package com.codegym.updownloadfirebase.services;

import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FirebaseService {

    String DOWNLOAD_URL = "https://firebasestorage.googleapis.com/v0/b/fir-practice-c8d2a.appspot.com/o/%s?alt=media";

    private String uploadFile(File file,
                              String fileName) throws IOException {
        BlobId blobId = BlobId.of("fir-practice-c8d2a.appspot.com",
                fileName);
        BlobInfo blobInfo = BlobInfo
                .newBuilder(blobId)
                .setContentType("media")
                .build();
        Credentials credentials = GoogleCredentials.fromStream(new FileInputStream("src\\main\\resources\\fir-practice-c8d2a-firebase-adminsdk-gin57-cd9e0e92df.json"));
        Storage storage = StorageOptions
                .newBuilder()
                .setCredentials(credentials)
                .build()
                .getService();
        storage.create(blobInfo,
                Files.readAllBytes(file.toPath()));
        return String.format(DOWNLOAD_URL,
                URLEncoder.encode(fileName,
                        StandardCharsets.UTF_8));
    }

    private File convertToFile(MultipartFile multipartFile,
                               String fileName) throws IOException {
        File tempFile = new File(fileName);
        try (FileOutputStream fos = new FileOutputStream(tempFile)) {
            fos.write(multipartFile.getBytes());
            fos.close();
        }
        return tempFile;
    }

    private String getExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf("."));
    }

    public String upload(MultipartFile multipartFile) {

        try {
            String fileName = multipartFile.getOriginalFilename();                        // to get original file name
            fileName = UUID
                    .randomUUID()
                    .toString()
                    .concat(this.getExtension(fileName));  // to generated random string values for file name.

            File file = this.convertToFile(multipartFile,
                    fileName);                      // to convert multipartFile to File
            String TEMP_URL = this.uploadFile(file,
                    fileName);                                   // to get uploaded file link
            file.delete();                                                                // to delete the copy of uploaded file stored in the project folder
            return TEMP_URL;                     // Your customized response
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    public String download(String fileName) throws IOException {
        String destFileName = UUID
                .randomUUID()
                .toString()
                .concat(this.getExtension(fileName));     // to set random strinh for destination file name
        String destFilePath = "E:\\WCode\\firebase_upload_file\\test-folder-to-download-file\\"
                              + destFileName;                                    // to set destination file path

        ////////////////////////////////   Download  ////////////////////////////////////////////////////////////////////////
        Credentials credentials = GoogleCredentials.fromStream(new FileInputStream("src\\main\\resources\\fir-practice-c8d2a-firebase-adminsdk-gin57-cd9e0e92df.json"));
        Storage storage = StorageOptions
                .newBuilder()
                .setCredentials(credentials)
                .build()
                .getService();
        Blob blob = storage.get(BlobId.of("fir-practice-c8d2a.appspot.com",
                fileName));
        blob.downloadTo(Paths.get(destFilePath));
        return "Successfully Downloaded!";
    }
}
