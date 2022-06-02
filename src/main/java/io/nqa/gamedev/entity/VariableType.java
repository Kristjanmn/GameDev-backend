package io.nqa.gamedev.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "type")
public class VariableType {

    @Id
    private String id;

    private String type;
}
