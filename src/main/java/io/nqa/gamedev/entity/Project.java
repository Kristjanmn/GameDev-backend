package io.nqa.gamedev.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "project")
public class Project {

    @Id
    private String id;

    private String title;

    @OneToMany
    private List<Quest> quests;

    @OneToMany
    private List<Dialog> dialogs;

    @OneToMany
    private List<Script> scripts;
}
