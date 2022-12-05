package ru.stoker.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.stoker.database.entity.embeddable.Credentials;
import ru.stoker.dto.profile.CredentialsDto;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING)
public abstract class CredentialMapper {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Mapping(target = "password", qualifiedByName = "fromRawPassword")
    public abstract Credentials fromCredentialsDto(CredentialsDto credentialsDto);

    @Named("fromRawPassword")
    protected String fromRawPassword(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }

    @Mapping(target = "password", ignore = true)
    public abstract CredentialsDto toCredentialsDto(Credentials credentials);

}
