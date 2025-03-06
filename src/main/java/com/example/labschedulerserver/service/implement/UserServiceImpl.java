package com.example.labschedulerserver.service.implement;

import com.example.labschedulerserver.model.Account;
import com.example.labschedulerserver.repository.AccountRepository;
import com.example.labschedulerserver.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final AccountRepository accountRepository;

    @Override
    public String checkUserIfExist(String email) {
        Account account = accountRepository.findUserByEmail(email).orElse(null);
        if(account == null) {
            return null;
        }
        return account.getEmail();
    }

    @Override
    public Optional<Account> getUserByEmail(String email) {
        return accountRepository.findUserByEmail(email);
    }

    @Override
    public void saveUser(Account account) {
        accountRepository.save(account);
    }
}
