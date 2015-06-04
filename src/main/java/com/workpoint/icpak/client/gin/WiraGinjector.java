package com.workpoint.icpak.client.gin;

import com.google.gwt.inject.client.GinModules;
import com.google.gwt.inject.client.Ginjector;
import com.gwtplatform.dispatch.rest.client.gin.RestDispatchAsyncModule;
import com.gwtplatform.dispatch.rpc.client.gin.RpcDispatchAsyncModule;

//
@GinModules(properties = {"gin.ginjector.modules"},
		value = {ClientModule.class})
public interface WiraGinjector extends Ginjector {
	
}