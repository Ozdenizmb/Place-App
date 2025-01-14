package com.mekanapp.mekanuserms.account;

import com.mekanapp.mekanuserms.exception.AppException;
import com.mekanapp.mekanuserms.exception.ErrorMessages;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class AccountService {

    private final AccountRepository repository;
    private final AccountMapper accountMapper;

    // Sign Up
    public UUID createUser(AccountCreateDto accountCreateDto) {
        Account newAccount = new Account();
        BeanUtils.copyProperties(accountCreateDto, newAccount);
        newAccount.setStatus(AccountStatuses.ACTIVE.toString());
        Account responseAccount = repository.save(newAccount);
        return responseAccount.getId();
    }

    // GET YOUR ACCOUNT INFO
    public AccountDto getAccount(UUID id) {
        Optional<Account> responseAccount = repository.findById(id);
        Account existAccount;

        if(responseAccount.isEmpty()) {
            throw AppException.withStatusAndMessage(HttpStatus.NOT_FOUND, ErrorMessages.USER_NOT_FOUND);
        }
        else {
            existAccount = responseAccount.get();
        }

        return accountMapper.toDto(existAccount);
    }

    // WHO USE THE MEKANAPP - FRIENDLIST (The logged in user cannot see him/herself in this list.)
    public Page<AccountDto> getAccounts(Pageable page, UUID id) {
        AccountDto existAccount = null;

        if(id != null){
            existAccount = getAccount(id);
        }

        if(existAccount != null) {
            return repository.findByUsernameNot(existAccount.username(), page);
        }
        return repository.findAll(page).map(accountMapper::toDto);
    }

    // Updates Account
    public AccountDto updateAccount(UUID id, AccountUpdateDto accountUpdateDto) {
        Optional<Account> existAccount = repository.findById(id);

        if(existAccount.isEmpty()) {
            throw AppException.withStatusAndMessage(HttpStatus.NOT_FOUND, ErrorMessages.USER_NOT_FOUND);
        }

        Account existingAccount = existAccount.get();
        BeanUtils.copyProperties(accountUpdateDto, existingAccount);
        Account responseAccount = repository.save(existingAccount);

        return accountMapper.toDto(responseAccount);

    }

    // Delete account
    public Boolean deleteAccount(UUID id) {
        Optional<Account> existAccount = repository.findById(id);

        if(existAccount.isEmpty()) {
            throw AppException.withStatusAndMessage(HttpStatus.NOT_FOUND, ErrorMessages.USER_NOT_FOUND);
        }
        else {
            repository.deleteById(id);
            return true;
        }
    }

}
