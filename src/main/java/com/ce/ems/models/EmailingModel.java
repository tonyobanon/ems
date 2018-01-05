package com.ce.ems.models;

import com.ce.ems.base.classes.InstallOptions;
import com.ce.ems.base.classes.MailCredentialSpec;
import com.ce.ems.utils.JMSUtil;
import com.kylantis.eaa.core.keys.ConfigKeys;

public class EmailingModel extends BaseModel {

	@Override
	public String path() {
		return "core/emailing";
	}
	
	@Override
	public void install(InstallOptions options) {
		
		MailCredentialSpec spec = options.getMailCredentials();
		
		ConfigModel.put(ConfigKeys.MAIL_PROVIDER_URL, spec.getProviderUrl());
		ConfigModel.put(ConfigKeys.MAIL_PROVIDER_USERNAME, spec.getUsername());
		ConfigModel.put(ConfigKeys.MAIL_PROVIDER_PASSWORD, spec.getPassword());
		
		start();
	}
	
	@Override
	public void start() {
		
		MailCredentialSpec spec = new MailCredentialSpec()
				.setProviderUrl(ConfigModel.get(ConfigKeys.MAIL_PROVIDER_URL))
				.setUsername(ConfigModel.get(ConfigKeys.MAIL_PROVIDER_USERNAME))
				.setPassword(ConfigModel.get(ConfigKeys.MAIL_PROVIDER_PASSWORD));
		
		JMSUtil.setCredentials(spec);
		
	}

}
