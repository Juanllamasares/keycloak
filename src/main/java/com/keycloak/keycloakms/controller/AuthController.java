package com.keycloak.keycloakms.controller;

import org.keycloak.admin.client.Keycloak;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.keycloak.keycloakms.dto.UserDto;
import com.keycloak.keycloakms.service.KeycloakUserServiceImpl;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    KeycloakUserServiceImpl keycloakUserServiceImpl;

    @Autowired
    Keycloak keycloak;

    @PreAuthorize("hasRole('admin-client')")
    @GetMapping("/secured/users")
    public ResponseEntity<?> getUsers() {

        return ResponseEntity.ok(keycloakUserServiceImpl.getUsers());
    }

    @PostMapping("/register-user")
    public ResponseEntity<String> registerUser(@RequestBody UserDto userDto) {
        return new ResponseEntity<String>(keycloakUserServiceImpl.createUser(userDto), HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('admin-client')")
    @PostMapping("/secured/register-admin")
    public ResponseEntity<String> registerAdmin(@RequestBody UserDto userDto) {
        return new ResponseEntity<String>(keycloakUserServiceImpl.createAdmin(userDto), HttpStatus.CREATED);
    }

}
