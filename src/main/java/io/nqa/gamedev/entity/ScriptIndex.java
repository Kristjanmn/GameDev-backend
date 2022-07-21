package io.nqa.gamedev.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "script_index")
public class ScriptIndex {

    @Id
    private String id;

    @ManyToOne
    private Script script;

    private int zOrder;
}
