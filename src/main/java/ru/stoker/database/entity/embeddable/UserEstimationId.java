package ru.stoker.database.entity.embeddable;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@EqualsAndHashCode(callSuper = false)
@Embeddable
public class UserEstimationId implements Serializable {

    @NotNull(message = "Id оценивающего пользователя не должен быть пустым")
    @Column(name = "estimating_user_id")
    private Long estimatingUserId;

    @NotNull(message = "Id оцениваемого пользователя не должен быть пустым")
    @Column(name = "estimated_user_id")
    private Long estimatedUserId;

}
