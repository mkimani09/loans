package com.interview.loan.projection.util.response;

import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;

@Getter
@Builder
public class GenericResponse implements Serializable {

    private static final long serialVersionUID = 11223344556677889L;

    private int status;
    private String message;
    private String msgDeveloper;

    private Object data;

    public GenericResponse() {
        super();
    }

    public GenericResponse(int status, String message, String msgDeveloper, Object data) {
        super();
        this.status = status;
        this.message = message;
        this.msgDeveloper = msgDeveloper;
        this.data = data;

    }

    @Getter
    @Builder
    public static class GenericResponseData {
        private int status;
        private String message;
        private String msgDeveloper;

        private Object data;


//        public GenericResponseData {
//            super();
//        }

        public GenericResponseData(int status, String message, String msgDeveloper, Object data) {
            super();
            this.status = status;
            this.message = message;
            this.msgDeveloper = msgDeveloper;
            this.data = data;

        }
    }
}
