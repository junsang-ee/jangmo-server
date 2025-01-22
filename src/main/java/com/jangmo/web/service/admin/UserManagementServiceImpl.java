package com.jangmo.web.service.admin;

import com.jangmo.web.config.sms.SmsProvider;
import com.jangmo.web.constants.MercenaryStatus;
import com.jangmo.web.constants.SmsType;
import com.jangmo.web.constants.message.ErrorMessage;
import com.jangmo.web.exception.custom.AuthException;
import com.jangmo.web.exception.custom.InvalidStateException;
import com.jangmo.web.exception.custom.NotFoundException;
import com.jangmo.web.model.entity.user.MercenaryCodeEntity;
import com.jangmo.web.model.entity.user.MercenaryEntity;
import com.jangmo.web.repository.MercenaryCodeRepository;
import com.jangmo.web.repository.MercenaryRepository;
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

    private final MercenaryCodeRepository mercenaryCodeRepository;

    private final SmsProvider smsProvider;

    @Override
    @Transactional
    public void approveMercenary(String mercenaryId) {
        MercenaryEntity mercenary = mercenaryRepository.findById(mercenaryId).orElseThrow(
                () -> new NotFoundException(ErrorMessage.MERCENARY_NOT_FOUND)
        );
        log.info("mobile1 :: " + mercenary.getMobile());

        MercenaryStatus status = mercenary.getStatus();

        if (status == MercenaryStatus.DISABLED)
            throw new AuthException(ErrorMessage.AUTH_DISABLED);

        MercenaryCodeEntity codeEntity =
                mercenaryCodeRepository.findByMercenary(mercenary).orElseGet(() -> null);

        if (codeEntity != null) {
            if (status == MercenaryStatus.ENABLED) {
                throw new InvalidStateException(ErrorMessage.MERCENARY_ALREADY_ENABLED);
            }
            throw new InvalidStateException(ErrorMessage.MERCENARY_ALREADY_CODE_EXISTS);
        }

        if (status == MercenaryStatus.EXPIRED)
            throw new InvalidStateException(ErrorMessage.MERCENARY_NEEDS_CODE_REISSUE);
        else if (status == MercenaryStatus.ENABLED)
            throw new InvalidStateException(ErrorMessage.MERCENARY_NEEDS_STATUS);

        mercenary.updateStatus(MercenaryStatus.ENABLED);

        String mercenaryCode = CodeGeneratorUtil.getMercenaryCode();

        MercenaryCodeEntity mercenaryCodeEntity = MercenaryCodeEntity.create(
                mercenary,
                mercenaryCode
        );
        mercenaryCodeRepository.save(mercenaryCodeEntity);
        log.info("mobile2 :: " + mercenary.getMobile());
        smsProvider.send(mercenary.getMobile(), mercenaryCode, SmsType.MERCENARY);
    }

}
