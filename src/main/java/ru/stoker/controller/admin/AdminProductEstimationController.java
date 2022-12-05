package ru.stoker.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.stoker.dto.productestimaton.ProductEstimationIdDto;
import ru.stoker.dto.productestimaton.UpdateProductEstimation;
import ru.stoker.dto.productestimaton.ProductEstimationInfo;
import ru.stoker.exceptions.ProductEstimationEx;
import ru.stoker.service.productestimation.ProductEstimationService;

import javax.validation.Valid;
import java.util.Locale;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static ru.stoker.exceptions.ProductEstimationEx.NotFoundException.PRODUCT_ESTIMATION_NOT_FOUND;
import static ru.stoker.exceptions.ProductEstimationEx.UserSelfEstimationException.USER_SELF_ESTIMATION_UPDATE;

@PreAuthorize("hasAuthority('ADMIN')")
@RestController
@RequestMapping("/api/v1/admin/estimation/product")
public class AdminProductEstimationController {

    private final ProductEstimationService productEstimationService;

    private final MessageSource messageSource;

    @Autowired
    public AdminProductEstimationController(ProductEstimationService productEstimationService,
                                            MessageSource messageSource) {
        this.productEstimationService = productEstimationService;
        this.messageSource = messageSource;
    }

    @PutMapping
    public ProductEstimationInfo update(@RequestBody @Valid UpdateProductEstimation productEstimationDto) {
        return productEstimationService.update(productEstimationDto, productEstimationDto.getUserId());
    }

    @DeleteMapping
    public void deleteById(@RequestBody @Valid ProductEstimationIdDto id) {
        productEstimationService.deleteByProductId(id.getUserId(), id.getProductId());
    }

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(ProductEstimationEx.UserSelfEstimationException.class)
    public String handleUserSelfEstimationException(ProductEstimationEx.UserSelfEstimationException ex,
                                                    Locale locale) {
        return messageSource.getMessage(USER_SELF_ESTIMATION_UPDATE,
                new Object[]{ ex.getUserId(), ex.getProductId() }, locale);
    }

    @ResponseStatus(NOT_FOUND)
    @ExceptionHandler(ProductEstimationEx.NotFoundException.class)
    public String handleNotFoundException(ProductEstimationEx.NotFoundException ex, Locale locale) {
        return messageSource.getMessage(PRODUCT_ESTIMATION_NOT_FOUND,
                new Object[]{ ex.getUserId(), ex.getProductId() }, locale);
    }

}
