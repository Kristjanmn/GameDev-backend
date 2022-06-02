package io.nqa.gamedev.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CustomResponse {
    private boolean success;
    private String message;
    private Object object;
}
