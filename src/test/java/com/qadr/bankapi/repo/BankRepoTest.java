package com.qadr.bankapi.repo;

import com.qadr.bankapi.model.Bank;
import com.qadr.bankapi.model.BankType;
import com.qadr.bankapi.model.Country;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import java.util.Date;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
class BankRepoTest {

    @Autowired private BankRepo bankRepo;
    @Autowired private TestEntityManager entityManager;

    @Test
    void testAddBank(){
        Country country = entityManager.find(Country.class, 165);
        assertThat(country).isNotNull();
        System.out.println(country);
        Bank bank = new Bank();
        bank.setType(BankType.nuban);
        bank.setCode("4214");
        bank.setLongCode("4214873587");
        bank.setCreatedTime(new Date());
        bank.setAlias("zen");
        bank.setName("Zenith");
        bank.setCountry(country);
        bank.setEnabled(true);
        Bank save = bankRepo.save(bank);
        assertThat(save).isNotNull();
        System.out.println(save);
    }




    @Test
    void findById() {
        Optional<Bank> bank = bankRepo.findById(1);
        assertThat(bank).isPresent();
        System.out.println(bank);
    }

    @Test
    void findByAlias() {
        // given
        String alias = "zen";
        Optional<Bank> bank = bankRepo.findByAlias(alias);
        assertThat(bank).isPresent();
        System.out.println(bank.get());
        assertThat(bank.get().getAlias()).isEqualTo(alias);
    }

    @Test
    void findByCode() {
        String sortCode = "4214";
        Optional<Bank> bank = bankRepo.findByCode(sortCode);
        assertThat(bank).isPresent();
        System.out.println(bank.get());
        assertThat(bank.get().getCode()).isEqualTo(sortCode);
    }
}