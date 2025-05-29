package com.jangmo.web.infra.sms;

import com.jangmo.web.config.sms.SmsConfig;
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

    private final SmsConfig smsConfig;

    private static final String SMS_API_ENDPOINT = "https://api.coolsms.co.kr";

    private DefaultMessageService messageService;

    @PostConstruct
    public void init() {
        messageService = NurigoApp.INSTANCE.initialize(
                smsConfig.getKey(),
                smsConfig.getSecret(),
                SMS_API_ENDPOINT
        );
    }

    public void send(String to, String code, SmsType type) {
        String content = "";
        if (type == SmsType.AUTH_CODE) {
            content = smsConfig.getAuthContent().replace(
                    "{authCode}", code
            );
        } else if (type == SmsType.MERCENARY_CODE){
            content = smsConfig.getMercenaryContent().replace(
                    "{mercenaryCode}", code
            );
        }

        Message message = new Message();
        message.setFrom(smsConfig.getSender());
        message.setTo(to);
        message.setText(content);

        messageService.sendOne(
                new SingleMessageSendingRequest(message)
        );
    }
}
