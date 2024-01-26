package com.mekanapp.userservice.account;

import com.mekanapp.userservice.shared.BaseMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AccountMapper extends BaseMapper<Account, AccountDto> {



}
