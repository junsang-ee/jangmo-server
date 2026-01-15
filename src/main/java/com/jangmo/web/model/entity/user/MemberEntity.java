package com.jangmo.web.model.entity.user;

import com.jangmo.web.constants.Gender;
import com.jangmo.web.constants.user.MemberStatus;
import com.jangmo.web.constants.UserRole;

import com.jangmo.web.model.entity.UniformEntity;
import com.jangmo.web.model.entity.administrative.City;
import com.jangmo.web.model.entity.administrative.District;
import com.jangmo.web.model.entity.api.KakaoApiUsageEntity;

import com.jangmo.web.validation.DomainPreconditions;

import lombok.Getter;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.time.LocalDate;

import static lombok.AccessLevel.PROTECTED;

@Getter
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
		cascade = {CascadeType.REMOVE, CascadeType.PERSIST},
		orphanRemoval = true)
	private KakaoApiUsageEntity kakaoApiUsage;

	private MemberEntity(
		String name,
		String mobile,
		Gender gender,
		LocalDate birth,
		String password,
		City city,
		District district
	) {
		super(name, mobile, UserRole.MEMBER, gender);
		this.birth = birth;
		this.password = password;
		this.status = MemberStatus.PENDING;
		this.city = city;
		this.district = district;
		this.uniform = null;
		this.kakaoApiUsage = null;
	}

	public static MemberEntity create(
		final String name,
		final String mobile,
		final Gender gender,
		final LocalDate birth,
		final String encodedPassword,
		final City city,
		final District district
	) {
		validate(name, mobile, gender, birth, encodedPassword);

		return new MemberEntity(
			name, mobile, gender, birth, encodedPassword, city, district
		);
	}

	public void updateStatus(MemberStatus status) {
		this.status = status;
	}

	public void updatePassword(String newEncodedPassword) {
		this.password = newEncodedPassword;
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

	private static void validate(
		String name,
		String mobile,
		Gender gender,
		LocalDate birth,
		String password
	) {
		DomainPreconditions.validate(
			StringUtils.hasText(name), "회원 이름은 비어있을 수 없습니다."
		);
		DomainPreconditions.validate(
			StringUtils.hasText(mobile), "휴대폰번호는 비어있을 수 없습니다."
		);
		DomainPreconditions.validate(
			gender != null, "성별은 필수 항목입니다."
		);
		DomainPreconditions.validate(
			birth != null, "생년월일은 필수 항목입니다."
		);
		DomainPreconditions.validate(
			StringUtils.hasText(password), "비밀번호는 비어있을 수 없습니다."
		);
	}
}
