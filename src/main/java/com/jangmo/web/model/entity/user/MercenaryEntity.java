package com.jangmo.web.model.entity.user;

import com.jangmo.web.constants.*;
import com.jangmo.web.model.dto.request.MercenaryRegistrationRequest;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
@Entity(name = "mercenary")
public class MercenaryEntity extends UserEntity {

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private MercenaryRetentionStatus retentionStatus;

    @Column(nullable = false)
    private MercenaryStatus status;

    private MercenaryEntity(String name, String mobile,
                            UserRole role, Gender gender,
                            MercenaryRetentionStatus retentionStatus) {
        super(name, mobile, role, gender);
        this.retentionStatus = retentionStatus;
        this.status = MercenaryStatus.PENDING;
    }

    public static MercenaryEntity create(final MercenaryRegistrationRequest registration) {
        return new MercenaryEntity(
                registration.getName(),
                registration.getMobile(),
                UserRole.MERCENARY,
                registration.getGender(),
                registration.getRetentionStatus()
        );
    }

    public void updateStatus(MercenaryStatus status) {
        this.status = status;
    }




}
