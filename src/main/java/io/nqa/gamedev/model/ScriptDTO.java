package io.nqa.gamedev.model;

import lombok.Data;

import java.util.List;

@Data
public class ScriptDTO {
    private String name;
    private List<ScriptVariableDTO> variables;
}
