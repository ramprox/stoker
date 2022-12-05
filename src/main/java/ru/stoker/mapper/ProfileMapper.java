package ru.stoker.mapper;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ru.stoker.database.entity.User;
import ru.stoker.dto.auth.RegisterDto;
import ru.stoker.dto.profile.*;

import java.time.LocalDateTime;
import java.util.List;

import static org.mapstruct.InjectionStrategy.CONSTRUCTOR;
import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING, injectionStrategy = CONSTRUCTOR,
        uses = { CredentialMapper.class })
public interface ProfileMapper {

    UserProfileInfo toUserProfileInfo(User user);

    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "personalData", source = ".")
    void updateFromProfileInfo(PersonalDataDto personalDataDto, @MappingTarget User user);

    @Mapping(target = "credentials", source = ".")
    @Mapping(target = "createdAt", ignore = true)
    void updateFromCredentialsDto(CredentialsDto credentialsDto, @MappingTarget User user);

    ProfileInfo toProfileInfo(User user);



    User fromProfileInfo(AdminUserProfileInfo adminUserProfileInfo);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    void updateFromProfileInfo(AdminUserProfileInfo adminUserProfileInfo, @MappingTarget User user);

    AdminUserProfileInfo toAdminProfileInfo(User user);

    List<AdminUserProfileInfo> toListProfileInfo(List<User> users);

    @AfterMapping
    default void afterMapping(@MappingTarget User user) {
        if(user.getId() == null) {
            user.setCreatedAt(LocalDateTime.now());
        }
    }


    User fromRegisterDto(RegisterDto registerDto);

}
