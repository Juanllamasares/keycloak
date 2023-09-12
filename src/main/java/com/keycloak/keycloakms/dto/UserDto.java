package com.keycloak.keycloakms.dto;

import java.util.Set;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.Value;

@Value
@RequiredArgsConstructor
@Builder
public class UserDto {
    String username;
    String email;
    String firstName;
    String lastName;
    String password;
    Set<String> roles;
}
