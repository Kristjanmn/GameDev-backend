package io.nqa.gamedev.repository;

import io.nqa.gamedev.entity.Dialog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DialogRepository extends JpaRepository<Dialog, String> {

    Optional<Dialog> findByDialogIdEquals(String dialogId);
}
