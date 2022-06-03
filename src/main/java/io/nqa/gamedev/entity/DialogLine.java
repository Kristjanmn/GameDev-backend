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
    @ManyToMany
    private List<DialogLine> choices;       // will most likely turn into infinite loop very shortly.
                                            // It does not like List<String>, and I can't
                                            // think of anything else at this time.

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

    private String comment;
}
