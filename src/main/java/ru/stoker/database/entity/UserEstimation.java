package ru.stoker.database.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import ru.stoker.database.entity.embeddable.Estimation;
import ru.stoker.database.entity.embeddable.UserEstimationId;

import javax.persistence.*;
import javax.validation.Valid;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "user_estimations")
public class UserEstimation {

    @Getter
    @Valid
    @EmbeddedId
    private UserEstimationId userEstimationId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "estimating_user_id", updatable = false, insertable = false,
            foreignKey = @ForeignKey(name = "fk_user_esting_by_user_id"))
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User estimatingUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "estimated_user_id", updatable = false, insertable = false,
            foreignKey = @ForeignKey(name = "fk_user_ested_user_user_id"))
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User estimatedUser;

    @Getter
    @Setter
    @Valid
    @Embedded
    private Estimation estimation;

    public UserEstimation(Long estimatingUserId, Long estimatedUserId, Estimation estimation) {
        userEstimationId = new UserEstimationId(estimatingUserId, estimatedUserId);
        this.estimation = estimation;
    }

}
