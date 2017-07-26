package com.company.aem.commons.core.services.impl;

import com.company.aem.commons.core.services.RunmodeService;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.runmode.RunMode;
import org.osgi.service.component.ComponentContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Component(immediate = true, metatype = false)
@Service
public class RunmodeServiceImpl implements RunmodeService {

	private static final Logger LOGGER = LoggerFactory.getLogger(RunmodeServiceImpl.class);

	@Reference
	RunMode runmode;
	
	String[] runmodes;
	boolean isAuthorMode;
	

	protected void activate(ComponentContext componentContext){
		runmodes = runmode.getCurrentRunModes();
		isAuthorMode = false;
		if(runmodes != null){
			for(String mode : runmodes){
				if(mode.equalsIgnoreCase("author")) {
					LOGGER.debug("RunModeServiceImpl : activate : runmode is author");
					isAuthorMode = true;
				}
			}
		}
	}
	
	@Override
	public boolean isAuthorMode() {
		return isAuthorMode;
	}

	@Override
	public String[] getRunmodes() {
		return runmodes;
	}

}
