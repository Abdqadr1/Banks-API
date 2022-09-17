package com.qadr.bankapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.qadr.bankapi.model.Bank;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@SpringBootTest
@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@AutoConfigureRestDocs(outputDir = "target/generated-snippets")
class BankControllerTest {
    private MockMvc mockMvc;
    ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    public void setUp(
            WebApplicationContext context,
            RestDocumentationContextProvider restDocumentationContextProvider
    ) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(documentationConfiguration(restDocumentationContextProvider)
                        .uris()
                        .withHost("api-env.eba-irpspqyp.us-east-1.elasticbeanstalk.com")
                        .withScheme("http")
                )
                .build();
    }

    private List<Bank> getBankList(){
        ArrayList<Bank> banks = new ArrayList<>();
        Bank bank = new Bank();
        bank.setType("nuban");
        bank.setCode("4214");
        bank.setLongCode("4214873587");
        bank.setCreatedTime(new Date());
        bank.setAlias("zen");
        bank.setName("Zenith");
        bank.setCountry("NG");
        bank.setEnabled(true);
        banks.add(bank);

        Bank bank1 = new Bank();
        bank1.setType("nuban");
        bank1.setCode("4214");
        bank1.setLongCode("4214873587");
        bank1.setCreatedTime(new Date());
        bank1.setAlias("zen");
        bank1.setName("Zenith");
        bank1.setCountry("NG");
        bank1.setEnabled(true);
        banks.add(bank1);

        return banks;
    }

//    @Test
    @WithMockUser(username = "admin@gmail.com", roles = {"ADMIN"})
    void shouldCreateUser() throws Exception {
        List<Bank> banks = getBankList();

        mockMvc.perform(MockMvcRequestBuilders.post("/bank/admin/add")
                        .content(mapper.writeValueAsString(banks.get(0)))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(banks.get(0).getName()));
    }

    @Test
    void shouldReturnAllBanks() throws Exception {
        List<Bank> banks = getBankList();

//        when(bankService.getAll()).thenReturn(banks);

        mockMvc.perform(MockMvcRequestBuilders.get("/bank/all"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()").isNotEmpty())
                .andDo(
                        document("{methodName}",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()))
                );
    }

    @Test
    void shouldReturnBankWithName() throws Exception {
        List<Bank> banks = getBankList();

        var alias = "zenith-bank";
        mockMvc.perform(RestDocumentationRequestBuilders.get("/bank/alias/{alias}", alias))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.alias").value(alias))
                .andDo(
                        document("{methodName}",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                pathParameters(
                                        parameterWithName("alias")
                                                .description("bank alias, It must not be null or empty")
                                )
                        )
                )
                .andDo(print());
    }

    @Test
    void shouldReturnBankWithType() throws Exception {
        List<Bank> banks = getBankList();


        mockMvc.perform(RestDocumentationRequestBuilders.get("/bank/type/{type}", "nuban"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()").isNotEmpty())
                .andDo(
                        document("{methodName}",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                pathParameters(
                                        parameterWithName("type")
                                                .description("bank type. Only 'nuban' is currently acceptable. It must not be null or empty")
                                )
                        )
                );
    }

//    @Test
    @WithMockUser(username = "admin@gmail.com", roles = {"ADMIN"})
    void shouldDeleteBankById() throws Exception {
        List<Bank> banks = getBankList();

//        ArgumentCaptor<Integer> argumentMatchers = ArgumentCaptor.forClass(Integer.class);
//        when(bankService.deleteBank(argumentMatchers.capture()))
//                .thenReturn(banks.get(0));

        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.delete("/bank/admin/delete/{id}", "328"))
                .andExpect(status().isOk());
//        verify(bankService).deleteBank(argumentMatchers.capture());

//        response.andExpect(MockMvcResultMatchers.jsonPath("$.fullName")
//                .value(banks.stream().filter((bank)-> Objects.equals(bank.getId(), argumentMatchers.getValue())).findFirst().get().getFullName()));
    }

//    @Test
    @WithMockUser(username = "admin@gmail.com", roles = {"ADMIN"})
    void testGetBankPage() throws Exception{
        String url = "/bank/admin/page/1";
        MvcResult mvcResult = mockMvc.perform(get(url)).andExpect(status().isOk())
                .andDo(print()).andReturn();
    }

}

