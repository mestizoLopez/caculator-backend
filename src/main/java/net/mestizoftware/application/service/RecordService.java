package net.mestizoftware.application.service;

import lombok.RequiredArgsConstructor;
import net.mestizoftware.domain.model.Record;
import net.mestizoftware.domain.model.User;
import net.mestizoftware.domain.repository.RecordRepository;
import net.mestizoftware.web.dto.RecordDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RecordService {

    private final RecordRepository recordRepository;

    public Page<RecordDto> findByUserAndDeletedFalse(User user, Pageable pageable) {
        Page<Record> records = recordRepository.findByUserAndDeletedFalse(user, pageable);
        if (records.isEmpty()) {
            throw new IllegalArgumentException("No records found for the user");
        }

        return records.map(record ->
            RecordDto.builder()
                .id(record.getId())
                .username(record.getUser().getUsername())
                .operation(record.getOperation().getType())
                .amount(record.getAmount())
                .userBalance(record.getUserBalance())
                .operationResponse(record.getOperationResponse())
                .createdAt(record.getCreatedAt())
                .build()
        );

    }

    public Boolean softDeleteRecord(Long id, Long userId) {
        recordRepository.findById(id)
                .filter(record -> record.getUser().getId().equals(userId))
                        .map(record -> {
                            record.setDeleted(true);
                            recordRepository.save(record);
                            return true;
                        });
        return false;
    }

}
