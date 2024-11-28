package com.jangmo.web.model.entity.user;

import com.jangmo.web.constants.Gender;
import com.jangmo.web.constants.MobileCarrierType;
import com.jangmo.web.constants.UserRole;
import com.jangmo.web.model.dto.request.MemberSignUpRequest;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;

import java.io.Serializable;

import static lombok.AccessLevel.PROTECTED;

@Getter
@ToString
@NoArgsConstructor(access = PROTECTED)
@Entity(name = "member")
public class MemberEntity extends UserEntity implements Serializable {

    @Column(nullable = false)
    private int birth;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String address;

    private MemberEntity(String name, String mobile,
                         MobileCarrierType mobileCarrier,
                         UserRole role, Gender gender, int birth,
                         String password, String address) {
        super(name, mobile, mobileCarrier, role, gender);
        this.birth = birth;
        this.password = password;
        this.address = address;
    }

    public static MemberEntity create(final MemberSignUpRequest signup) {
        return new MemberEntity(
                signup.getName(),
                signup.getMobile(),
                signup.getMobileCarrier(),
                signup.getRole(),
                signup.getGender(),
                signup.getBirth(),
                signup.getPassword(),
                signup.getAddress()
        );
    }
}
