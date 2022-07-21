package io.nqa.gamedev.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "variable")
public class ScriptVariable {

    @Id
    private String id;

    private String variableType;

    private String variableName;
}
