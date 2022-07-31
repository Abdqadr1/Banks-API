package com.qadr.bankapi.service;

import com.qadr.bankapi.errors.CustomException;
import com.qadr.bankapi.model.Bank;
import com.qadr.bankapi.model.Country;
import com.qadr.bankapi.repo.BankRepo;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
@AutoConfigureMockMvc
class BankServiceTest {
    @MockBean private BankRepo repo;
    @Autowired private BankService bankService;

    public List<Bank> list = List.of(
            new Bank(1, "bank1", "bk1", "nuban", "35353"),
            new Bank(2, "bank2", "bk2", "nuban", "3533"),
            new Bank(3, "bank3", "bk3", "nuban", "5353")
    );

    @BeforeEach
    void setUp(){
        given(repo.findById(anyInt())).willReturn(Optional.of(list.get(0)));
        given(repo.findByAlias(anyString())).willReturn(Optional.of(list.get(0)));
        given(repo.findByCode(anyString())).willReturn(List.of(list.get(1)));
        given(repo.save(any(Bank.class))).willReturn(list.get(1));
        Page<Bank> page = new Page<>() {
            @Override
            public int getTotalPages() {
                return 0;
            }

            @Override
            public long getTotalElements() {
                return 0;
            }

            @Override
            public <U> Page<U> map(Function<? super Bank, ? extends U> converter) {
                return null;
            }

            @Override
            public int getNumber() {
                return 0;
            }

            @Override
            public int getSize() {
                return 0;
            }

            @Override
            public int getNumberOfElements() {
                return 0;
            }

            @Override
            public List<Bank> getContent() {
                return list;
            }

            @Override
            public boolean hasContent() {
                return false;
            }

            @Override
            public Sort getSort() {
                return null;
            }

            @Override
            public boolean isFirst() {
                return false;
            }

            @Override
            public boolean isLast() {
                return false;
            }

            @Override
            public boolean hasNext() {
                return false;
            }

            @Override
            public boolean hasPrevious() {
                return false;
            }

            @Override
            public Pageable nextPageable() {
                return null;
            }

            @Override
            public Pageable previousPageable() {
                return null;
            }

            @NotNull
            @Override
            public Iterator<Bank> iterator() {
                return list.iterator();
            }
        };
        given(repo.searchBanks(anyString(), any(Pageable.class))).willReturn(page);
        given(repo.findAll(any(Pageable.class))).willReturn(page);
    }

    @Test
    void testValidation(){
        assertThatThrownBy(()-> bankService.addBank(list.get(1)))
                .isInstanceOf(CustomException.class)
                .hasMessageContaining("Sort code already exists!");

        assertThatThrownBy(()-> bankService.addBank(list.get(2)))
                .isInstanceOf(CustomException.class)
                .hasMessageContaining("Alias already exists!");
    }

    @Test
    void addBank() {
        Bank bank = bankService.addBank(list.get(0));
        assertThat(bank).isNotNull();
        assertThat(bank).isEqualTo(list.get(1));
        System.out.println(bank);
    }

    @Test
    void updateBank() {
        Bank bank = bankService.updateBank(list.get(0));
        assertThat(bank).isNotNull();
        assertThat(bank).isEqualTo(list.get(1));
        System.out.println(bank);
    }

    @Test
    void updateEnabled() {
        bankService.updateEnabled(1, true);
        verify(repo, times(1)).findById(1);
    }

    @Test
    void getAllBanks() {
        bankService.getAllBanks();
        verify(repo).findAll(any(Sort.class));
    }

    @Test
    void deleteBankById() {
        bankService.deleteBankById(anyInt());
        verify(repo, times(1)).findById(anyInt());
        verify(repo, times(1)).deleteById(anyInt());
    }

    @Test
    void getBankPage() {
        Map<String, Object> nullSearch = bankService.getBankPage(2, "");
        assertThat(nullSearch.get("currentPage")).isEqualTo(2);
        assertThat( ((List) nullSearch.get("banks")).size()).isEqualTo(list.size());
        System.out.println(nullSearch);

        Map<String, Object> wordSearch = bankService.getBankPage(2, "dgg");
        assertThat(wordSearch.get("currentPage")).isEqualTo(2);
        assertThat( ((List) wordSearch.get("banks")).size()).isEqualTo(list.size());
        System.out.println(wordSearch);
    }
}