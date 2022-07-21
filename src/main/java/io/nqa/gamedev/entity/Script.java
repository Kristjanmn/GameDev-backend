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
    private List<ScriptVariableIndex> variables;

    private String comment;

    public Script(String id, boolean global, String name, List<ScriptVariableIndex> variables) {
        this.id = id;
        this.global = global;
        this.name = name;
        this.variables = variables;
        this.comment = "";
    }
}
