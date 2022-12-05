package ru.stoker.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.stoker.dto.advt.AdvtInfo;
import ru.stoker.exceptions.Advt;
import ru.stoker.exceptions.FavoriteEx;
import ru.stoker.service.security.StokerUserDetails;
import ru.stoker.service.favorite.FavoriteService;

import java.util.List;
import java.util.Locale;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static ru.stoker.exceptions.Advt.NotFoundException.ADVT_NOT_FOUND;
import static ru.stoker.exceptions.FavoriteEx.NotFoundException.USER_FAVORITE_NOT_FOUND;

@PreAuthorize("hasAuthority('USER')")
@RestController
@RequestMapping("/api/v1/favorite")
public class FavoriteController {

    private final FavoriteService favoriteService;

    private final MessageSource messageSource;

    @Autowired
    public FavoriteController(FavoriteService favoriteService,
                              MessageSource messageSource) {
        this.favoriteService = favoriteService;
        this.messageSource = messageSource;
    }

    @PostMapping
    public void save(@RequestBody Long advtId,
                     @AuthenticationPrincipal StokerUserDetails userDetails) {
        favoriteService.save(userDetails.getId(), advtId);
    }

    @GetMapping("/all")
    public List<AdvtInfo> getFavorites(@AuthenticationPrincipal StokerUserDetails userDetails) {
        return favoriteService.getFavorites(userDetails.getId());
    }

    @DeleteMapping("/{advtId}")
    public void deleteById(@PathVariable Long advtId,
                           @AuthenticationPrincipal StokerUserDetails userDetails) {
        favoriteService.deleteById(userDetails.getId(), advtId);
    }

    @ResponseStatus(NOT_FOUND)
    @ExceptionHandler(FavoriteEx.NotFoundException.class)
    public String handleFavoriteAdvtNotFoundException(FavoriteEx.NotFoundException ex, Locale locale) {
        return messageSource.getMessage(USER_FAVORITE_NOT_FOUND, new Object[]{ ex.getAdvtId() }, locale);
    }

    @ResponseStatus(NOT_FOUND)
    @ExceptionHandler(Advt.NotFoundException.class)
    public String handleAdvertisementIdNotFoundException(Advt.NotFoundException ex, Locale locale) {
        return messageSource.getMessage(ADVT_NOT_FOUND, new Object[]{ ex.getId() }, locale);
    }

}
