package com.mymoneyapp.backend.mapper;

import com.mymoneyapp.backend.domain.Address;
import com.mymoneyapp.backend.request.AddressRequest;
import com.mymoneyapp.backend.response.AddressResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AddressMapper {

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "enabled", ignore = true),
    })
    Address requestToAddress(AddressRequest addressRequest);

    List<Address> requestsToAddresses(Iterable<AddressRequest> addressesRequests);

    AddressResponse addressToResponse(Address address);

    List<AddressResponse> addressesToResponses(Iterable<Address> addresses);

}
