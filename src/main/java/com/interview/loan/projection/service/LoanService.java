package com.interview.loan.projection.service;

import com.interview.loan.projection.pojo.FeesProjectionRequest;
import com.interview.loan.projection.pojo.InstallmentProjectionRequest;

import org.springframework.http.ResponseEntity;

public interface LoanService {
    ResponseEntity<Object> getFeeProjection(FeesProjectionRequest feesProjectionRequest);

    ResponseEntity<Object> getInstallmentProjection(InstallmentProjectionRequest installmentProjectionRequest);
}
