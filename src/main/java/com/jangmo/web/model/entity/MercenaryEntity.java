package com.jangmo.web.model.entity;

import com.jangmo.web.constants.Gender;
import com.jangmo.web.constants.MobileCarrierType;
import com.jangmo.web.constants.UserRole;
import com.jangmo.web.model.dto.request.MercenaryRegistrationRequest;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;

import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
@Entity(name = "mercenary")
public class MercenaryEntity extends AbstractUserEntity {

    @Builder
    protected MercenaryEntity(String name, int phoneNumber,
                              MobileCarrierType mobileCarrierType,
                              UserRole role, Gender gender) {
        super(name, phoneNumber, mobileCarrierType, role, gender);
    }

    public static MercenaryEntity of(final MercenaryRegistrationRequest request) {
        return MercenaryEntity.builder()
                .name(request.getName())
                .phoneNumber(request.getPhoneNumber())
                .mobileCarrierType(request.getMobileCarrier())
                .role(request.getRole())
                .gender(request.getGender())
                .build();
    }


}
