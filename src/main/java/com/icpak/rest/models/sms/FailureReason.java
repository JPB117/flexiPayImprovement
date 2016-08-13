package com.icpak.rest.models.sms;

public enum FailureReason {
	
	InsufficientCredit("InsufficientCredit","This occurs when the subscriber don't have enough airtime for a premium subscription service/message"),
	InvalidLinkId("InvalidLinkId", "This occurs when a message is sent with an invalid linkId for an onDemand service"),
	UserIsInactive("UserIsInactive","This occurs when the subscriber is inactive or the account deactivated by the MSP (Mobile Service Provider)."),
	UserInBlackList("User In BlackList","This would occur if the user has been blacklisted not to receive messages from a paricular service (shortcode or keyword)"),
	UserAccountSuspended("UserAccountSuspended","This would occur when the mobile subscriber has been suspended by the MSP."),
	NotNetworkSubcribe("Not NetworkSubcribe","This occurs when the message is passed to an MSP where the subscriber doesn't belong."),
	UserNotSubscribedToProduct("UserNotSubscribedToProduct","This is for a subscription product which the subscriber has not subscribed to."),
	UserDoesNotExist("UserDoesNotExist","This occurs when the message is sent to a non-existent mobile number."),
	DeliveryFailure("DeliveryFailure","This occurs when message delivery fails for any reason not listed above or where the MSP didn't provide a delivery failure reason.");
	
	private String displayName;
	private String description;

	private FailureReason(String displayName, String description){
		this.displayName = displayName;
		this.description = description;
	}
	
	public static FailureReason getStatus(String name){
		for(FailureReason status: FailureReason.values()){
			if(status.displayName.equals(name)){
				return status;
			}
		}
		return null;
	}
	
	public String getDisplayName() {
		return displayName;
	}
	
	public String getDescription() {
		return description;
	}
	
}
