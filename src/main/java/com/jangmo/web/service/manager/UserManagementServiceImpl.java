package com.jangmo.web.service.manager;

import com.jangmo.web.config.sms.SmsProvider;
import com.jangmo.web.constants.MemberStatus;
import com.jangmo.web.constants.MercenaryRetentionStatus;
import com.jangmo.web.constants.MercenaryStatus;
import com.jangmo.web.constants.SmsType;
import com.jangmo.web.constants.message.ErrorMessage;
import com.jangmo.web.exception.custom.AuthException;
import com.jangmo.web.exception.custom.InvalidStateException;
import com.jangmo.web.exception.custom.NotFoundException;
import com.jangmo.web.model.entity.MatchEntity;
import com.jangmo.web.model.entity.user.MemberEntity;
import com.jangmo.web.model.entity.user.MercenaryEntity;
import com.jangmo.web.model.entity.user.MercenaryTransientEntity;
import com.jangmo.web.model.entity.user.UserEntity;

import com.jangmo.web.repository.MercenaryRepository;
import com.jangmo.web.repository.MercenaryTransientRepository;
import com.jangmo.web.repository.MemberRepository;
import com.jangmo.web.repository.MatchRepository;
import com.jangmo.web.repository.UserRepository;

import com.jangmo.web.utils.CodeGeneratorUtil;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserManagementServiceImpl implements UserManagementService {

    private final MercenaryRepository mercenaryRepository;

    private final MercenaryTransientRepository mercenaryTransientRepository;

    private final MemberRepository memberRepository;

    private final MatchRepository matchRepository;

    private final UserRepository userRepository;

    private final SmsProvider smsProvider;


    @Override
    @Transactional
    public void approveMercenary(String mercenaryId, String matchId) {
        MercenaryEntity mercenary = mercenaryRepository.findById(mercenaryId).orElseThrow(
                () -> new NotFoundException(ErrorMessage.MERCENARY_NOT_FOUND)
        );
        if (mercenary.getMercenaryTransient() != null)
            throw new InvalidStateException(ErrorMessage.MERCENARY_ALREADY_TRANSIENT_EXISTS);

        MatchEntity match = matchRepository.findById(matchId).orElseThrow(
                () -> new NotFoundException(ErrorMessage.MATCH_NOT_FOUND)
        );

        switch (mercenary.getStatus()) {
            case DISABLED:
                throw new AuthException(ErrorMessage.AUTH_DISABLED);
            case EXPIRED:
                if (mercenary.getRetentionStatus() == MercenaryRetentionStatus.KEEP)
                    throw new AuthException(ErrorMessage.AUTH_MERCENARY_EXPIRED);
                else
                    throw new AuthException(ErrorMessage.AUTH_MERCENARY_PRIVACY);
            case ENABLED:
                throw new AuthException(ErrorMessage.AUTH_ALREADY_ENABLED);
            default: break;
        }

        mercenary.updateStatus(MercenaryStatus.ENABLED);
        activateMercenary(mercenary, match);
    }

    @Override
    @Transactional
    public void approveMember(String memberId) {
        MemberEntity member = memberRepository.findById(memberId).orElseThrow(
                () -> new NotFoundException(ErrorMessage.MEMBER_NOT_FOUND)
        );
        if (member.getStatus() == MemberStatus.DISABLED) {
            throw new AuthException(ErrorMessage.AUTH_DISABLED);
        }
        member.updateStatus(MemberStatus.ENABLED);
    }

    @Override
    public List<UserEntity> getApprovalUsers() {
        return userRepository.findApprovalUsers();
    }

    @Transactional
    private void activateMercenary(MercenaryEntity mercenary, MatchEntity match) {
        String mercenaryCode = CodeGeneratorUtil.getMercenaryCode();
        MercenaryTransientEntity mercenaryTransient = MercenaryTransientEntity.create(
                mercenaryCode,
                match
        );
        mercenaryTransientRepository.save(mercenaryTransient);
        mercenary.updateTransient(mercenaryTransient);
        smsProvider.send(
                mercenary.getMobile(),
                mercenaryCode,
                SmsType.MERCENARY
        );
    }
}
