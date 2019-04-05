package com.mymoneyapp.backend.mapper;

import com.mymoneyapp.backend.domain.User;
import com.mymoneyapp.backend.request.UserRequest;
import com.mymoneyapp.backend.response.UserResponse;
import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "username", ignore = true),
            @Mapping(target = "createdAt", ignore = true),
            @Mapping(target = "authorities", ignore = true),
            @Mapping(target = "enabled", ignore = true),
    })
    User requestToUser(UserRequest userRequest);

    @InheritConfiguration
    List<UserResponse> userToResponse(Iterable<User> user);

    default Page<UserResponse> usersToResponses(Page<User> users) {
        List<UserResponse> userResponses = userToResponse(users.getContent());
        return new PageImpl<>(userResponses, users.getPageable(), users.getTotalElements());
    }

}
