package com.kylantis.eaa.core.cron;

import com.ce.ems.base.core.AppUtils;
import com.ce.ems.base.core.ClassIdentityType;
import com.ce.ems.base.core.ClasspathScanner;
import com.kylantis.eaa.core.keys.AppConfigKey;

public class CronUtility {

	public static void start () {
		String ext = AppUtils.getConfig(AppConfigKey.CLASSES_CRONJOBS_EXT);
		
		for (Class<? extends CronJob> cronJob : new ClasspathScanner<>(ext, CronJob.class,
				ClassIdentityType.SUPER_CLASS).scanClasses()) {
			
			
			
		}
	}
	
	
}
