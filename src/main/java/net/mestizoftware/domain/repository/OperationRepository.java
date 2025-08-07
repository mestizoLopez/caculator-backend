package net.mestizoftware.domain.repository;

import net.mestizoftware.domain.model.Operation;
import net.mestizoftware.domain.model.OperationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OperationRepository extends JpaRepository<Operation, Long> {
    Optional<Operation> findByType(OperationType type);
}
