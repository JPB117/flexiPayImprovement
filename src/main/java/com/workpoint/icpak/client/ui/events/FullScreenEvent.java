package com.workpoint.icpak.client.ui.events;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

public class FullScreenEvent extends GwtEvent<FullScreenEvent.FullScreenHandler> {
    private static Type<FullScreenHandler> TYPE = new Type<FullScreenHandler>();
    
    public interface FullScreenHandler extends EventHandler {
        void onFullScreen(FullScreenEvent event);
    }
    
    
    private final String message;
   
	public FullScreenEvent(final String message) {
        this.message = message;
    }

    public static Type<FullScreenHandler> getType() {
        return TYPE;
    }

    @Override
    protected void dispatch(final FullScreenHandler handler) {
        handler.onFullScreen(this);
    }

    @Override
    public Type<FullScreenHandler> getAssociatedType() {
        return TYPE;
    }
    
    public String getMessage() {
        return this.message;
    }
}