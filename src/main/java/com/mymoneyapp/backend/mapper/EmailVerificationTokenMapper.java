package com.mymoneyapp.backend.mapper;

import com.mymoneyapp.backend.domain.EmailVerificationToken;
import com.mymoneyapp.backend.domain.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface EmailVerificationTokenMapper {

    @Mappings({
            @Mapping(target = "id",          ignore = true),
            @Mapping(target = "createdAt",   ignore = true),
            @Mapping(target = "enabled",     ignore = true),
            @Mapping(target = "token",     source = "hash"),
    })
    EmailVerificationToken userToEmailVerificationToken(User user, String hash);
}
