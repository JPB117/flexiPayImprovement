package com.workpoint.icpak.client.ui.home;

import com.gwtplatform.mvp.client.proxy.Gatekeeper;
import com.workpoint.icpak.client.ui.admin.TabDataExt;

public class HomeTabData extends TabDataExt{

	String itemStyle=null;
	String key=null;
	public HomeTabData(String key,String label,String itemStyle, float priority,
			Gatekeeper gateKeeper) {
		super(label, "", priority, gateKeeper);
		this.itemStyle=itemStyle;
		this.key=key;
	}
	
	public HomeTabData(String key,String label,String itemStyle, float priority,
			Gatekeeper gateKeeper, boolean isDisplayLink){
		super(label, "", priority, gateKeeper,isDisplayLink);
		this.itemStyle=itemStyle;
		this.key=key;
	}
	
	public String getKey(){
		return key;
	}
	
}
