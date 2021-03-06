package com.mymoneyapp.backend.mapper;

import com.mymoneyapp.backend.domain.PaymentCycle;
import com.mymoneyapp.backend.request.PaymentCycleRequest;
import com.mymoneyapp.backend.response.PaymentCycleResponse;
import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.List;

@Mapper(componentModel = "spring", uses = {
        BankingAccountMapper.class,
        CreditMapper.class,
        DebitMapper.class,
})
public interface PaymentCycleMapper {

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "enabled", ignore = true),
            @Mapping(target = "bankingAccount.id", source = "bankingAccount"),
    })
    PaymentCycle requestToPaymentCycle(PaymentCycleRequest paymentCycleRequest);

    @Mappings({
            @Mapping(target = "date", dateFormat = "dd/MM/yyyy"),
    })
    PaymentCycleResponse paymentCycleToResponse(PaymentCycle paymentCycle);

    @InheritConfiguration
    List<PaymentCycleResponse> paymentCyclesToResponses(Iterable<PaymentCycle> paymentCycles);

    default Page<PaymentCycleResponse> paymentCyclesToResponses(Page<PaymentCycle> paymentCycles) {
        List<PaymentCycleResponse> paymentCyclesResponses = paymentCyclesToResponses(paymentCycles.getContent());
        return new PageImpl<>(paymentCyclesResponses, paymentCycles.getPageable(), paymentCycles.getTotalElements());
    }

    @InheritConfiguration
    void updatePaymentCycleFromRequest(@MappingTarget PaymentCycle paymentCycle,
                                       PaymentCycleRequest paymentCycleRequest);

}
