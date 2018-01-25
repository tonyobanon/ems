package com.kylantis.eaa.core.fusion.services;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.ce.ems.base.classes.CursorMoveType;
import com.ce.ems.base.classes.IndexedNameType;
import com.ce.ems.base.classes.ListingFilter;
import com.ce.ems.base.classes.ListingType;
import com.ce.ems.base.classes.SearchableUISpec;
import com.ce.ems.base.core.GsonFactory;
import com.ce.ems.models.SearchModel;
import com.ce.ems.utils.BackendObjectMarshaller;
import com.google.gson.reflect.TypeToken;
import com.kylantis.eaa.core.fusion.BaseService;
import com.kylantis.eaa.core.fusion.EndpointClass;
import com.kylantis.eaa.core.fusion.EndpointMethod;
import com.kylantis.eaa.core.fusion.FusionHelper;
import com.kylantis.eaa.core.users.Functionality;

import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

@EndpointClass(uri = "/search/service")
public class SearchService extends BaseService {

	@EndpointMethod(uri = "/new-list-context", bodyParams = { "type", "pageSize", "listingFilters",
			"order" }, method = HttpMethod.PUT, functionality = Functionality.PERFORM_LIST_OPERATION)
	public void newListContext(RoutingContext ctx) {

		JsonObject body = ctx.getBodyAsJson();

		Long userId = FusionHelper.getUserId(ctx.request());

		IndexedNameType type = IndexedNameType.from(body.getInteger("type"));
		Integer pageSize = body.getInteger("pageSize");
		String order = body.getString("order");

		List<ListingFilter> listingFilters = GsonFactory.newInstance()
				.fromJson(body.getJsonArray("listingFilters").encode(), new TypeToken<List<ListingFilter>>() {
				}.getType());

		for (ListingFilter listingFilter : listingFilters) {

			for (Entry<String, Object> filter : listingFilter.getFilters().entrySet()) {
				// If there are any dates in <filters>, attempt parsing it correctly
				try {
					Date date = BackendObjectMarshaller.unmarshalDate(filter.getValue().toString());
					filter.setValue(date);
				} catch (ParseException | NumberFormatException e) {
				}
			}
		} 

		String contextKey = SearchModel.newListContext(userId, type, pageSize,
				(order != null && !order.equals("undefined")) ? order : null, listingFilters);

		ctx.response().write(new JsonObject().put("contextKey", contextKey).encode());
	}

	@EndpointMethod(uri = "/new-search-context", bodyParams = { "type", "phrase",
			"pageSize" }, method = HttpMethod.PUT, functionality = Functionality.PERFORM_LIST_OPERATION)
	public void newSearchContext(RoutingContext ctx) {

		JsonObject body = ctx.getBodyAsJson();

		Long userId = FusionHelper.getUserId(ctx.request());

		IndexedNameType type = IndexedNameType.from(body.getInteger("type"));
		String phrase = body.getString("phrase");
		Integer pageSize = body.getInteger("pageSize");

		String contextKey = SearchModel.newSearchContext(userId, type, phrase, pageSize);

		ctx.response().write(new JsonObject().put("contextKey", contextKey).encode());
	}

	@EndpointMethod(uri = "/clear-cache", requestParams = {
			"type" }, method = HttpMethod.DELETE, functionality = Functionality.MANAGE_SYSTEM_CACHES)
	public void clearSearchCache(RoutingContext ctx) {
		ListingType type = ListingType.from(Integer.parseInt(ctx.request().getParam("type")));
		SearchModel.clearCache(type);
	}

	@EndpointMethod(uri = "/has-cursor", requestParams = { "moveType",
			"contextKey" }, method = HttpMethod.GET, functionality = Functionality.PERFORM_LIST_OPERATION)
	public void hasListingCursor(RoutingContext ctx) {

		Long userId = FusionHelper.getUserId(ctx.request());

		CursorMoveType moveType = CursorMoveType.from(Integer.parseInt(ctx.request().getParam("moveType")));
		String contextKey = ctx.request().getParam("contextKey");

		Boolean b = SearchModel.has(userId, moveType, contextKey);
		ctx.response().write(b.toString());
	}

	@EndpointMethod(uri = "/is-context-available", requestParams = {
			"contextKey" }, method = HttpMethod.GET, functionality = Functionality.PERFORM_LIST_OPERATION)
	public void isListingContextAvailable(RoutingContext ctx) {

		String contextKey = ctx.request().getParam("contextKey");

		Boolean b = SearchModel.isContextAvailable(contextKey);
		ctx.response().write(b.toString());
	}

	@EndpointMethod(uri = "/next-results", requestParams = { "moveType",
			"contextKey" }, method = HttpMethod.GET, functionality = Functionality.PERFORM_LIST_OPERATION)
	public void nextListingResults(RoutingContext ctx) {

		Long userId = FusionHelper.getUserId(ctx.request());

		CursorMoveType moveType = CursorMoveType.from(Integer.parseInt(ctx.request().getParam("moveType")));
		String contextKey = ctx.request().getParam("contextKey");

		Map<?, ?> result = SearchModel.next(userId, moveType, contextKey);

		ctx.response().write(GsonFactory.newInstance().toJson(result));
	}

	@EndpointMethod(uri = "/get-searchable-lists", method = HttpMethod.GET, functionality = Functionality.GET_SEARCHABLE_LISTS)
	public void getSearchableList(RoutingContext ctx) {

		Long userId = FusionHelper.getUserId(ctx.request());

		Map<Integer, SearchableUISpec> result = SearchModel.getSearchableLists(userId);
		ctx.response().write(GsonFactory.newInstance().toJson(result));
	}

}
