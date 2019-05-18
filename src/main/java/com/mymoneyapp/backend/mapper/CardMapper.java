package com.mymoneyapp.backend.mapper;

import com.mymoneyapp.backend.domain.Card;
import com.mymoneyapp.backend.request.CardRequest;
import com.mymoneyapp.backend.response.CardResponse;
import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring", uses = {
        TransactionMapper.class
})
public interface CardMapper {

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "enabled", ignore = true),
            @Mapping(target = "transactions", ignore = true),
            @Mapping(target = "createdAt", ignore = true),
            @Mapping(target = "bankingAccount.id", source = "bankingAccountId"),
    })
    Card requestToCard (CardRequest cardRequest);

    @Mappings({
            @Mapping(target = "cardExpiration", dateFormat = "dd/MM/yyyy"),
    })
    CardResponse cardToResponse(Card card);

    @InheritConfiguration
    List<CardResponse> cardToResponses(Iterable<Card> cards);


}
