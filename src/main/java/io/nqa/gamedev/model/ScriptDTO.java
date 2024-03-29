package io.nqa.gamedev.model;

import lombok.Data;

import java.util.List;

@Data
public class ScriptDTO {
    String id;
    boolean global;
    String name;
    List<ScriptVariableDTO> variables;
    String comment;
}
