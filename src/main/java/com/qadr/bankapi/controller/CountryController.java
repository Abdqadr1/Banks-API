package com.qadr.bankapi.controller;

import com.qadr.bankapi.model.Country;
import com.qadr.bankapi.repo.CountryRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/country")
public class CountryController {
    public static final int COUNTRY_PER_PAGE = 25;
    @Autowired private CountryRepo countryRepo;

    @GetMapping("/all")
    public List<Country> getAllCountries(){
        return countryRepo.findAll();
    }

    @GetMapping("/continent/{continent}")
    public List<Country> getCountriesByContinent(@PathVariable String continent){
        return countryRepo.findByContinent(continent);
    }


    @GetMapping("/page/{number}")
    public Map<String, Object> getCountryPage(@PathVariable("number") Integer pageNumber){
        Sort sort = Sort.by("name").ascending();
        PageRequest pageRequest = PageRequest.of(pageNumber - 1, COUNTRY_PER_PAGE, sort);
        Page<Country> page = countryRepo.findAll(pageRequest);
        int startCount = (pageNumber-1) * COUNTRY_PER_PAGE + 1;
        int endCount = COUNTRY_PER_PAGE * pageNumber;
        endCount = (endCount > page.getTotalElements()) ? (int) page.getTotalElements() : endCount;
        HashMap<String, Object> map = new HashMap<>();
        map.put("currentPage", pageNumber);
        map.put("startCount", startCount);
        map.put("endCount", endCount);
        map.put("totalPages", page.getTotalPages());
        map.put("totalElements", page.getTotalElements());
        map.put("countries", page.getContent());
        map.put("numberPerPage", COUNTRY_PER_PAGE);
        return map;
    }
}
