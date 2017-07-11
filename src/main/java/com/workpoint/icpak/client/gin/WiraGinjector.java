package com.workpoint.icpak.client.gin;

import com.google.gwt.inject.client.GinModules;
import com.google.gwt.inject.client.Ginjector;

//
@GinModules(properties = { "gin.ginjector.modules" }, value = { ClientModule.class })
public interface WiraGinjector extends Ginjector {
}