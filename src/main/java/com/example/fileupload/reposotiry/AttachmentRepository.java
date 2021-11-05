package com.example.fileupload.reposotiry;

import com.example.fileupload.entity.attachment.Attachment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttachmentRepository extends JpaRepository<Attachment, Integer> {

}
