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

    /**
     * Respond with just object
     *
     * @param object provided object
     */
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

    /**
     * Respond with message and object
     *
     * @param message message for client
     * @param object provided object
     */
    public CustomResponse(String message, Object object) {
        if (GlobalService.isNull(object)) {
            this.success = false;
            this.message = message;
            this.object = null;
        } else {
            this.success = true;
            this.message = message;
            this.object = object;
        }
    }
}
