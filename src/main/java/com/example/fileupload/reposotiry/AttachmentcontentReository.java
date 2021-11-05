package com.example.fileupload.reposotiry;

import com.example.fileupload.entity.attachment.Attachment;
import com.example.fileupload.entity.attachment.AttachmentContent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttachmentcontentReository extends JpaRepository<AttachmentContent,Integer> {
        AttachmentContent findByAttachment(Attachment attachment);

}
