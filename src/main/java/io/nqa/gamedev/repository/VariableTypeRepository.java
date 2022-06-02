package io.nqa.gamedev.repository;

import io.nqa.gamedev.entity.VariableType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VariableTypeRepository extends JpaRepository<VariableType, String> {
}
