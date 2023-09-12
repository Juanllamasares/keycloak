package com.keycloak.keycloakms.service;

import java.util.Collections;
import java.util.List;

import javax.ws.rs.core.Response;

import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import com.keycloak.keycloakms.dto.UserDto;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class KeycloakUserServiceImpl implements KeycloakUserService{

    @Value("${keycloak.realm}")
    public String realm;

    @Autowired
    Keycloak keycloak;

    @Override
    public String createUser(@NonNull UserDto userDto) {
        int status;
        UserRepresentation userRepresentation = new UserRepresentation();
        RealmResource realmResource = keycloak.realm(realm);
        UsersResource usersResource = realmResource.users();
        
        //creando el usuario
        userRepresentation.setUsername(userDto.getUsername());
        userRepresentation.setEmail(userDto.getEmail());
        userRepresentation.setFirstName(userDto.getFirstName());
        userRepresentation.setLastName(userDto.getLastName());
        userRepresentation.setEnabled(true);
        userRepresentation.setEmailVerified(true);

        Response response = usersResource.create(userRepresentation);

        status = response.getStatus();

        if (status == 201) {
            String path = response.getLocation().getPath();
            String userId = path.substring(path.lastIndexOf("/") + 1);

            CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
            credentialRepresentation.setTemporary(false);
            credentialRepresentation.setType(CredentialRepresentation.PASSWORD);
            credentialRepresentation.setValue(userDto.getPassword());

            usersResource.get(userId).resetPassword(credentialRepresentation);

            //AGREGAR ROLES SOLO ESTANDO AUTENTICADO Y CON AUTORIZACION EN KEYCLOAK
            /* List<RoleRepresentation> rolesRepresentation;

            rolesRepresentation = List.of(realmResource.roles().get("user").toRepresentation());

            usersResource.get(userId).roles().realmLevel().add(rolesRepresentation); */

            return "User created successfully!!";

        } else if (status == 409) {
            log.error("User already exist!");
            return "User already exist!";
        } else {
            log.error("Error creating user, please contact with the administrator.");
            return "Error creating user, please contact with the administrator.";
        }
    }

    public String createAdmin(@NonNull UserDto userDto) {
        int status;
        UserRepresentation userRepresentation = new UserRepresentation();
        RealmResource realmResource = keycloak.realm(realm);
        UsersResource usersResource = realmResource.users();
        
        //creando el usuario
        userRepresentation.setUsername(userDto.getUsername());
        userRepresentation.setEmail(userDto.getEmail());
        userRepresentation.setFirstName(userDto.getFirstName());
        userRepresentation.setLastName(userDto.getLastName());
        userRepresentation.setEnabled(true);
        userRepresentation.setEmailVerified(true);

        Response response = usersResource.create(userRepresentation);

        status = response.getStatus();

        if (status == 201) {
            String path = response.getLocation().getPath();
            String userId = path.substring(path.lastIndexOf("/") + 1);

            CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
            credentialRepresentation.setTemporary(false);
            credentialRepresentation.setType(CredentialRepresentation.PASSWORD);
            credentialRepresentation.setValue(userDto.getPassword());

            usersResource.get(userId).resetPassword(credentialRepresentation);

            //AGREGAR ROLES SOLO ESTANDO AUTENTICADO Y CON AUTORIZACION EN KEYCLOAK
            List<RoleRepresentation> rolesRepresentation;

            rolesRepresentation = List.of(realmResource.roles().get("admin").toRepresentation());

            usersResource.get(userId).roles().realmLevel().add(rolesRepresentation);

            return "Admin created successfully!!";

        } else if (status == 409) {
            log.error("Admin already exist!");
            return "Admin already exist!";
        } else {
            log.error("Error creating admin, please contact with the administrator.");
            return "Error creating admin, please contact with the administrator.";
        }
    }

    
    @Override
    public void deleteUser(String userId) {
        keycloak.realm(realm).users().get(userId).remove();
    }

    @Override
    public void updateUser(String userId, UserDto userDto) {
        RealmResource realmResource = keycloak.realm(realm);
        UsersResource usersResource = realmResource.users();
        CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
        UserRepresentation user = new UserRepresentation();
        UserResource userResource = usersResource.get(userId);

        user.setUsername(userDto.getUsername());
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setEmail(userDto.getEmail());
        user.setEnabled(true);
        user.setEmailVerified(true);
        user.setCredentials(Collections.singletonList(credentialRepresentation));

        credentialRepresentation.setTemporary(false);
        credentialRepresentation.setType(OAuth2Constants.PASSWORD);
        credentialRepresentation.setValue(userDto.getPassword());

        userResource.update(user);
    }

    @Override
    public List<UserRepresentation> getUsers() {
        return keycloak.realm(realm).users().list();
    }

    @Override
    public UserRepresentation getUserByUsername(String username) {
        return (UserRepresentation) keycloak.realm(realm).users().searchByUsername(username, true);
    }
    
}
