package com.mymoneyapp.backend.mapper;

import com.mymoneyapp.backend.domain.Debit;
import com.mymoneyapp.backend.request.DebitRequest;
import com.mymoneyapp.backend.response.DebitResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DebitMapper {

    @Mappings({
            @Mapping(target = "id",      ignore = true),
            @Mapping(target = "enabled", ignore = true),
    })
    Debit requestToDebit(DebitRequest debitRequest);

    List<Debit> requestsToDebits(Iterable<DebitRequest> debitsRequests);

    DebitResponse debitToResponse(Debit debit);

    List<DebitResponse> debitsToResponses(Iterable<Debit> debits);

}
