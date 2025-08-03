package net.mestizoftware.domain.repository;

import net.mestizoftware.domain.model.Operation;
import net.mestizoftware.domain.model.OperationType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OperationRepository extends JpaRepository<Operation, Long> {
    Optional<Operation> findByType(OperationType type);
}
