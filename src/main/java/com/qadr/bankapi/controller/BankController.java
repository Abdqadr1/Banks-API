package com.qadr.bankapi.controller;

import com.qadr.bankapi.errors.CustomException;
import com.qadr.bankapi.model.Bank;
import com.qadr.bankapi.security.JWTUtil;
import com.qadr.bankapi.service.BankService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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
@RestController
@RequestMapping("/bank")
public class BankController {
    @Autowired private AuthenticationManager authenticationManager;
    @Autowired private BankService bankService;

    @GetMapping("/all")
    public List<Bank> getAll(){
        return bankService.getAllBanks();
    }

    @GetMapping("/alias/{alias}")
    public Bank getBankByAlias(@PathVariable("alias") String alias){
        return bankService.getBankByAlias(alias);
    }

    @GetMapping("/id/{id}")
    public Bank getBankById(@PathVariable("id") Integer id){
        return bankService.getBankById(id);
    }


    @GetMapping("/type/{type}")
    public List<Bank> getBanksByType(@PathVariable("type") String type){
        return bankService.getBanksByType(type);
    }


    @PostMapping("/admin/add")
    public Bank addBank(Bank bank){
        return bankService.addBank(bank);
    }

    @GetMapping("/admin/enabled/{id}/{enabled}")
    public void updateEnabled(@PathVariable("id") Integer id,
                              @PathVariable("enabled") boolean enabled){
        bankService.updateEnabled(id,enabled);
    }


    @GetMapping("/admin/page/{number}")
    public Map<String, Object> getBankPage(@PathVariable("number") Integer pageNumber,
                                           @RequestParam(value = "keyword", defaultValue = "") String keyword){
        return bankService.getBankPage(pageNumber, keyword);
    }


    @DeleteMapping("/admin/delete/{id}")
    public Bank deleteBank(@PathVariable("id") int id){
        return bankService.deleteBankById(id);
    }

    @PostMapping("/admin/edit")
    public Bank editBank (Bank bank){
        return bankService.updateBank(bank);
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



