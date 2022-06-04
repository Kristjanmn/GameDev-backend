package io.nqa.gamedev.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "cue")
public class Cue {

    @Id
    private String id;

    private String cueId;

    private String comment;
}
