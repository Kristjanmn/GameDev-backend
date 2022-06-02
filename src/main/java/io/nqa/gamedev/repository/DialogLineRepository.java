package io.nqa.gamedev.repository;

import io.nqa.gamedev.entity.DialogLine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DialogLineRepository extends JpaRepository<DialogLine, String> {
}
