package com.kylantis.eaa.core.fusion.services;

import java.util.HashMap;
import java.util.Map;

import com.ce.ems.base.core.GsonFactory;
import com.ce.ems.models.LocationModel;
import com.ce.ems.models.RBModel;
import com.ce.ems.utils.LocaleUtils;
import com.google.gson.JsonObject;
import com.kylantis.eaa.core.fusion.BaseService;
import com.kylantis.eaa.core.fusion.EndpointClass;
import com.kylantis.eaa.core.fusion.EndpointMethod;
import com.kylantis.eaa.core.users.Functionality;
 
import io.vertx.core.http.HttpMethod;
import io.vertx.ext.web.RoutingContext;
    
@EndpointClass(uri = "/resource-bundle", externalModels = { LocationModel.class })
public class ResourceBundleService extends BaseService {
   
	public static final String DEFAULT_LOCALE_COOKIE = "DEFAULT_LOCALE";

	@EndpointMethod(uri = "/get-available-countries", functionality = Functionality.GET_AVAILABLE_COUNTRIES)
	public void getAvailableCountries(RoutingContext ctx) {

		// K: locale, V: name, code 
		Map<String, JsonObject> result = new HashMap<>();

		RBModel.getLocaleCountries().forEach((language, countries) -> {

			LocationModel.getCountryNames(countries).forEach((code, name) -> {
 
				String k = language + LocaleUtils.LANGUAGE_COUNTRY_DELIMETER + code;

				JsonObject v = new JsonObject();
				v.addProperty("code", code);
				v.addProperty("name", name);
  
				result.put(k, v);
			}); 

		});

		ctx.response().write(GsonFactory.newInstance().toJson(result)).setChunked(true);
	}

	@EndpointMethod(uri = "/get-rb-entry", bodyParams = {"keys"}, method = HttpMethod.POST,
			functionality = Functionality.GET_RESOURCE_BUNDLE_ENTRIES)
	public void getRbEntry(RoutingContext ctx) {

		Map<String, Object> keys = ctx.getBodyAsJson().getJsonObject("keys").getMap();
		Map<String, String> result = new HashMap<>();

		keys.forEach((k, v) -> {
			result.put(k, RBModel.get(v.toString()));
		});

		ctx.response().write(GsonFactory.newInstance().toJson(result)).setChunked(true);
	}

}
