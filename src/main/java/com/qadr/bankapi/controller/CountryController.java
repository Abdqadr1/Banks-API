package com.qadr.bankapi.controller;

import com.qadr.bankapi.model.Country;
import com.qadr.bankapi.repo.CountryRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/country")
public class CountryController {
    @Autowired private CountryRepo countryRepo;

    @GetMapping("/all")
    public List<Country> getAllCountries(){
        return countryRepo.findAll();
    }
}
