package com.ce.ems.models;

import com.ce.ems.base.classes.InstallOptions;
import com.ce.ems.base.core.Logger;
import com.ce.ems.utils.BackendObjectMarshaller;
import com.kylantis.eaa.core.keys.ConfigKeys;

public class PlatformModel extends BaseModel {
	
	@Override
	public String path() {
		return "core/platform";
	}

	public static void doInstall(InstallOptions spec) {
	
		//Install all models
		Logger.debug("Installing Models");
		
		BaseModel.getModels().forEach(e -> {
			Logger.debug("Installing " + e.getClass().getSimpleName());
			e.install(spec);
		});
		
		ConfigModel.put(ConfigKeys.IS_INSTALLED, BackendObjectMarshaller.marshal(true));
	}
	 
	public static boolean isInstalled() {
		return BackendObjectMarshaller.unmarshalBool(ConfigModel.get(ConfigKeys.IS_INSTALLED));
	}
}
