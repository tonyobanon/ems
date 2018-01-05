package com.kylantis.eaa.core.fusion.services;

import com.ce.ems.base.core.GsonFactory;
import com.ce.ems.models.CacheModel;
import com.ce.ems.models.LocationModel;
import com.kylantis.eaa.core.fusion.BaseService;
import com.kylantis.eaa.core.fusion.EndpointClass;
import com.kylantis.eaa.core.fusion.EndpointMethod;
import com.kylantis.eaa.core.keys.CacheKeys;
import com.kylantis.eaa.core.users.Functionality;

import io.vertx.ext.web.RoutingContext;

@EndpointClass(uri = "/locations/service")
public class LocationService extends BaseService {

	@EndpointMethod(uri = "/countryList",
			functionality = Functionality.GET_COUNTRY_NAMES)
	public void getCountries(RoutingContext ctx) {
		String json = (String) CacheModel.get(CacheKeys.COUNTRY_NAMES);
		if (json != null) {
			ctx.response().setChunked(true).write(json);
		} else {
			json = GsonFactory.newInstance().toJson(LocationModel.getCountryNames());
			CacheModel.put(CacheKeys.COUNTRY_NAMES, json);
			ctx.response().setChunked(true).write(json);
		}
		ctx.response().end();
	}

	@EndpointMethod(uri = "/currencyList",
			functionality = Functionality.GET_LOCATION_DATA)
	public void getCurrencies(RoutingContext ctx) {
		String json = (String) CacheModel.get(CacheKeys.CURRENCY_NAMES);
		if (json != null) {
			ctx.response().setChunked(true).write(json);
		} else {
			json = GsonFactory.newInstance().toJson(LocationModel.getCurrencyNames());
			CacheModel.put(CacheKeys.CURRENCY_NAMES, json);
			ctx.response().setChunked(true).write(json);
		}
		ctx.response().end();
	}

	@EndpointMethod(uri = "/localeList", requestParams = "countryCode",
			functionality = Functionality.GET_LOCATION_DATA)
	public void getLocales(RoutingContext ctx) {

		String countryCode = ctx.request().getParam("countryCode");

		if (countryCode != null && !countryCode.equals("null")) {
			String json = (String) CacheModel.get(CacheKeys.LOCALES_$COUNTRY.replace("$COUNTRY", countryCode));
			if (json != null) {
				ctx.response().setChunked(true).write(json);
			} else {
				json = GsonFactory.newInstance().toJson(LocationModel.getAvailableLocales(countryCode));
				CacheModel.put(CacheKeys.LOCALES_$COUNTRY.replace("$COUNTRY", countryCode), json);
				ctx.response().setChunked(true).write(json);
			}
		} else {
			String json = (String) CacheModel.get(CacheKeys.LOCALES);
			if (json != null) {
				ctx.response().setChunked(true).write(json);
			} else {
				json = GsonFactory.newInstance().toJson(LocationModel.getAllLocales());
				CacheModel.put(CacheKeys.LOCALES, json);
				ctx.response().setChunked(true).write(json);
			}
		}
		ctx.response().end();
	}

	@EndpointMethod(uri = "/timezoneList",
			functionality = Functionality.GET_LOCATION_DATA)
	public void getTimezones(RoutingContext ctx) {
		String json = (String) CacheModel.get(CacheKeys.TIMEZONES);
		if (json != null) {
			ctx.response().setChunked(true).write(json);
		} else {
			json = GsonFactory.newInstance().toJson(LocationModel.getAllTimezones());
			CacheModel.put(CacheKeys.TIMEZONES, json);
			ctx.response().setChunked(true).write(json);
		}
		ctx.response().end();
	}

	@EndpointMethod(uri = "/territoryList", requestParams = "ctx",
			functionality = Functionality.GET_TERRITORY_NAMES)
	public void getTerritories(RoutingContext ctx) {
		String countryCode = ctx.request().getParam("ctx");
		String json = (String) CacheModel.get(CacheKeys.TERRITORIES_$COUNTRY.replace("$COUNTRY", countryCode));
		if (json != null) {
			ctx.response().setChunked(true).write(json);
		} else {
			json = GsonFactory.newInstance().toJson(LocationModel.getTerritoryNames(countryCode));
			CacheModel.put(CacheKeys.TERRITORIES_$COUNTRY.replace("$COUNTRY", countryCode), json);
			ctx.response().setChunked(true).write(json);
		}
		ctx.response().end();
	}

