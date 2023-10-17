package com.interview.loan.projection.Controller;

import com.google.gson.Gson;
import com.interview.loan.projection.pojo.FeesProjectionRequest;
import com.interview.loan.projection.pojo.InstallmentProjectionRequest;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class LoanControllerTest {

    @Autowired
    private MockMvc mockMvc;

    /**
     * Fees Tests
     * @throws Exception
     */
    @Test
    public void getFeesProjectionEmptyRequestTest() throws Exception {
        FeesProjectionRequest feesProjectionRequest = new FeesProjectionRequest();
        String json = new Gson().toJson(feesProjectionRequest);
        this.mockMvc.perform(post("/api/fee/projection").contentType(MediaType.APPLICATION_JSON).content(json))
                .andDo(print())
                .andExpect(status().isBadRequest());

    }

    @Test
    public void getFeesProjectionInvalidDurationTest() throws Exception {
        FeesProjectionRequest feesProjectionRequest = new FeesProjectionRequest();
        feesProjectionRequest.setDuration(5);
        feesProjectionRequest.setFrequency("w");
        feesProjectionRequest.setPrincipalAmount(1000D);
        feesProjectionRequest.setStartDate("01/06/2023");
        String json = new Gson().toJson(feesProjectionRequest);
        this.mockMvc.perform(post("/api/fee/projection").contentType(MediaType.APPLICATION_JSON).content(json))
                .andDo(print())
                .andExpect(status().isBadRequest());

    }

    @Test
    public void getFeeProjectionWeeklyTest() throws Exception {
        FeesProjectionRequest feesProjectionRequest = new FeesProjectionRequest();
        feesProjectionRequest.setDuration(3);
        feesProjectionRequest.setFrequency("w");
        feesProjectionRequest.setPrincipalAmount(3000D);
        feesProjectionRequest.setStartDate("01/06/2023");
        String json = new Gson().toJson(feesProjectionRequest);
        MvcResult mvcResult = this.mockMvc.perform(post("/api/fee/projection").contentType(MediaType.APPLICATION_JSON).content(json))
                .andDo(print())
                .andReturn();

        int httpStatus = mvcResult.getResponse().getStatus();
        String response = mvcResult.getResponse().getContentAsString();

        Assert.assertEquals(200,httpStatus);

        String data = JsonPath.parse(response).read("$.data");
        Assert.assertEquals("01/06/2023 => 30 08/06/2023 => 30 08/06/2023 => 15 15/06/2023 => 30 ",data);

    }


    @Test
    public void getFeeProjectionMonthlyTest() throws Exception {
        FeesProjectionRequest feesProjectionRequest = new FeesProjectionRequest();
        feesProjectionRequest.setDuration(1);
        feesProjectionRequest.setFrequency("m");
        feesProjectionRequest.setPrincipalAmount(3000D);
        feesProjectionRequest.setStartDate("01/06/2023");
        String json = new Gson().toJson(feesProjectionRequest);
        MvcResult mvcResult = this.mockMvc.perform(post("/api/fee/projection").contentType(MediaType.APPLICATION_JSON).content(json))
                .andDo(print())
                .andReturn();

        int httpStatus = mvcResult.getResponse().getStatus();
        String response = mvcResult.getResponse().getContentAsString();

        Assert.assertEquals(200,httpStatus);

        String data = JsonPath.parse(response).read("$.data");
        Assert.assertEquals("01/06/2023 => 120 ",data);

    }

    /***
     * Installment tests
     * @throws Exception
     */

    @Test
    public void getInstallmentProjectionEmptyRequestTest() throws Exception {
        InstallmentProjectionRequest installmentProjectionRequest = new InstallmentProjectionRequest();
        String json = new Gson().toJson(installmentProjectionRequest);
        this.mockMvc.perform(post("/api/installment/projection").contentType(MediaType.APPLICATION_JSON).content(json))
                .andDo(print())
                .andExpect(status().isBadRequest());

    }

    @Test
    public void getInstallmentProjectionInvalidDurationTest() throws Exception {
        FeesProjectionRequest feesProjectionRequest = new FeesProjectionRequest();
        feesProjectionRequest.setDuration(13);
        feesProjectionRequest.setFrequency("m");
        feesProjectionRequest.setPrincipalAmount(1000D);
        feesProjectionRequest.setStartDate("01/06/2023");
        String json = new Gson().toJson(feesProjectionRequest);
        this.mockMvc.perform(post("/api/installment/projection").contentType(MediaType.APPLICATION_JSON).content(json))
                .andDo(print())
                .andExpect(status().isBadRequest());

    }

    @Test
    public void getInstallmentProjectionWeeklyTest() throws Exception {
        FeesProjectionRequest feesProjectionRequest = new FeesProjectionRequest();
        feesProjectionRequest.setDuration(3);
        feesProjectionRequest.setFrequency("w");
        feesProjectionRequest.setPrincipalAmount(3000D);
        feesProjectionRequest.setStartDate("01/06/2023");
        String json = new Gson().toJson(feesProjectionRequest);
        MvcResult mvcResult = this.mockMvc.perform(post("/api/installment/projection").contentType(MediaType.APPLICATION_JSON).content(json))
                .andDo(print())
                .andReturn();

        int httpStatus = mvcResult.getResponse().getStatus();
        String response = mvcResult.getResponse().getContentAsString();

        Assert.assertEquals(200,httpStatus);

        String data = JsonPath.parse(response).read("$.data");
        Assert.assertEquals("08/06/2023 => 1030 15/06/2023 => 1045 22/06/2023 => 1030 ",data);

    }

    @Test
    public void getInstallmentProjectionMonthlyTest() throws Exception {
        FeesProjectionRequest feesProjectionRequest = new FeesProjectionRequest();
        feesProjectionRequest.setDuration(3);
        feesProjectionRequest.setFrequency("m");
        feesProjectionRequest.setPrincipalAmount(3000D);
        feesProjectionRequest.setStartDate("01/06/2023");
        String json = new Gson().toJson(feesProjectionRequest);
        MvcResult mvcResult = this.mockMvc.perform(post("/api/installment/projection").contentType(MediaType.APPLICATION_JSON).content(json))
                .andDo(print())
                .andReturn();

        int httpStatus = mvcResult.getResponse().getStatus();
        String response = mvcResult.getResponse().getContentAsString();

        Assert.assertEquals(200,httpStatus);

        String data = JsonPath.parse(response).read("$.data");
        Assert.assertEquals("31/06/2023 => 1120 02/06/2023 => 1120 01/06/2023 => 1135 ",data);

    }


}
