package com.workpoint.icpak.client.ui.component;

import java.util.Date;
import static com.workpoint.icpak.client.ui.util.DateUtils.*;
import static com.workpoint.icpak.client.ui.util.StringUtils.*;

public class DateField extends TextField{

	public DateField() {
		setType("date");
	}
	
	public Date getValueDate(){
		String dateStr = getValue();
		//yyyy-MM-dd
		
		if(isNullOrEmpty(dateStr)){
			return null;
		}
		
		return DATEFORMAT_SYS.parse(dateStr);
	}
	
	public void setValue(Date date){
		if(date==null){
			setValue("");
		}else{
			setValue(DATEFORMAT_SYS.format(date));
		}
	}
}
