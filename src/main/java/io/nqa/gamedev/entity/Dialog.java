package io.nqa.gamedev.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

@Data
@Entity
@Table(name = "dialog")
public class Dialog {

    @Id
    private String id;

    private String dialogId;

    @OneToMany
    private List<DialogLine> lines;
}
