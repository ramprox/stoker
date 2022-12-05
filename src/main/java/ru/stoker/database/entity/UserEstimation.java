package ru.stoker.database.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.stoker.database.entity.embeddable.Estimation;
import ru.stoker.database.entity.embeddable.UserEstimationId;

import javax.persistence.Embedded;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "user_estimations")
public class UserEstimation {

    @EmbeddedId
    private UserEstimationId id;

    @Embedded
    private Estimation estimation;

    public UserEstimation(UserEstimationId id, Estimation estimation) {
        this.id = id;
        this.estimation = estimation;
    }

}
