package com.interview.loan.projection.controller;

import com.interview.loan.projection.pojo.FeesProjectionRequest;
import com.interview.loan.projection.pojo.InstallmentProjectionRequest;
import com.interview.loan.projection.service.LoanService;
import com.interview.loan.projection.util.exceptions.ErrorsUtil;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class LoanController {

    @Autowired
    private Validator validator;

    @Autowired
    LoanService loanService;

    @PostMapping(path = "/fee/projection")
    public ResponseEntity<Object> getFeeProjection(HttpServletRequest request, @RequestBody FeesProjectionRequest feesProjectionRequest, Errors errors) {
        validator.validate(feesProjectionRequest, errors);
        if (errors.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorsUtil.getInstance().notifyErrors(errors));
        }

        return loanService.getFeeProjection(feesProjectionRequest);
    }

    @PostMapping(path = "/installment/projection")
    public ResponseEntity<Object> getInstallmentProjection(HttpServletRequest request, @RequestBody InstallmentProjectionRequest installmentProjectionRequest, Errors errors) {
        validator.validate(installmentProjectionRequest, errors);
        if (errors.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorsUtil.getInstance().notifyErrors(errors));
        }

        return loanService.getInstallmentProjection(installmentProjectionRequest);
    }

}
