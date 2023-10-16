package com.interview.loan.projection.Controller;

import com.google.gson.Gson;
import com.interview.loan.projection.pojo.FeesProjectionRequest;
import com.interview.loan.projection.pojo.InstallmentProjectionRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class LoanControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void getFeesProjectionEmptyRequestTest() throws Exception {
        FeesProjectionRequest feesProjectionRequest = new FeesProjectionRequest();
        String json = new Gson().toJson(feesProjectionRequest);
        this.mockMvc.perform(post("/api/fee/projection").contentType(MediaType.APPLICATION_JSON).content(json))
                .andDo(print())
                .andExpect(status().isBadRequest());

    }

    @Test
    public void getInstallmentProjectionEmptyRequestTest() throws Exception {
        InstallmentProjectionRequest installmentProjectionRequest = new InstallmentProjectionRequest();
        String json = new Gson().toJson(installmentProjectionRequest);
        this.mockMvc.perform(post("/api/installment/projection").contentType(MediaType.APPLICATION_JSON).content(json))
                .andDo(print())
                .andExpect(status().isBadRequest());

    }
}
