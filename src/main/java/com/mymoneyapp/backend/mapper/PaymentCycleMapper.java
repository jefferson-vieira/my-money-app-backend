package com.mymoneyapp.backend.mapper;

import com.mymoneyapp.backend.domain.PaymentCycle;
import com.mymoneyapp.backend.request.PaymentCycleRequest;
import com.mymoneyapp.backend.response.PaymentCycleResponse;
import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring", uses = {
        CreditMapper.class,
        DebitMapper.class,
})
public interface PaymentCycleMapper {

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "enabled", ignore = true)
    })
    PaymentCycle requestToPaymentCycle(PaymentCycleRequest paymentCycleRequest);

    @Mappings({
            @Mapping(target = "date", dateFormat = "dd/MM/yyyy"),
    })
    PaymentCycleResponse paymentCycleToResponse(PaymentCycle paymentCycle);

    @InheritConfiguration
    List<PaymentCycleResponse> paymentCyclesToResponses(Iterable<PaymentCycle> paymentCycle);
}