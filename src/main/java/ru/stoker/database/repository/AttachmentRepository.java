package ru.stoker.database.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.stoker.database.entity.Attachment;

public interface AttachmentRepository extends JpaRepository<Attachment, Long> {
}
