package com.zerobase.cms.user.client.service;

import com.zerobase.cms.user.client.MailgunClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class EmailSendServiceTest {

    @Autowired
    private MailgunClient mailgunClient;

    @Test
    void testSendEmail() {
        // given
        // when

        // need test code
        mailgunClient.sendEmail(null);

        // then
    }
}