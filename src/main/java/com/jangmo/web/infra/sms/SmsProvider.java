package com.jangmo.web.infra.sms;

import com.jangmo.web.config.properties.SmsProperties;
import com.jangmo.web.constants.SmsType;
import lombok.RequiredArgsConstructor;
import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@RequiredArgsConstructor
@Component
public class SmsProvider {

    private final SmsProperties smsProperties;

    private static final String SMS_API_ENDPOINT = "https://api.coolsms.co.kr";

    private DefaultMessageService messageService;

    @PostConstruct
    public void init() {
        messageService = NurigoApp.INSTANCE.initialize(
                smsProperties.key(),
                smsProperties.secret(),
                SMS_API_ENDPOINT
        );
    }

    public void send(String to, String code, SmsType type) {
        String content = "";
        if (type == SmsType.AUTH_CODE) {
            content = smsProperties.authContent().replace(
                    "{authCode}", code
            );
        } else if (type == SmsType.MERCENARY_CODE){
            content = smsProperties.mercenaryContent().replace(
                    "{mercenaryCode}", code
            );
        }

        Message message = new Message();
        message.setFrom(smsProperties.sender());
        message.setTo(to);
        message.setText(content);

        messageService.sendOne(
                new SingleMessageSendingRequest(message)
        );
    }
}
