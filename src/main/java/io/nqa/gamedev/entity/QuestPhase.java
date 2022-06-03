package io.nqa.gamedev.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.List;

@Data
@Entity
@Table(name = "phase")
public class QuestPhase {

    @Id
    private String id;

    private String phaseId;

    private String description;

    @ManyToMany
    private List<Script> script;

    private String comment;
}
