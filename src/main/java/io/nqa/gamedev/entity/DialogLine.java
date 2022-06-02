package io.nqa.gamedev.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "line")
public class DialogLine {

    @Id
    private String id;

    private String lineId;

    private int listOrder;

    // dialog type

    private String nextLine;

    // choices

    // speaker

    // cue
    @ManyToOne
    private Cue cue;

    // waitTime
    private float waitTime;

    private String lineText;

    @ManyToMany
    private List<Script> script;

    private boolean locked;
}
