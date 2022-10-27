package ru.stoker.database.entity.embeddable;

import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
@Embeddable
public class FavoriteId implements Serializable {

    @NotNull(message = "Id пользователя не должен быть пустым")
    @Column(name = "user_id")
    private Long userId;

    @NotNull(message = "Id объявления не должен быть пустым")
    @Column(name = "advertisement_id")
    private Long advertisementId;

}
