package ru.stoker.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.stoker.dto.productestimaton.ProductEstimationDto;
import ru.stoker.dto.productestimaton.ProductEstimationInfo;
import ru.stoker.exceptions.ProductEstimationEx.AlreadyExistException;
import ru.stoker.exceptions.ProductEstimationEx.NotFoundException;
import ru.stoker.exceptions.ProductEstimationEx.UserSelfEstimationException;
import ru.stoker.exceptions.ProductEx;
import ru.stoker.service.security.StokerUserDetails;
import ru.stoker.service.productestimation.ProductEstimationService;

import javax.validation.Valid;
import java.util.List;
import java.util.Locale;

import static org.springframework.http.HttpStatus.*;
import static ru.stoker.exceptions.ProductEstimationEx.AlreadyExistException.USER_ALREADY_ESTIMATED;
import static ru.stoker.exceptions.ProductEstimationEx.NotFoundException.PRODUCT_ESTIMATION_NOT_FOUND;
import static ru.stoker.exceptions.ProductEstimationEx.UserSelfEstimationException.USER_SELF_ESTIMATION;

@RestController
@RequestMapping("/api/v1/estimation/product")
public class ProductEstimationController {

    private final ProductEstimationService productEstimationService;

    private final MessageSource messageSource;

    @Autowired
    public ProductEstimationController(ProductEstimationService productEstimationService,
                                       MessageSource messageSource) {
        this.productEstimationService = productEstimationService;
        this.messageSource = messageSource;
    }

    @PreAuthorize("hasAuthority('USER')")
    @PostMapping
    public ProductEstimationInfo estimate(@RequestBody @Valid ProductEstimationDto productEstimation,
                                          @AuthenticationPrincipal StokerUserDetails userDetails) {
        return productEstimationService.estimate(userDetails.getId(), productEstimation);
    }

    @GetMapping("/{productId}/{userId}")
    public ProductEstimationInfo getById(@PathVariable Long productId,
                                         @PathVariable Long userId) {
        return productEstimationService.getById(userId, productId);
    }

    @GetMapping("/all")
    public List<ProductEstimationInfo> getAllProductEstimations(@RequestParam Long productId) {
        return productEstimationService.getAll(productId);
    }

    @PreAuthorize("hasAuthority('USER')")
    @PutMapping
    public ProductEstimationInfo update(@RequestBody @Valid ProductEstimationDto productEstimationDto,
                                        @AuthenticationPrincipal StokerUserDetails userDetails) {
        return productEstimationService.update(productEstimationDto, userDetails.getId());
    }

    @PreAuthorize("hasAuthority('USER')")
    @DeleteMapping("/{productId}")
    public void deleteByProductId(@PathVariable Long productId,
                                  @AuthenticationPrincipal StokerUserDetails userDetails) {
        productEstimationService.deleteByProductId(userDetails.getId(), productId);
    }

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(UserSelfEstimationException.class)
    public String handleUserSelfEstimationException(Locale locale) {
        return messageSource.getMessage(USER_SELF_ESTIMATION, null, locale);
    }

    @ResponseStatus(CONFLICT)
    @ExceptionHandler(AlreadyExistException.class)
    public String handleAlreadyExistException(AlreadyExistException ex, Locale locale) {
        return messageSource.getMessage(USER_ALREADY_ESTIMATED,
                new Object[]{ ex.getProductId() }, locale);
    }

    @ResponseStatus(NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public String handleNotFoundException(NotFoundException ex, Locale locale) {
        return messageSource.getMessage(PRODUCT_ESTIMATION_NOT_FOUND,
                new Object[]{ ex.getUserId(), ex.getProductId() }, locale);
    }

    @ResponseStatus(NOT_FOUND)
    @ExceptionHandler(ProductEx.NotFoundException.class)
    public String handleProductNotFoundException(ProductEx.NotFoundException ex, Locale locale) {
        return messageSource.getMessage(ProductEx.NotFoundException.PRODUCT_NOT_FOUND,
                new Object[]{ ex.getId() }, locale);
    }

}
