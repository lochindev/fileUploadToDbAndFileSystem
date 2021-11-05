package com.example.fileupload.service;

import com.example.fileupload.entity.attachment.Attachment;
import com.example.fileupload.entity.attachment.AttachmentContent;
import com.example.fileupload.reposotiry.AttachmentRepository;
import com.example.fileupload.reposotiry.AttachmentcontentReository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class AttachmentService {
        @Autowired
        AttachmentRepository attachmentRepository;

        @Autowired
        AttachmentcontentReository attachmentcontentReository;

        public List<Integer> uploadTo(MultipartHttpServletRequest request) {
              try {
                      List<Integer> photoIds = new ArrayList<>();
                      Iterator<String> fileNames = request.getFileNames();
                      List<MultipartFile> files = request.getFiles(fileNames.next());

                      if (files.size() > 0) {
                              for (MultipartFile file : files) {
                                      Attachment attachment = new Attachment();

                                      attachment.setName(file.getOriginalFilename());
                                      attachment.setSize(file.getSize());
                                      attachment.setContentType(file.getContentType());

                                      Attachment savedAttachment = attachmentRepository.save(attachment);
                                      photoIds.add(savedAttachment.getId());

                                      AttachmentContent attachmentContent = new AttachmentContent();

                                      attachmentContent.setAttachment(savedAttachment);
                                      attachmentContent.setBytes(file.getBytes());

                                      attachmentcontentReository.save(attachmentContent);


//                        return new ApiResponse("File uploaded!", true);
                              }
                      }
                      return photoIds;
              }catch(Exception e){
                      e.printStackTrace();
                      return null;
              }
//                return new ApiResponse("Error! File is not uploaded!", false);
        }


        public HttpEntity<?> download(Integer id) {
                Attachment byId = attachmentRepository.getById(id);
                AttachmentContent byAttachment = attachmentcontentReository.findByAttachment(byId);
                        return ResponseEntity.ok()
                                .contentType(MediaType.parseMediaType(byId.getContentType()))
                                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment:file=\""+ byId.getId() + "\"")
                                .body(byAttachment.getBytes());
        }
}


