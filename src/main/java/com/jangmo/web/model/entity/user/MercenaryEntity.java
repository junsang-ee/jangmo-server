package com.jangmo.web.model.entity.user;

import com.jangmo.web.constants.MercenaryRetentionStatus;
import com.jangmo.web.constants.user.MercenaryStatus;
import com.jangmo.web.constants.UserRole;
import com.jangmo.web.constants.Gender;
import com.jangmo.web.model.dto.request.MercenaryRegistrationRequest;

import lombok.Getter;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
@Entity(name = "mercenary")
public class MercenaryEntity extends UserEntity {

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private MercenaryRetentionStatus retentionStatus;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private MercenaryStatus status;

	@OneToOne(cascade = CascadeType.REMOVE,
		orphanRemoval = true,
		fetch = FetchType.LAZY)
	@JoinColumn(name = "mercenary_transient")
	private MercenaryTransientEntity mercenaryTransient;

	private MercenaryEntity(
		String name,
		String mobile,
		UserRole role,
		Gender gender,
		MercenaryRetentionStatus retentionStatus
	) {
		super(name, mobile, role, gender);
		this.retentionStatus = retentionStatus;
		this.status = MercenaryStatus.PENDING;
		this.mercenaryTransient = null;
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

	public void updateTransient(MercenaryTransientEntity mercenaryTransient) {
		this.mercenaryTransient = mercenaryTransient;
	}

}
