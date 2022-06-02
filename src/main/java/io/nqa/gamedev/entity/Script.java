package io.nqa.gamedev.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "script")
public class Script {

    @Id
    private String id;

    private String name;

    @ManyToMany
    private List<ScriptVariable> variables;
}
