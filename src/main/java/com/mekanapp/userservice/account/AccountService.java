package com.mekanapp.userservice.account;

import com.mekanapp.userservice.exception.ErrorMessages;
import com.mekanapp.userservice.exception.UserException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class AccountService {

    private final AccountRepository repository;
    private final AccountMapper accountMapper;

    //SIGN UP
    public UUID createUser(AccountCreateDto accountCreateDto) {
        Account newAccount = new Account();
        BeanUtils.copyProperties(accountCreateDto, newAccount);
        newAccount.setStatus(AccountStatuses.ACTIVE.toString());
        Account responseAccount = repository.save(newAccount);
        return responseAccount.getId();
    }

    //SIGN IN
    public AccountDto getAccount(UUID id) {
        Optional<Account> responseAccount = repository.findById(id);
        Account existAccount;

        if(responseAccount.isEmpty()) {
            throw UserException.withStatusAndMessage(HttpStatus.NOT_FOUND, ErrorMessages.USER_NOT_FOUND);
        }
        else {
            existAccount = responseAccount.get();
        }

        return accountMapper.toDto(existAccount);
    }

    // WHO USE THE MEKANAPP - FRIENDLIST
    public List<AccountDto> getAccounts() {
        List<Account> responseAccounts = repository.findAll();
        return accountMapper.toDtoList(responseAccounts);
    }

    // UPDATE PROFILE
    public AccountDto updateAccount(UUID id, AccountUpdateDto accountUpdateDto) {
        Optional<Account> existAccount = repository.findById(id);

        if(existAccount.isEmpty()) {
            throw UserException.withStatusAndMessage(HttpStatus.NOT_FOUND, ErrorMessages.USER_NOT_FOUND);
        }

        Account existingAccount = existAccount.get();
        BeanUtils.copyProperties(accountUpdateDto, existingAccount);
        Account responseAccount = repository.save(existingAccount);

        return accountMapper.toDto(responseAccount);

    }

    // CLOSE ACCOUNT
    public Boolean deleteAccount(UUID id) {
        Optional<Account> existAccount = repository.findById(id);

        if(existAccount.isEmpty()) {
            throw UserException.withStatusAndMessage(HttpStatus.NOT_FOUND, ErrorMessages.USER_NOT_FOUND);
        }
        else {
            repository.deleteById(id);
            return true;
        }
    }
//sil
}