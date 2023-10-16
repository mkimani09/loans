package com.interview.loan.projection.pojo;


import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.*;

@Data
@RequiredArgsConstructor
public class InstallmentProjectionRequest {

    @NotNull
    @Min(value = 1)
    private Integer duration;

    @Size(max = 1, message = "Field cannot exceed 1 character, should either w or m, i.e weekly or monthly")
    @Pattern(regexp = "(^[w|m]$)", message = "must contain either w(weekly) or m(monthly).")
    @NotNull
    private String frequency;

    @NotNull
    @Min(value = 1)
    @Max(value = 1000000)
    private Double principalAmount;

    @NotNull
    private String startDate; //01/06/2023 //dd/mm/yyyy

}
