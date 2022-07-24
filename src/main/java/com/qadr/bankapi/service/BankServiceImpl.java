package com.qadr.bankapi.service;

import com.qadr.bankapi.errors.CustomException;
import com.qadr.bankapi.model.Bank;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.qadr.bankapi.repo.BankRepo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service @RequiredArgsConstructor @Transactional
public class BankServiceImpl implements BankService {
    public static final int BANKS_PER_PAGE = 3;
    private final BankRepo bankRepo;

    @Override
    public Bank addBank(Bank newBank) {
        Optional<Bank> bankOptional = bankRepo.findBySortCode(newBank.getSortCode());
        if (bankOptional.isPresent()) {
            throw new CustomException("Sort code already exists!", HttpStatus.BAD_REQUEST);
        }
        return bankRepo.save(newBank);
    }

    @Override
    public Bank getBank(int id) {
        Optional<Bank> bankOptional =  bankRepo.findById(id);
        if (bankOptional.isEmpty()) throw new CustomException("Bank not found", HttpStatus.NOT_FOUND);
        return bankOptional.get();
    }

    @Override
    public Bank getBank(String name) {
        return bankRepo.findByShortName(name).orElseThrow(
                ()-> new CustomException("Bank not found", HttpStatus.NOT_FOUND));
    }

    @Override
    public List<Bank> getBanks(String type) {
        return bankRepo.findByType(type);
    }

    @Override
    public List<Bank> getAll() {
        return bankRepo.findAll(Sort.by("fullName").ascending());
    }

    @Override
    public Bank deleteBank(int id) {
        Bank bank = bankRepo.findById(id)
                .orElseThrow(
                        ()-> new CustomException("Bank not found", HttpStatus.NOT_FOUND)
                );
        bankRepo.deleteById(id);
        return bank;
    }

    @Override
    public Bank updateBank(int id, Bank newBank) {
        Bank bank = bankRepo.findById(id).orElseThrow(
                () -> new CustomException("Bank not found", HttpStatus.NOT_FOUND)
        );

        Optional<Bank> bankOptional = bankRepo.findBySortCode(newBank.getSortCode());
        if (bankOptional.isPresent() && id != bankOptional.get().getId()) {
            throw new CustomException("Sort code already exists!", HttpStatus.BAD_REQUEST);
        }

        bank.setFullName(newBank.getFullName());
        bank.setShortName(newBank.getShortName());
        bank.setType(newBank.getType());
        bank.setSortCode(newBank.getSortCode());
        return bank;
    }

    @Override
    public Map<String, Object> getBankPage(int pageNumber) {
        Sort sort = Sort.by("fullName").ascending();
        PageRequest pageRequest = PageRequest.of(pageNumber - 1, BANKS_PER_PAGE, sort);
        Page<Bank> page = bankRepo.findAll(pageRequest);
        int startCount = (pageNumber-1) * BANKS_PER_PAGE + 1;
        int endCount = BANKS_PER_PAGE * pageNumber;
        endCount = (endCount > page.getTotalElements()) ? (int) page.getTotalElements() : endCount;
        HashMap<String, Object> map = new HashMap<>();
        map.put("currentPage", pageNumber);
        map.put("startCount", startCount);
        map.put("endCount", endCount);
        map.put("totalPages", page.getTotalPages());
        map.put("totalElements", page.getTotalElements());
        map.put("banks", page.getContent());
        map.put("numberPerPage", BANKS_PER_PAGE);
        return map;
    }



}
