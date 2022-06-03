package io.nqa.gamedev.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "project")
public class Project {

    @Id
    private String id;

    // This is different from 'id' above, it can be changed from
    // project settings and can be used to access project from web.
    private String projectId;

    private String title;

    @OneToMany
    private List<Quest> quests;

    @OneToMany
    private List<Dialog> dialogs;

    @OneToMany
    private List<Script> scripts;

    @OneToMany
    private List<Cue> cues;
}
