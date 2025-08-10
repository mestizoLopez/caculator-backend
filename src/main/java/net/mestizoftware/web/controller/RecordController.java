package net.mestizoftware.web.controller;

import lombok.RequiredArgsConstructor;
import net.mestizoftware.application.service.RecordService;
import net.mestizoftware.domain.model.User;
import net.mestizoftware.web.dto.RecordDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = {"{http://localhost:4200"
        ,"http://calculator-backend.s3-website-us-east-1.amazonaws.com/"}, allowCredentials = "true")
@RequestMapping("/api/v1/records")
@RequiredArgsConstructor
public class RecordController {

    private final RecordService recordService;

    @GetMapping
    public ResponseEntity<Page<RecordDto>> getUserRecords(
            @AuthenticationPrincipal User user,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy
    ) {
        PageRequest pageable = PageRequest.of(page, size, Sort.by(sortBy).descending());
        Page<RecordDto> dtos = recordService.findByUserAndDeletedFalse(user, pageable);

        return ResponseEntity.ok(dtos);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> softDelete(@PathVariable Long id, @AuthenticationPrincipal User user) {
        return  recordService.softDeleteRecord(id, user.getId()) == true
                ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

}
