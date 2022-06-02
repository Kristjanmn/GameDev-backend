package io.nqa.gamedev.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "phase")
public class QuestPhase {

    @Id
    private String id;

    private String phaseId;
}
