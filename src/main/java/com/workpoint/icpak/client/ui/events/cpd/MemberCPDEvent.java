package com.workpoint.icpak.client.ui.events.cpd;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.workpoint.icpak.shared.model.MemberDto;

public class MemberCPDEvent extends GwtEvent<MemberCPDEvent.MemberCPDHandler> {
	private static Type<MemberCPDHandler> TYPE = new Type<MemberCPDHandler>();

	public interface MemberCPDHandler extends EventHandler {
		void onMemberCPD(MemberCPDEvent event);
	}

	private final String message;
	private MemberDto member = null;

	public MemberCPDEvent(final String message) {
		this.message = message;
	}

	public MemberCPDEvent(MemberDto member) {
		this("");
		this.member = member;
	}

	public static Type<MemberCPDHandler> getType() {
		return TYPE;
	}

	@Override
	protected void dispatch(final MemberCPDHandler handler) {
		handler.onMemberCPD(this);
	}

	@Override
	public Type<MemberCPDHandler> getAssociatedType() {
		return TYPE;
	}

	public String getMessage() {
		return this.message;
	}

	public MemberDto getMember() {
		return member;
	}
}
