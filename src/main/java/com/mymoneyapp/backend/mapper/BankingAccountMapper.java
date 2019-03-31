package com.mymoneyapp.backend.mapper;

import com.mymoneyapp.backend.domain.BankingAccount;
import com.mymoneyapp.backend.request.BankingAccountRequest;
import com.mymoneyapp.backend.response.BankingAccountResponse;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BankingAccountMapper {
    //Coisas que você não ira seram enviadas pelo usuario nem para ele
    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "createdAt", ignore = true),
            @Mapping(target = "enabled", ignore = true),
    })
    BankingAccount requestToBankingAccount(BankingAccountRequest bankingAccountRequest);

    List<BankingAccountResponse> bankingAccountToResponse(Iterable<BankingAccount> bankingAccount);
}
