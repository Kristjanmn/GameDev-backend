package io.nqa.gamedev.model;

import io.nqa.gamedev.service.global.GlobalService;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CustomResponse {
    private boolean success;
    private String message;
    private Object object;

    public CustomResponse(Object object) {
        if (GlobalService.isNull(object)) {
            this.success = false;
            this.message = "failure";
            this.object = null;
        } else {
            this.success = true;
            this.message = "success";
            this.object = object;
        }
    }
}
