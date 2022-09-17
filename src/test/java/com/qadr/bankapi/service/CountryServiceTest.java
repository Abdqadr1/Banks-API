package com.qadr.bankapi.service;

import com.qadr.bankapi.errors.CustomException;
import com.qadr.bankapi.model.Country;
import com.qadr.bankapi.repo.CountryRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.*;
import java.util.function.Function;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
@AutoConfigureMockMvc
class CountryServiceTest {
    @MockBean private CountryRepo repo;
    @Autowired private CountryService countryService;

    public List<Country> list = List.of(
            new Country(4,
                    "testing1",
                    "ts",
                    "+35",
                    "test_continent"),
            new Country(1_000,
                    "testing2",
                    "ts2",
                    "+33",
                    "test_continent2"),
            new Country(1_001,
                    "testing3",
                    "ts3",
                    "+333",
                    "test_continent3")
    );

    @BeforeEach
    void setUP(){
        given(repo.findById(anyInt())).willReturn(Optional.of(list.get(0)));
        given(repo.findByName(anyString())).willReturn(Optional.of(list.get(0)));
        given(repo.findByCode(anyString())).willReturn(Optional.of(list.get(1)));
        given(repo.findByCallCode(anyString())).willReturn(list);
        given(repo.save(any(Country.class))).willReturn(list.get(1));
        Page<Country> page = new Page<>() {
            @Override
            public int getTotalPages() {
                return 0;
            }

            @Override
            public long getTotalElements() {
                return 0;
            }

            @Override
            public <U> Page<U> map(Function<? super Country, ? extends U> converter) {
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
            public List<Country> getContent() {
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

            @Override
            public Iterator<Country> iterator() {
                return list.iterator();
            }
        };
        given(repo.searchCountries(anyString(), any(Pageable.class))).willReturn(page);
        given(repo.findAll(any(Pageable.class))).willReturn(page);
    }

    @Test
    void testValidation(){
        assertThatThrownBy(()-> countryService.saveCountry(list.get(1)))
                .isInstanceOf(CustomException.class)
                .hasMessageContaining("already exists");

        assertThatThrownBy(()-> countryService.saveCountry(list.get(0)))
                .isInstanceOf(CustomException.class)
                .hasMessageContaining("already exists");

        given(repo.findByName(anyString())).willReturn(Optional.empty());
        given(repo.findByCode(anyString())).willReturn(Optional.empty());
        assertThatThrownBy(()-> countryService.saveCountry(list.get(1)))
                .isInstanceOf(CustomException.class)
                .hasMessageContaining("already exists");
    }

    @Test
    void saveCountry() {
        given(repo.findByName(anyString())).willReturn(Optional.empty());
        given(repo.findByCode(anyString())).willReturn(Optional.empty());
        given(repo.findByCallCode(anyString())).willReturn(Collections.EMPTY_LIST);
        Country country = countryService.saveCountry(list.get(0));
        assertThat(country).isNotNull();
        assert(country.equals(list.get(1)));
        System.out.println(country);
    }

    @Test
    void getCountryPage() {
        Map<String, Object> nullSearch = countryService.getCountryPage(2, "");
        assertThat(nullSearch.get("currentPage")).isEqualTo(2);
        assertThat( ((List) nullSearch.get("countries")).size()).isEqualTo(list.size());
        System.out.println(nullSearch);

        Map<String, Object> wordSearch = countryService.getCountryPage(2, "dgg");
        assertThat(wordSearch.get("currentPage")).isEqualTo(2);
        assertThat( ((List) wordSearch.get("countries")).size()).isEqualTo(list.size());
        System.out.println(wordSearch);
    }

    @Test
    void findAll() {
        given(repo.findAll()).willReturn(list);
        List<Country> all = countryService.findAll();
        assertThat(all.size()).isEqualTo(list.size());
    }

    @Test
    void deleteCountry() {
        countryService.deleteCountry(4);
        verify(repo, times(1)).deleteById(4);
    }
}