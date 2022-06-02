package io.nqa.gamedev.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "variable")
public class ScriptVariable {

    @Id
    private String id;

    private int listOrder;

    @ManyToOne
    private VariableType variableType;

    private String variableName;
}
