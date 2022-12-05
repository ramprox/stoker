package ru.stoker.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.stoker.dto.category.CategoryDto;
import ru.stoker.dto.util.validgroups.OnCreate;
import ru.stoker.dto.util.validgroups.OnUpdate;
import ru.stoker.service.category.CategoryService;

import javax.validation.groups.Default;
import java.util.List;
import java.util.Locale;

import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static ru.stoker.exceptions.Category.*;
import static ru.stoker.exceptions.Category.AlreadyExistException.CATEGORY_ALREADY_EXIST;
import static ru.stoker.exceptions.Category.NotFoundException.CATEGORY_NOT_FOUND;
import static ru.stoker.exceptions.Category.ParentNotFoundException.PARENT_CATEGORY_NOT_FOUND;
import static ru.stoker.exceptions.Category.RootAlreadyExistException.ROOT_CATEGORY_EXIST;

@RestController
@RequestMapping("/api/v1/category")
public class CategoryController {

    private final CategoryService categoryService;

    private final MessageSource messageSource;

    @Autowired
    public CategoryController(CategoryService categoryService,
                              MessageSource messageSource) {
        this.categoryService = categoryService;
        this.messageSource = messageSource;
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping
    public CategoryDto save(@RequestBody @Validated({ OnCreate.class, Default.class}) CategoryDto category) {
        return categoryService.save(category);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/{id}")
    public CategoryDto findById(@PathVariable Long id) {
        return categoryService.findById(id);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping
    public CategoryDto update(@RequestBody @Validated({ OnUpdate.class, Default.class }) CategoryDto categoryDto) {
        return categoryService.update(categoryDto);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) {
        categoryService.deleteById(id);
    }

    @GetMapping("/all")
    public List<CategoryDto> findAll(@RequestParam Long parentId) {
        return categoryService.findAll(parentId);
    }

    @ResponseStatus(NOT_FOUND)
    @ExceptionHandler(ParentNotFoundException.class)
    public String handleParentNotFoundException(ParentNotFoundException ex, Locale locale) {
        return messageSource.getMessage(PARENT_CATEGORY_NOT_FOUND, new Object[]{ ex.getId() }, locale);
    }

    @ResponseStatus(NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public String handleCategoryNotFoundException(NotFoundException ex, Locale locale) {
        return messageSource.getMessage(CATEGORY_NOT_FOUND, new Object[]{ ex.getId() }, locale);
    }

    @ResponseStatus(CONFLICT)
    @ExceptionHandler(AlreadyExistException.class)
    public String handleCategoryAlreadyExistException(AlreadyExistException ex, Locale locale) {
        return messageSource.getMessage(CATEGORY_ALREADY_EXIST, new Object[]{ ex.getName(), ex.getId() }, locale);
    }

    @ResponseStatus(CONFLICT)
    @ExceptionHandler(RootAlreadyExistException.class)
    public String handleRootCategoryExistException(RootAlreadyExistException ex, Locale locale) {
        return messageSource.getMessage(ROOT_CATEGORY_EXIST, new Object[]{ ex.getName() }, locale);
    }

}
