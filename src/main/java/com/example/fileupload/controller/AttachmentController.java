package com.example.fileupload.controller;

import com.example.fileupload.payload.response.ApiResponse;
import com.example.fileupload.service.AttachmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/api/attachment")
public class AttachmentController {
    @Autowired
    AttachmentService attachmentService;

    @PostMapping("/upload")
    public HttpEntity<?> upload(MultipartHttpServletRequest request)  {

        return ResponseEntity.ok(attachmentService.uploadTo(request));

    }

    @GetMapping("/download/{id}")
    public HttpEntity<?> download(@PathVariable Integer id){
        return attachmentService.download(id);
    }

    @PostMapping("/uploadToFileSystem")
    public HttpEntity<?> uploadToFileSystem(MultipartHttpServletRequest request) throws IOException {
        return ResponseEntity.ok(attachmentService.uploadToFileSystem(request));
    }


    @GetMapping("/dowloadFromFileSystem/{id}")
    public HttpEntity<?> dowloadFromFileSystem(@PathVariable Integer id) throws IOException {
        return attachmentService.dowloadFromFileSystem(id);
    }

}
