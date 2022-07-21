package io.nqa.gamedev.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "line")
public class DialogLine {

    @Id
    private String id;

    private String lineId;

    //private int listOrder;

    // dialog type

    private String nextLine;

    // choices
    //@ManyToMany
    // choice zIndex, choice dialogId
    //private Map<Integer, String> choices;
    @ElementCollection
    private List<String> choices;       // will most likely turn into infinite loop very shortly.
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
    private List<ScriptIndex> script;

    private boolean locked;

    private String comment;

    private int zOrder;
}
