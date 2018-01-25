package com.kylantis.eaa.core.fusion.services;

import com.ce.ems.models.PrototypingModel;
import com.kylantis.eaa.core.fusion.BaseService;
import com.kylantis.eaa.core.fusion.EndpointClass;
import com.kylantis.eaa.core.fusion.EndpointMethod;
import com.kylantis.eaa.core.users.Functionality;

import io.vertx.ext.web.RoutingContext;

@EndpointClass(uri = "/prototyping")
public class PrototypingService extends BaseService {

	@EndpointMethod(uri = "/create-mocks", functionality = Functionality.ADD_SYSTEM_MOCK_DATA)
	public void createMocks(RoutingContext ctx) {
		PrototypingModel.addMocks();
	}
	
}
