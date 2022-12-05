package ru.stoker.mapper;

import org.mapstruct.*;
import ru.stoker.database.entity.Category;
import ru.stoker.dto.category.CategoryDto;

import java.util.List;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING)
public interface CategoryMapper {

    @Mapping(target = "parent", source = "parentId")
    Category toCategory(CategoryDto categoryDto);

    default Category toParent(Long parentId) {
        if(parentId == null) {
            return null;
        }
        Category category = new Category();
        category.setId(parentId);
        return category;
    }

    @Mapping(target = "parentId", source = "parent.id")
    CategoryDto fromCategory(Category category);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id")
    CategoryDto updateFromCategory(Category category, @MappingTarget CategoryDto categoryDto);

    @InheritConfiguration
    void updateCategoryFromDto(CategoryDto categoryDto, @MappingTarget Category category);

    List<CategoryDto> toList(List<Category> categories);

}
