package com.interview.loan.projection.util.exceptions;



import com.interview.loan.projection.util.response.GenericResponse;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

import java.util.List;
import java.util.StringJoiner;

public class ErrorsUtil {
    private static ErrorsUtil instance = null;
    public static ErrorsUtil getInstance() {
        if (instance == null) {
            instance = new ErrorsUtil();
        }
        return instance;
    }

    public GenericResponse notifyErrors(Errors errors) {
        StringJoiner sj = new StringJoiner(";", "", "");
        List<FieldError> ls = errors.getFieldErrors();
        ls.stream().forEach(fieldError -> sj.add("Field " + fieldError.getField() + " " + fieldError.getDefaultMessage()));
        return new GenericResponse(-1, "Bad Request - " + sj, sj.toString(), new Object());
    }
}
