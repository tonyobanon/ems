package com.ce.ems.models;

import com.ce.ems.base.core.BlockerTodo;
import com.ce.ems.base.core.ModelMethod;
import com.kylantis.eaa.core.users.Functionality;

@BlockerTodo("Implement ASAP")
public class ConfigurationModel extends BaseModel {

	@Override
	public String path() {
		return "core/configuration";
	}
	
	@ModelMethod(functionality = Functionality.VIEW_SYSTEM_CONFIGURATION)
	public static final void getAll() {
		
		//get all config entries that is marked as front-end
		
	}

}
