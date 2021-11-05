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

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

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

        public String uploadToFileSystem(MultipartHttpServletRequest request) throws IOException {
                Iterator<String> fileNames = request.getFileNames();
                MultipartFile file = request.getFile(fileNames.next());
                if (file != null) {
                        Attachment attechment = new Attachment();
                        attechment.setName(file.getOriginalFilename());
                        attechment.setContentType(file.getContentType());
                        attechment.setSize(file.getSize());
                        attechment = attachmentRepository.save(attechment);
                        String[] split = file.getOriginalFilename().split("\\.");
                        String name = UUID.randomUUID()+"_"+file.getOriginalFilename(); // + "." + split[split.length - 1] // .png
                        attechment.setFileName(name);
                        Path path = Paths.get("E:\\new files\\" + name);
                        Files.copy(file.getInputStream(), path);
                        attechment.setFilePath(path.toString());
                        attachmentRepository.save(attechment);
                        return "Saved";
                }
                return null;
        }



        public HttpEntity<?> dowloadFromFileSystem(Integer id) throws IOException {
                Attachment byId = attachmentRepository.getById(id);
                File file=new File(byId.getFilePath());
                byte[] bytes = Files.readAllBytes(file.toPath());
                return ResponseEntity.ok()
                        .contentType(MediaType.parseMediaType(byId.getContentType()))
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + byId.getName() + "\"")
                        .body(bytes);
        }
}


