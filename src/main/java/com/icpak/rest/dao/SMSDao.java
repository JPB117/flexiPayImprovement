package com.icpak.rest.dao;

import com.icpak.rest.models.sms.FailureReason;
import com.icpak.rest.models.sms.SMSLog;
import com.icpak.rest.models.sms.SmsStatus;

public class SMSDao extends BaseDao {

	public void updateSMSStatus(String messageId, String status, String failureReason) {
		SMSLog sms = findSMSLogById(messageId);
		sms.setStatus(SmsStatus.valueOf(status.toUpperCase()));
		if (!failureReason.isEmpty()) {
			sms.setFailureReason(FailureReason.valueOf(failureReason));
		}
		save(sms);
		System.err.println("Successfully updated!");
	}

	public SMSLog findSMSLogById(String smsId) {
		assert smsId != null;
		String query = "from SMSLog s where s.smsId = :smsId";
		return getSingleResultOrNull(getEntityManager().createQuery(query).setParameter("smsId", smsId));
	}
}
