package com.jangmo.web.model.entity.user;

import com.jangmo.web.constants.Gender;
import com.jangmo.web.constants.user.MemberStatus;
import com.jangmo.web.constants.UserRole;
import com.jangmo.web.model.dto.request.MemberSignUpRequest;

import com.jangmo.web.model.entity.UniformEntity;
import com.jangmo.web.model.entity.administrative.City;
import com.jangmo.web.model.entity.administrative.District;
import com.jangmo.web.model.entity.api.KakaoApiUsageEntity;
import com.jangmo.web.utils.EncryptUtil;

import lombok.Getter;
import lombok.ToString;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import java.io.Serializable;
import java.time.LocalDate;

import static lombok.AccessLevel.PROTECTED;

@Getter
@ToString
@NoArgsConstructor(access = PROTECTED)
@Entity(name = "member")
public class MemberEntity extends UserEntity implements Serializable {

    @Column(nullable = false)
    private LocalDate birth;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private MemberStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "city", nullable = false)
    private City city;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "district", nullable = false)
    private District district;

    @OneToOne(fetch = FetchType.LAZY,
            cascade = {CascadeType.REMOVE, CascadeType.PERSIST},
            orphanRemoval = true)
    @JoinColumn(name = "uniform")
    private UniformEntity uniform;

    @OneToOne(mappedBy = "apiCaller",
            fetch = FetchType.LAZY,
            cascade = {CascadeType.REMOVE, CascadeType.PERSIST},
            orphanRemoval = true)
    private KakaoApiUsageEntity kakaoApiUsage;

    private MemberEntity(String name, String mobile,
                         Gender gender, LocalDate birth,
                         String password, City city, District district) {
        super(name, mobile, UserRole.MEMBER, gender);
        this.birth = birth;
        this.password = password;
        this.status = MemberStatus.PENDING;
        this.city = city;
        this.district = district;
        this.uniform = null;
        this.kakaoApiUsage = null;
    }

    public static MemberEntity create(final MemberSignUpRequest signup,
                                      final City city,
                                      final District district) {
        return new MemberEntity(
                signup.getName(),
                signup.getMobile(),
                signup.getGender(),
                signup.getBirth(),
                EncryptUtil.encode(signup.getPassword()),
                city,
                district
        );
    }

    public void updateStatus(MemberStatus status) {
        this.status = status;
    }

    public void updatePassword(String newPassword) {
        this.password = EncryptUtil.encode(newPassword);
    }

    public void registerUniform(int backNumber) {
        this.uniform = UniformEntity.create(backNumber);
    }

    public void updateAddress(City city, District district) {
        this.city = city;
        this.district = district;
    }

    public void createKakaoApiUsage() {
        this.kakaoApiUsage = KakaoApiUsageEntity.create(this);
    }
}
