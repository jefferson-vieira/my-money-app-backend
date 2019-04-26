package com.mymoneyapp.backend.mapper;

import com.mymoneyapp.backend.domain.Credit;
import com.mymoneyapp.backend.request.CreditRequest;
import com.mymoneyapp.backend.response.CreditResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CreditMapper {

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "enabled", ignore = true),
    })
    Credit requestToCredit(CreditRequest creditRequest);

    List<Credit> requestsToCredits(Iterable<CreditRequest> creditsRequests);

    CreditResponse creditToResponse(Credit credit);

    List<CreditResponse> creditsToResponses(Iterable<Credit> credits);

}
