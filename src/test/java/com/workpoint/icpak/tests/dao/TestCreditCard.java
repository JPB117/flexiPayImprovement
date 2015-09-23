package com.workpoint.icpak.tests.dao;

import org.apache.http.entity.StringEntity;
import org.junit.Test;

import com.google.inject.Inject;
import com.workpoint.icpak.server.payment.CreditCardServiceImpl;
import com.workpoint.icpak.shared.model.CreditCardDto;
import com.workpoint.icpak.shared.model.CreditCardResponse;
import com.workpoint.icpak.tests.base.AbstractDaoTest;

/**
 * Created by wladek on 9/17/15.
 */
public class TestCreditCard extends AbstractDaoTest{

    @Inject
    CreditCardServiceImpl creditCardService;

    @Test
    public void testCreditCard() throws Exception{
        CreditCardDto creditCardDto = new CreditCardDto();
        StringEntity  e;
        creditCardDto.setCard_holder_name("5646544");
        creditCardDto.setCard_number("5646544");
        creditCardDto.setAmount("5646544");
        creditCardDto.setExpiry("5646544");
        creditCardDto.setSecurity_code("5646544");

        CreditCardResponse result = creditCardService.authorizeCardTransaction(creditCardDto);


        System.out.println(">> "+result.toString());
    }
}