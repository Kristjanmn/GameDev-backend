package io.nqa.gamedev.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "variable_index")
public class ScriptVariableIndex {

    @Id
    private String id;

    @ManyToOne
    private ScriptVariable variable;

    private int zOrder;
}
