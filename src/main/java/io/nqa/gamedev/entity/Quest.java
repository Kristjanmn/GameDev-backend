package io.nqa.gamedev.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

@Data
@Entity
@Table(name = "quest")
public class Quest {

    @Id
    private String id;

    private String questId;

    private String title;

    private String comment;

    @OneToMany
    private List<QuestPhase> phases;
}
