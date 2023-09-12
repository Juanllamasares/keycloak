package com.keycloak.keycloakms.service;

import java.util.List;

import org.keycloak.representations.idm.UserRepresentation;

import com.keycloak.keycloakms.dto.UserDto;

public interface KeycloakUserService {
    public String createUser(UserDto userDto);
    public void deleteUser(String userId);
    public void updateUser(String userId, UserDto userDto);
    public List<UserRepresentation> getUsers();
    public UserRepresentation getUserByUsername(String username);
}
