package net.mestizoftware.web.controller;

import lombok.RequiredArgsConstructor;
import net.mestizoftware.domain.model.Record;
import net.mestizoftware.domain.model.User;
import net.mestizoftware.domain.repository.RecordRepository;
import net.mestizoftware.web.dto.RecordDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/records")
@RequiredArgsConstructor
public class RecordController {

    private final RecordRepository recordRepository;

    @GetMapping
    public ResponseEntity<Page<RecordDto>> getUserRecords(
            @AuthenticationPrincipal User user,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy
    ) {
        PageRequest pageable = PageRequest.of(page, size, Sort.by(sortBy).descending());
        Page<Record> records = recordRepository.findByUserAndDeletedFalse(user, pageable);

        Page<RecordDto> dtos = records.map(record ->
                new RecordDto(
                        record.getId(),
                        record.getOperation().getType(),
                        record.getAmount(),
                        record.getUserBalance(),
                        record.getOperationResponse(),
                        record.getCreatedAt()
                )
        );

        return ResponseEntity.ok(dtos);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> softDelete(@PathVariable Long id, @AuthenticationPrincipal User user) {
        return recordRepository.findById(id)
                .filter(r -> r.getUser().getId().equals(user.getId()))
                .map(r -> {
                    r.setDeleted(true);
                    recordRepository.save(r);
                    return ResponseEntity.ok().build();
                })
                .orElse(ResponseEntity.notFound().build());
    }

}
