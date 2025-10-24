package com.jangmo.web.infra.sms;


import com.jangmo.web.config.properties.SmsProperties;
import com.jangmo.web.constants.SmsType;
import com.jangmo.web.utils.CodeGeneratorUtil;
import lombok.extern.slf4j.Slf4j;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@Slf4j
@ExtendWith(MockitoExtension.class)
public class SmsProviderTest {

    @Mock
    private SmsProperties smsProperties;

    @Mock
    private DefaultMessageService messageService;

    @InjectMocks
    private SmsProvider smsProvider;

    @Captor
    ArgumentCaptor<SingleMessageSendingRequest> captor;

    @BeforeEach
    void setUp() {
        when(smsProperties.secret()).thenReturn("01012341111");
        ReflectionTestUtils.setField(smsProvider, "messageService", messageService);
    }

    @DisplayName("인증코드 전송 테스트")
    @Test
    void sendAuthCodeTest() {
        //given
        String to = "01012349999";
        String differentTo = "01012348888";
        String code = "123123";
        String expectedContent = "인증번호는 123123 입니다.";

        //when
        when(smsProperties.authContent()).thenReturn("인증번호는 {authCode} 입니다.");
        smsProvider.send(to, code, SmsType.AUTH_CODE);
        log.info("smsConfig authContent : {}", smsProperties.authContent().replace("{authCode}", code));
        log.info("expectedContent : {} ", expectedContent);

        //then captor (capture)
        verify(messageService).sendOne(captor.capture());
        SingleMessageSendingRequest actualRequest = captor.getValue();
        Message sentMessage = actualRequest.getMessage();

        //verify
        assertEquals(to, sentMessage.getTo(), "수신 번호가 일치하지 않습니다");
        assertEquals(expectedContent, sentMessage.getText(), "문자 본문이 기대와 다릅니다");
    }


    @DisplayName("용병코드 전송 테스트")
    @Test
    void sendMercenaryCodeTest() {
        //given
        String to = "01012349999";
        String code = CodeGeneratorUtil.getMercenaryCode();
        String expectedContent = "용병코드는 " + code + " 입니다.";

        //when
        when(smsProperties.mercenaryContent()).thenReturn("용병코드는 {mercenaryCode} 입니다.");
        smsProvider.send(to, code, SmsType.MERCENARY_CODE);
        log.info("smsConfig mercenaryContent : {}", smsProperties.mercenaryContent().replace("{mercenaryCode}", code));
        log.info("expectedContent : {} ", expectedContent);

        //then captor (capture)
        verify(messageService).sendOne(captor.capture());
        SingleMessageSendingRequest actualRequest = captor.getValue();
        Message sentMessage = actualRequest.getMessage();

        //verify
        assertEquals(to, sentMessage.getTo(), "수신 번호가 일치하지 않습니다");
        assertEquals(expectedContent, sentMessage.getText(), "문자 본문이 기대와 다릅니다");
    }

}
