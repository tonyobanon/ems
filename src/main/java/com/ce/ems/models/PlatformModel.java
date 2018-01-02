package com.ce.ems.models;

import com.ce.ems.base.classes.InstallOptions;
import com.ce.ems.base.core.Application;
import com.ce.ems.utils.BackendObjectMarshaller;
import com.kylantis.eaa.core.keys.ConfigKeys;

public class PlatformModel extends BaseModel{

	@Override
	public String path() {
		return "core/platform";
	}
	

	public static void doInstall(InstallOptions spec) {
	
		// consolidate service, models
		
		//Install all models
		
		BaseModel.getModels().forEach(model -> {
			model.install(spec);
		});
		
		
		Application.isInstalled = true;
	}
	
	public static boolean isInstalled() {
		return BackendObjectMarshaller.unmarshalBool(ConfigModel.get(ConfigKeys.IS_INSTALLED));
	}
}
