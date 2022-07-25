package com.qadr.bankapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.qadr.bankapi.model.Bank;
import com.qadr.bankapi.model.BankType;
import com.qadr.bankapi.model.Country;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
@SpringBootTest
@AutoConfigureMockMvc
class BankControllerTest {
    @Autowired private MockMvc mockMvc;
    @Autowired private TestEntityManager entityManager;
    ObjectMapper mapper = new ObjectMapper();

    private List<Bank> getBankList(){
        ArrayList<Bank> banks = new ArrayList<>();
        Bank bank = new Bank();
        bank.setType(BankType.nuban);
        bank.setCode("4214");
        bank.setLongCode("4214873587");
        bank.setCreatedTime(new Date());
        bank.setAlias("zen");
        bank.setName("Zenith");
        bank.setCountry(new Country(165));
        bank.setEnabled(true);
        banks.add(bank);

        Bank bank1 = new Bank();
        bank1.setType(BankType.nuban);
        bank1.setCode("4214");
        bank1.setLongCode("4214873587");
        bank1.setCreatedTime(new Date());
        bank1.setAlias("zen");
        bank1.setName("Zenith");
        bank1.setCountry(new Country(165));
        bank1.setEnabled(true);
        banks.add(bank1);

        return banks;
    }


    @Test
    void shouldCreateUser() throws Exception {

        List<Bank> banks = getBankList();

        mockMvc.perform(MockMvcRequestBuilders.post("/api/add")
                        .content(mapper.writeValueAsString(banks.get(0)))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(banks.get(0).getName()));
    }

    @Test
    void shouldReturnAllBanks() throws Exception {
        List<Bank> banks = getBankList();

//        when(bankService.getAll()).thenReturn(banks);

        mockMvc.perform(MockMvcRequestBuilders.get("/bank"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()").value(2));
    }

    @Test
    void shouldReturnBankWithName() throws Exception {
        List<Bank> banks = getBankList();

//        when(bankService.getBank("UBA")).thenReturn(banks.get(0));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/name/{name}", "UBA"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(banks.get(0).getName()));
    }

    @Test
    void shouldReturnBankWithType() throws Exception {
        List<Bank> banks = getBankList();

//        when(bankService.getBanks("commercial")).thenReturn(banks);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/type/{type}", "commercial"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()").value(banks.size()));
    }

    @Test
    void shouldDeleteBankById() throws Exception {
        List<Bank> banks = getBankList();

//        ArgumentCaptor<Integer> argumentMatchers = ArgumentCaptor.forClass(Integer.class);
//        when(bankService.deleteBank(argumentMatchers.capture()))
//                .thenReturn(banks.get(0));

        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.delete("/bank/delete/{id}", "1"))
                .andExpect(status().isOk());
//        verify(bankService).deleteBank(argumentMatchers.capture());

//        response.andExpect(MockMvcResultMatchers.jsonPath("$.fullName")
//                .value(banks.stream().filter((bank)-> Objects.equals(bank.getId(), argumentMatchers.getValue())).findFirst().get().getFullName()));
    }

    @Test
    void shouldEditBankById() throws Exception {
        List<Bank> banks = getBankList();

//        ArgumentCaptor<Integer> argumentMatchers = ArgumentCaptor.forClass(Integer.class);
//        when(bankService.updateBank(anyInt(), any(Bank.class)))
//                .thenReturn(banks.get(1));
//
//        ResultActions response=mockMvc.perform(MockMvcRequestBuilders.put("/bank/edit/{id}", "1")
//                        .content(mapper.writeValueAsString(banks.get(0)))
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk());
//        verify(bankService).updateBank(argumentMatchers.capture(), any(Bank.class));
//        response.andExpect(MockMvcResultMatchers.jsonPath("$.fullName")
//                .value(banks.get(1).getFullName()));
    }

    @Test
    void testGetBankPage() throws Exception{
        String url = "/bank/page/1";
        MvcResult mvcResult = mockMvc.perform(get(url)).andExpect(status().isOk())
                .andDo(print()).andReturn();
    }

}

