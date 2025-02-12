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
import com.jangmo.web.repository.*;

import com.jangmo.web.utils.CodeGeneratorUtil;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserManagementServiceImpl implements UserManagementService {

    private final MercenaryRepository mercenaryRepository;

    private final MercenaryTransientRepository mercenaryTransientRepository;

    private final MemberRepository memberRepository;

    private final MatchRepository matchRepository;

    private final SmsProvider smsProvider;


    @Override
    @Transactional
    public void approveMercenary(String mercenaryId, String matchId) {
        MercenaryEntity mercenary = mercenaryRepository.findById(mercenaryId).orElseThrow(
                () -> new NotFoundException(ErrorMessage.MERCENARY_NOT_FOUND)
        );

        MatchEntity match = matchRepository.findById(matchId).orElseThrow(
                () -> new NotFoundException(ErrorMessage.MATCH_NOT_FOUND)
        );

        MercenaryTransientEntity transientEntity =
                mercenaryTransientRepository.findByMercenary(mercenary).orElseGet(() -> null);

        if (transientEntity != null) {
            if (transientEntity.getMatch() != null)
                throw new InvalidStateException(ErrorMessage.MERCENARY_MATCH_ALREADY_EXISTS);

            if (transientEntity.getCode() != null)
                throw new InvalidStateException(ErrorMessage.MERCENARY_CODE_ALREADY_EXISTS);
        }

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
        switch (member.getStatus()) {
            case DISABLED:
                throw new AuthException(ErrorMessage.AUTH_DISABLED);
            case RETIRED:
                throw new AuthException(ErrorMessage.AUTH_RETIRED);
        }
        member.updateStatus(MemberStatus.ENABLED);
    }

    private void activateMercenary(MercenaryEntity mercenary, MatchEntity match) {
        String mercenaryCode = CodeGeneratorUtil.getMercenaryCode();
        MercenaryTransientEntity mercenaryTransient = MercenaryTransientEntity.create(
                mercenary,
                mercenaryCode,
                match
        );
        mercenaryTransientRepository.save(mercenaryTransient);
        smsProvider.send(
                mercenary.getMobile(),
                mercenaryCode,
                SmsType.MERCENARY
        );
    }
}
