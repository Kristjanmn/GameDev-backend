package io.nqa.gamedev.entity.global;

import io.nqa.gamedev.entity.ScriptVariable;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.List;

/**
 * Global scripts are needed for basic functions of this system. Without them it will NOT work.
 */
@Data
@Entity
@Table(name = "global_script")
public class GlobalScript {

    @Id
    private String id;

    private String name;

    @ManyToMany
    private List<ScriptVariable> variables;
}
