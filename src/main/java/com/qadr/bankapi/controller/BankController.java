package com.qadr.bankapi.controller;

import com.qadr.bankapi.errors.CustomException;
import com.qadr.bankapi.model.Bank;
import com.qadr.bankapi.security.JWTUtil;
import com.qadr.bankapi.service.BankService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController @RequiredArgsConstructor
@RequestMapping("/bank") @CrossOrigin("*")
public class BankController {
    private final BankService bankService;
    private final AuthenticationManager authenticationManager;

    @PostMapping("/add")
    public Bank addBank(@RequestBody Bank bank){
        return bankService.addBank(bank);
    }

    @GetMapping("/")
    public List<Bank> getAll(){
        return bankService.getAll();
    }

    @GetMapping("/name/{name}")
    public Bank getBank(@PathVariable("name") String name){
        return bankService.getBank(name);
    }


    @GetMapping("/type/{type}")
    public List<Bank> getBanksByType(@PathVariable("type") String type){
        return bankService.getBanks(type);
    }

    @DeleteMapping("/delete/{id}")
    public Bank deleteBank(@PathVariable("id") int id){
        return bankService.deleteBank(id);
    }

    @PutMapping("/edit/{id}")
    public Bank editBank (@PathVariable("id") int id, @RequestBody Bank bank){
        return bankService.updateBank(id, bank);
    }

    @PostMapping("/authenticate")
    public ResponseEntity<Map<String, String>> login (@RequestParam("username") String username,
                                 @RequestParam("password") String password){

        try{
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
            Authentication auth = authenticationManager.authenticate(authenticationToken);
            User user = (User) auth.getPrincipal();
            Map<String, String> tokens = new HashMap<>();
            tokens.put("access_token", JWTUtil.createAccessToken(user, "/bank/api/login"));
            tokens.put("refresh_token", JWTUtil.createRefreshToken(user));
            return new ResponseEntity<>(tokens, HttpStatus.OK);
        } catch (Exception e) {
            throw new CustomException("Incorrect username or password", HttpStatus.BAD_REQUEST);
        }
    }

}



