package com.jangmo.web.service.sms;

import com.jangmo.web.config.sms.SmsConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Slf4j
@RequiredArgsConstructor
@Service
public class SmsService {

    private final SmsConfig smsConfig;

    private static final String provider = "https://api.coolsms.co.kr";

    private DefaultMessageService messageService;

    @PostConstruct
    public void init() {
        messageService = NurigoApp.INSTANCE.initialize(
                smsConfig.getKey(),
                smsConfig.getSecret(),
                provider
        );
    }

    public void send(String to, String code) {
        log.info("to : " + to);
        log.info("to : " + code);
        String content = smsConfig.getContent().replace("{authCode}", code);

        Message message = new Message();
        message.setFrom(smsConfig.getSender());
        message.setTo(to);
        message.setText(content);

        messageService.sendOne(
                new SingleMessageSendingRequest(message)
        );
    }


}
