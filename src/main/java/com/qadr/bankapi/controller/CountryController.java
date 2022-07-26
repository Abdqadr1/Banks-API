package com.qadr.bankapi.controller;

import com.qadr.bankapi.model.Country;
import com.qadr.bankapi.service.CountryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/country")
public class CountryController {
    @Autowired private CountryService countryService;

    @GetMapping("/all")
    public List<Country> getAllCountries(){
        return countryService.findAll();
    }


    @DeleteMapping("/delete/{id}")
    public void deleteCountry(@PathVariable Integer id){
        countryService.deleteCountry(id);
    }

    @PostMapping("/save")
    public Country saveCountry(Country country){
        return countryService.saveCountry(country);
    }

    @GetMapping("/continent/{continent}")
    public List<Country> getCountriesByContinent(@PathVariable String continent){
        return countryService.findByContinent(continent);
    }


    @GetMapping("/page/{number}")
    public Map<String, Object> getCountryPage(@PathVariable("number") Integer pageNumber){
        return countryService.getCountryPage(pageNumber);
    }
}
