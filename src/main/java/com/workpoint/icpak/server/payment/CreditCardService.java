package com.workpoint.icpak.server.payment;

import java.io.CharConversionException;

import com.amazonaws.util.json.JSONException;
import com.workpoint.icpak.shared.model.CreditCardDto;

/**
 * Created by achachiez on 02/03/15.
 */
public interface CreditCardService {
//	public void RequestPayment(CreditCardDto cardDetails)
//			throws JSONException, CharConversionException,
//			CardAuthorizationException;

	CreditCardDto setInitials(CreditCardDto creditCardDto);
}