	@EndpointMethod(uri = "/cityList", requestParams = "ctx",
			functionality = Functionality.GET_CITY_NAMES)
	public void getCities(RoutingContext ctx) {
		String territoryCode = ctx.request().getParam("ctx");
		String json = (String) CacheModel.get(CacheKeys.CITIES_$TERRITORY.replace("$TERRITORY", territoryCode));
		if (json != null) {
			ctx.response().setChunked(true).write(json);
		} else {
			json = GsonFactory.newInstance().toJson(LocationModel.getCityNames(territoryCode));
			CacheModel.put(CacheKeys.CITIES_$TERRITORY.replace("$TERRITORY", territoryCode), json);
			ctx.response().setChunked(true).write(json);
		}
		ctx.response().end();
	}

	@EndpointMethod(uri = "/cityCoordinates", requestParams = "cityCode",
			functionality = Functionality.GET_LOCATION_DATA)
	public void getCityCoordinates(RoutingContext ctx) {
		String cityCode = ctx.request().getParam("cityCode");
		String json = (String) CacheModel.get(CacheKeys.COORDINATES_$CITY.replace("$CITY", cityCode));
		if (json != null) {
			ctx.response().setChunked(true).write(json);
		} else {
			json = GsonFactory.newInstance().toJson(LocationModel.getCoordinates(Integer.parseInt(cityCode)));
			CacheModel.put(CacheKeys.COORDINATES_$CITY.replace("$CITY", cityCode), json);
			ctx.response().setChunked(true).write(json);
		}
		ctx.response().end();
	}

	@EndpointMethod(uri = "/cityTimezone", requestParams = "cityCode",
			functionality = Functionality.GET_LOCATION_DATA)
	public void getCityTimezone(RoutingContext ctx) {
		String cityCode = ctx.request().getParam("cityCode");
		String json = (String) CacheModel.get(CacheKeys.TIMEZONE_$CITY.replace("$CITY", cityCode));
		if (json != null) {
			ctx.response().setChunked(true).write(json);
		} else {
			json = GsonFactory.newInstance().toJson(LocationModel.getTimezone(Integer.parseInt(cityCode)));
			CacheModel.put(CacheKeys.TIMEZONE_$CITY.replace("$CITY", cityCode), json);
			ctx.response().setChunked(true).write(json);
		}
		ctx.response().end();
	}

	@EndpointMethod(uri = "/countryLanguages", requestParams = "countryCode",
			functionality = Functionality.GET_LOCATION_DATA)
	public void getSpokenLanguages(RoutingContext ctx) {
		String countryCode = ctx.request().getParam("countryCode");
		String json = (String) CacheModel.get(CacheKeys.SPOKEN_LANGUAGES_$COUNTRY.replace("$COUNTRY", countryCode));
		if (json != null) {
			ctx.response().setChunked(true).write(json);
		} else {
			json = GsonFactory.newInstance().toJson(LocationModel.getSpokenLanguages(countryCode));
			CacheModel.put(CacheKeys.SPOKEN_LANGUAGES_$COUNTRY.replace("$COUNTRY", countryCode), json);
			ctx.response().setChunked(true).write(json);
		}
		ctx.response().end();
	}

	@EndpointMethod(uri = "/currencyCode", requestParams = "countryCode",
			functionality = Functionality.GET_LOCATION_DATA)
	public void getCurrencyCode(RoutingContext ctx) {
		String countryCode = ctx.request().getParam("countryCode");
		String json = (String) CacheModel.get(CacheKeys.CURRENCY_CODE_$COUNTRY.replace("$COUNTRY", countryCode));
		if (json != null) {
			ctx.response().setChunked(true).write(json);
		} else {
			json = "[\"" + LocationModel.getCurrencyCode(countryCode) + "\"]";
			
			CacheModel.put(CacheKeys.CURRENCY_CODE_$COUNTRY.replace("$COUNTRY", countryCode), json);
			ctx.response().setChunked(true).write(json);
		} 
		ctx.response().end(); 
	}    
 
	@EndpointMethod(uri = "/currencyName", requestParams = "countryCode",
			functionality = Functionality.GET_LOCATION_DATA)
	public void getCurrencyName(RoutingContext ctx) {
		String countryCode = ctx.request().getParam("countryCode");
		String json = (String) CacheModel.get(CacheKeys.CURRENCY_NAME_$COUNTRY.replace("$COUNTRY", countryCode));
		if (json != null) {
			ctx.response().setChunked(true).write(json);
		} else {
			json = "[\"" + LocationModel.getCurrencyName(countryCode) + "\"]";
			CacheModel.put(CacheKeys.CURRENCY_NAME_$COUNTRY.replace("$COUNTRY", countryCode), json);
			ctx.response().setChunked(true).write(json);
		}
		ctx.response().end();
	}

}
