package com.qadr.bankapi.repo;

import com.qadr.bankapi.model.Country;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.Rollback;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback
class CountryRepoTest {
    @Autowired private CountryRepo repo;

    @Test
    void findByContinent() {
        String continent = "africa";
        List<Country> byContinent = repo.findByContinent(continent);
        assertThat(byContinent.size()).isGreaterThan(0);
        System.out.println(byContinent);
    }

    @Test
    void findByName() {
        String name = "nigeria";
        Optional<Country> byName = repo.findByName(name);
        assertThat(byName).isPresent();
        System.out.println(byName.get());
    }

    @Test
    void findByCode() {
        String code = "ng";
        Optional<Country> byCode = repo.findByCode(code);
        assertThat(byCode).isPresent();
        System.out.println(byCode.get());
    }

    @Test
    void findByCallCode() {
        String code = "+234";
        Optional<Country> byCode = repo.findByCallCode(code);
        assertThat(byCode).isPresent();
        System.out.println(byCode.get());
    }

    @Test
    void searchCountries() {
        String keyword = "ghan";
        Page<Country> countries = repo.searchCountries(keyword, PageRequest.of(0, 5));
        assertThat(countries.getContent().size()).isGreaterThan(0);
        System.out.println(countries.getContent());
    }
}