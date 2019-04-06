package com.mymoneyapp.backend.mapper;

import com.mymoneyapp.backend.domain.BankingAccount;
import com.mymoneyapp.backend.request.BankingAccountRequest;
import com.mymoneyapp.backend.response.BankingAccountResponse;
import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BankingAccountMapper {

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "createdAt", ignore = true),
            @Mapping(target = "enabled", ignore = true),
            @Mapping(target = "paymentCycle", ignore = true),
            @Mapping(target = "user.id", source = "userId"),
    })
    BankingAccount requestToBankingAccount(BankingAccountRequest bankingAccountRequest);

    @Mappings({
            @Mapping(target = "createdAt", dateFormat = "dd/MM/yyyy HH:mm"),
    })
    BankingAccountResponse bankingAccountToResponse(BankingAccount bankingAccount);

    @InheritConfiguration
    List<BankingAccountResponse> bankingAccountsToResponses(Iterable<BankingAccount> bankingAccount);

}
