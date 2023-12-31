package com.interview.loan.projection.service.impl;

import com.interview.loan.projection.pojo.FeesProjectionRequest;
import com.interview.loan.projection.pojo.InstallmentProjectionRequest;
import com.interview.loan.projection.service.LoanService;
import com.interview.loan.projection.util.Literals;
import com.interview.loan.projection.util.enums.ResponseStatus;
import com.interview.loan.projection.util.response.GenericResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class LoanServiceImpl implements LoanService {

    @Autowired
    Environment env;

    SimpleDateFormat formatter = new SimpleDateFormat("dd/mm/yyyy", Locale.ENGLISH);

    /**
     * @param feesProjectionRequest
     * @return
     */
    @Override
    public ResponseEntity<Object> getFeeProjection(FeesProjectionRequest feesProjectionRequest) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            Map<String, Object> valid = validDuration(feesProjectionRequest.getDuration(), feesProjectionRequest.getFrequency());
            if (!valid.get(Literals.STATUS).equals(true)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(GenericResponse.GenericResponseData.builder()
                                .status(ResponseStatus.FAILED.getStatus())
                                .message(String.valueOf(valid.get(Literals.MESSAGE)))
                                .msgDeveloper(String.valueOf(valid.get(Literals.MSG_DEVELOPER)))
                                .build());
            }

            Date startDate = formatter.parse(feesProjectionRequest.getStartDate());
            Date newDate = startDate;

            String frequency = feesProjectionRequest.getFrequency().toLowerCase();
            FeesProjectionRequest.LoanConfigurations loanConfig = this.getLoanConfigs(frequency);


            Double interestAmt = feesProjectionRequest.getPrincipalAmount() * (loanConfig.getInterestRate() / 100);

            Double fees = feesProjectionRequest.getPrincipalAmount() * (loanConfig.getFeeRate() / 100);
            Double maxFees = loanConfig.getMaxFees();

            int periodicFeesCount = loanConfig.getPeriodicFeesCount();

            int x = 1;
            for (int i = 0; i < feesProjectionRequest.getDuration(); i++) {
                stringBuilder.append(formatter.format(newDate) + " => " + Math.round(interestAmt) + " ");
                if (x == periodicFeesCount) {
                    stringBuilder.append(formatter.format(newDate) + " => " + Math.round(Math.min(fees, maxFees)) + " ");
                    x = 0;
                }
                newDate = dateAdd(frequency, newDate);
                x++;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(GenericResponse.GenericResponseData.builder()
                            .status(ResponseStatus.FAILED.getStatus())
                            .message("Something went wrong, try again later.")
                            .msgDeveloper(e.getMessage())
                            .build());
        }
        Map<String, Object> resp = new HashMap<>();
        resp.put("data", stringBuilder);

        return ResponseEntity.ok(resp);
    }

    /**
     * @param installmentProjectionRequest
     * @return
     */
    @Override
    public ResponseEntity<Object> getInstallmentProjection(InstallmentProjectionRequest installmentProjectionRequest) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            Map<String, Object> valid = validDuration(installmentProjectionRequest.getDuration(), installmentProjectionRequest.getFrequency());
            if (!valid.get(Literals.STATUS).equals(true)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(GenericResponse.GenericResponseData.builder()
                                .status(ResponseStatus.FAILED.getStatus())
                                .message(String.valueOf(valid.get(Literals.MESSAGE)))
                                .msgDeveloper(String.valueOf(valid.get(Literals.MSG_DEVELOPER)))
                                .build());
            }


            Date startDate = formatter.parse(installmentProjectionRequest.getStartDate());
            Date newDate = startDate;

            String frequency = installmentProjectionRequest.getFrequency().toLowerCase();
            FeesProjectionRequest.LoanConfigurations loanConfig = this.getLoanConfigs(frequency);

            Double interestAmt = installmentProjectionRequest.getPrincipalAmount() * (loanConfig.getInterestRate() / 100);

            Double fees = installmentProjectionRequest.getPrincipalAmount() * (loanConfig.getFeeRate() / 100);
            Double maxFees = loanConfig.getMaxFees();

            int periodicFeesCount = loanConfig.getPeriodicFeesCount();
            Double installment = installmentProjectionRequest.getPrincipalAmount() / installmentProjectionRequest.getDuration();

            int x = 1;

            for (int i = 0; i < installmentProjectionRequest.getDuration(); i++) {
                Double chargedFees = 0D;
                newDate = dateAdd(frequency, newDate);

                if (x == periodicFeesCount) {
                    chargedFees = Math.min(fees, maxFees);
                    x = 0;
                }
                Double totalAmount = Double.sum(Double.sum(installment, interestAmt), chargedFees);
                stringBuilder.append(formatter.format(newDate) + " => " + Math.round(totalAmount) + " ");

                x++;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(GenericResponse.GenericResponseData.builder()
                            .status(ResponseStatus.FAILED.getStatus())
                            .message("Something went wrong, try again later.")
                            .msgDeveloper(e.getMessage())
                            .build());
        }
        Map<String, Object> resp = new HashMap<>();
        resp.put("data", stringBuilder);

        return ResponseEntity.ok(resp);
    }


    private Map<String, Object> validDuration(Integer duration, String frequency) {
        Map<String, Object> resp = new HashMap<>();

        if (frequency.equals("w") && duration > 4) {
            resp.put(Literals.STATUS, false);
            resp.put(Literals.MESSAGE, "For Weekly frequency duration cannot be greater than 4.");
            resp.put(Literals.MSG_DEVELOPER, "Validation error: For Weekly frequency duration cannot be greater than 4.");
            return resp;
        } else if (frequency.equals("m") && duration > 12) {
            resp.put(Literals.STATUS, false);
            resp.put(Literals.MESSAGE, "For Monthly frequency duration cannot be greater than 12.");
            resp.put(Literals.MSG_DEVELOPER, "Validation error: For Weekly frequency duration cannot be greater than 12.");
            return resp;
        }

        resp.put(Literals.STATUS, true);
        return resp;
    }


    public Date dateAdd(String frequency, Date date) {
        Calendar cal = Calendar.getInstance();
        Integer duration;
        if (!Objects.isNull(date)) {
            cal.setTime(date);
        }
        duration = frequency.equals("w") ? 7 : 30;
        cal.add(Calendar.DATE, duration);
        return cal.getTime();
    }

    private FeesProjectionRequest.LoanConfigurations getLoanConfigs(String frequency) {
        FeesProjectionRequest.LoanConfigurations loanConfigurations = new FeesProjectionRequest.LoanConfigurations();
        loanConfigurations.setInterestRate(Double.parseDouble(env.getProperty("app.loan.interest.rate." + frequency)));
        loanConfigurations.setFeeRate(Double.valueOf(env.getProperty("app.loan.service.fee")));
        loanConfigurations.setPeriodicFeesCount(Integer.parseInt(env.getProperty("app.loan.periodic.fees." + frequency)));
        loanConfigurations.setMaxFees(Double.valueOf(env.getProperty("app.loan.fees.max." + frequency)));
        return loanConfigurations;
    }
}
