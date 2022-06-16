package ru.gb.storage.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.gb.storage.services.FileStoreService;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

@RestController
@CrossOrigin
public class FilesController {

    private FileStoreService fileStoreService;

    @Autowired
    public void setFileStoreService(FileStoreService fileStoreService) {
        this.fileStoreService = fileStoreService;
    }

    @PostMapping("/storefile")
    public ResponseEntity<String> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam("subtype") int subType
    ) throws IOException, NoSuchAlgorithmException, InterruptedException {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("File is empty");
        }
        String fileHash = fileStoreService.storeFile(file.getBytes(), file.getOriginalFilename(), subType);
        return ResponseEntity.ok(fileHash);
    }

    @GetMapping("/getfile")
    public ResponseEntity<Resource> downloadFile(@RequestParam("hash") UUID hash, @RequestParam("filename") String userFilename) throws IOException {
        byte[] array = fileStoreService.getFile(hash, userFilename);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(new ByteArrayResource(array));
    }

    @GetMapping("/getfiles")
    public ResponseEntity<?> downloadFile(@RequestParam("subtype") int subtype) throws IOException {
        return ResponseEntity.ok(fileStoreService.getMetaFiles(subtype));
    }

    @DeleteMapping("/deletefile")
    public int deleteFile(@RequestParam("hash") UUID hash, @RequestParam("filename") String userFilename) throws IOException {
        fileStoreService.deleteFile(hash, userFilename);
        return HttpStatus.OK.value();
    }
}
