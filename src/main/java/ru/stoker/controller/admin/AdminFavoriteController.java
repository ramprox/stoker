package ru.stoker.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.stoker.dto.favorite.FavoriteIdDto;
import ru.stoker.dto.advt.AdvtInfo;
import ru.stoker.exceptions.Advt;
import ru.stoker.exceptions.FavoriteEx;
import ru.stoker.exceptions.UserEx;
import ru.stoker.service.favorite.FavoriteService;

import javax.validation.Valid;
import java.util.List;
import java.util.Locale;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static ru.stoker.exceptions.Advt.NotFoundException.ADVT_NOT_FOUND;
import static ru.stoker.exceptions.FavoriteEx.NotFoundException.FAVORITE_NOT_FOUND;
import static ru.stoker.exceptions.UserEx.NotFoundException.USER_NOT_FOUND;

@PreAuthorize("hasAuthority('ADMIN')")
@RestController
@RequestMapping("/api/v1/admin/favorite")
public class AdminFavoriteController {

    private final FavoriteService favoriteService;

    private final MessageSource messageSource;

    @Autowired
    public AdminFavoriteController(FavoriteService favoriteService,
                                   MessageSource messageSource) {
        this.favoriteService = favoriteService;
        this.messageSource = messageSource;
    }

    @PostMapping
    public void save(@RequestBody @Valid FavoriteIdDto favoriteIdDto) {
        favoriteService.save(favoriteIdDto.getUserId(), favoriteIdDto.getAdvtId());
    }

    @GetMapping("/all")
    public List<AdvtInfo> getByUserId(@RequestParam Long userId) {
        return favoriteService.getFavorites(userId);
    }

    @DeleteMapping
    public void deleteById(@RequestBody @Valid FavoriteIdDto favoriteIdDto) {
        favoriteService.deleteById(favoriteIdDto.getUserId(), favoriteIdDto.getAdvtId());
    }

    @ResponseStatus(NOT_FOUND)
    @ExceptionHandler(FavoriteEx.NotFoundException.class)
    public String handleFavoriteAdvtNotFoundException(FavoriteEx.NotFoundException ex, Locale locale) {
        return messageSource.getMessage(FAVORITE_NOT_FOUND, new Object[]{ ex.getUserId(), ex.getAdvtId() }, locale);
    }

    @ResponseStatus(NOT_FOUND)
    @ExceptionHandler(Advt.NotFoundException.class)
    public String handleAdvertisementIdNotFoundException(Advt.NotFoundException ex, Locale locale) {
        return messageSource.getMessage(ADVT_NOT_FOUND, new Object[]{ ex.getId() }, locale);
    }

    @ResponseStatus(NOT_FOUND)
    @ExceptionHandler(UserEx.NotFoundException.class)
    public String handleUserNotFoundException(UserEx.NotFoundException ex, Locale locale) {
        return messageSource.getMessage(USER_NOT_FOUND, new Object[]{ ex.getId() }, locale);
    }

}
