package com.qadr.bankapi.repo;

import com.qadr.bankapi.model.Bank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BankRepo extends JpaRepository<Bank, Integer> {
    List<Bank> findByType(String type);
    Optional<Bank> findByAlias(String name);
    Optional<Bank> findByCode(String code);

    @Modifying
    @Query("UPDATE Bank b SET b.enabled=?2 WHERE b.id=?1")
    void updateEnabled(Integer id, boolean enabled);
}
