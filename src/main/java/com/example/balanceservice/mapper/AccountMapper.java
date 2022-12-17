package com.example.balanceservice.mapper;

import com.example.balanceservice.dto.AccountDTO;
import com.example.balanceservice.entity.AccountEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AccountMapper {
    AccountDTO accountEntityToAccountDTO(AccountEntity account);
    AccountEntity accountDTOtoAccountEntity(AccountDTO account);
}
