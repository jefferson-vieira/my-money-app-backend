package com.mymoneyapp.backend.mapper;

import com.mymoneyapp.backend.domain.Transaction;
import com.mymoneyapp.backend.request.TransactionRequest;
import com.mymoneyapp.backend.response.TransactionResponse;
import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TransactionMapper {

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "enabled", ignore = true),
            @Mapping(target = "card.id", source = "cardId"),
            @Mapping(target = "createdAt", ignore = true),
    })
    Transaction requestToTransaction(TransactionRequest transactionRequest);

    @Mappings({
            @Mapping(target = "dateTransaction", dateFormat = "dd/MM/yyyy"),
    })
    TransactionResponse transactionToResponse(Transaction transaction);

    @InheritConfiguration
    List<TransactionResponse> transactionsToResponses(Iterable<Transaction> transactions);
}
