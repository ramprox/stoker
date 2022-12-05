package ru.stoker.mapper;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ru.stoker.database.entity.Advertisement;
import ru.stoker.dto.advt.AdvtInfo;
import ru.stoker.dto.advt.CreateAdvt;
import ru.stoker.dto.advt.UpdateAdvt;

import java.time.LocalDate;
import java.util.List;

import static org.mapstruct.InjectionStrategy.CONSTRUCTOR;
import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING, uses = {ProductMapper.class},
        injectionStrategy = CONSTRUCTOR)
public interface AdvtMapper {

    @Mapping(target = "user.id", source = "userId")
    Advertisement fromCreateAdvt(Long userId, CreateAdvt createAdvt);

    @AfterMapping
    default void afterMapping(@MappingTarget Advertisement advertisement) {
        if(advertisement.getId() == null) {
            advertisement.setPostedAt(LocalDate.now());
        }
    }

    @Mapping(target = "userId", source = "user.id")
    AdvtInfo toAdvtInfo(Advertisement advertisement);

    List<AdvtInfo> toListAdvtInfo(List<Advertisement> advertisements);

    @Mapping(target = "user.id", source = "ownerId")
    void updateFromUpdateAdvt(Long ownerId, UpdateAdvt advtDto, @MappingTarget Advertisement advertisement);

}
