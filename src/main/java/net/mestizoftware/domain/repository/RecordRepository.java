package net.mestizoftware.domain.repository;

import net.mestizoftware.domain.model.Record;
import net.mestizoftware.domain.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecordRepository extends JpaRepository<Record, Long> {
    Page<Record> findByUserAndDeletedFalse(User user, Pageable pageable);
}
