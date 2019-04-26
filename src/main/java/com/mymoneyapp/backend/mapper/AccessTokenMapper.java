package com.mymoneyapp.backend.mapper;

import com.mymoneyapp.backend.domain.AccessToken;
import com.mymoneyapp.backend.domain.User;
import com.mymoneyapp.backend.response.AccessTokenResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface AccessTokenMapper {

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "createdAt", ignore = true),
            @Mapping(target = "enabled", ignore = true),
            @Mapping(target = "token", source = "hash"),
    })
    AccessToken userToAccessToken(User user, String hash);

    @Mappings({
            @Mapping(target = "expiresAt", source = "accessToken.createdAt", dateFormat = "dd/MM/yyyy HH:mm:ss"),
    })
    AccessTokenResponse accessTokenToResponse(AccessToken accessToken);

}
