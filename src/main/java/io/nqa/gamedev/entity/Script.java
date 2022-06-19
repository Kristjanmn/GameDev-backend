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
@Table(name = "script")
public class Script {

    @Id
    private String id;

    private boolean global;

    private String name;

    @ManyToMany
    private List<ScriptVariable> variables;

    public Script(String id, boolean global, String name, List<ScriptVariable> variables) {
        this.id = id;
        this.global = global;
        this.name = name;
        this.variables = variables;
        this.comment = "";
    }

    private String comment;
}
