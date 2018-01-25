package com.kylantis.eaa.core.fusion.services;

import java.util.List;
import java.util.Map;

import com.ce.ems.base.classes.FormSectionType;
import com.ce.ems.base.core.GsonFactory;
import com.ce.ems.models.FormModel;
import com.ce.ems.models.helpers.FormFieldRepository;
import com.ce.ems.models.helpers.FormFieldRepository.FieldType;
import com.kylantis.eaa.core.forms.CompositeEntry;
import com.kylantis.eaa.core.forms.Question;
import com.kylantis.eaa.core.forms.SimpleEntry;
import com.kylantis.eaa.core.fusion.BaseService;
import com.kylantis.eaa.core.fusion.EndpointClass;
import com.kylantis.eaa.core.fusion.EndpointMethod;
import com.kylantis.eaa.core.pdf.Section;
import com.kylantis.eaa.core.users.Functionality;
import com.kylantis.eaa.core.users.RoleRealm;

import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

@EndpointClass(uri = "/forms")
public class FormService extends BaseService {

	@EndpointMethod(uri = "/new-application-form-section", bodyParams = { "name",
			"realm" }, method = HttpMethod.PUT, functionality = Functionality.MANAGE_APPLICATION_FORMS)
	public void newApplicationFormSection(RoutingContext ctx) {

		JsonObject body = ctx.getBodyAsJson();

		String name = body.getString("name");
		RoleRealm roleRealm = RoleRealm.from(body.getInteger("realm"));

		FormModel.newSection(name, FormSectionType.APPLICATION_FORM, roleRealm);
	}

	@EndpointMethod(uri = "/list-application-form-sections", requestParams = {
			"realm" }, functionality = Functionality.VIEW_APPLICATION_FORM)
	public void listApplicationFormSections(RoutingContext ctx) {

		RoleRealm roleRealm = RoleRealm.from(Integer.parseInt(ctx.request().getParam("realm")));
		List<Section> sections = FormModel.listSections(FormSectionType.APPLICATION_FORM, roleRealm);
		ctx.response().setChunked(true).write(GsonFactory.newInstance().toJson(sections)).end();
	}

	@EndpointMethod(uri = "/list-system-configuration-sections", functionality = Functionality.VIEW_SYSTEM_CONFIGURATION)
	public void listSystemConfigurationSections(RoutingContext ctx) {

		List<Section> sections = FormModel.listSections(FormSectionType.SYSTEM_CONFIGURATION, null);
		ctx.response().setChunked(true).write(GsonFactory.newInstance().toJson(sections)).end();
	}

	@EndpointMethod(uri = "/list-application-form-fields", requestParams = {
			"sectionId" }, functionality = Functionality.VIEW_APPLICATION_FORM)
	public void listApplicationFormFields(RoutingContext ctx) {

		String sectionId = ctx.request().getParam("sectionId");

		List<Question> fields = FormModel.getFields(FormSectionType.APPLICATION_FORM, sectionId);
		ctx.response().setChunked(true).write(GsonFactory.newInstance().toJson(fields)).end();
	}

	@EndpointMethod(uri = "/list-application-form-field-names", requestParams = {
			"sectionId" }, functionality = Functionality.VIEW_APPLICATION_FORM)
	public void listApplicationFormFieldNames(RoutingContext ctx) {

		String sectionId = ctx.request().getParam("sectionId");

		Map<String, String> fields = FormModel.getFieldNames(FormSectionType.APPLICATION_FORM, sectionId);
		ctx.response().setChunked(true).write(GsonFactory.newInstance().toJson(fields)).end();
	}

	@EndpointMethod(uri = "/list-all-application-form-fields", bodyParams = {
			"sectionIds" }, method = HttpMethod.POST, functionality = Functionality.VIEW_APPLICATION_FORM)
	public void listAllApplicationFormFields(RoutingContext ctx) {

		JsonObject body = ctx.getBodyAsJson();

		List<String> sectionIds = body.getJsonArray("sectionIds").getList();

		Map<String, List<Question>> fields = FormModel.getAllFields(FormSectionType.APPLICATION_FORM, sectionIds);
		ctx.response().setChunked(true).write(GsonFactory.newInstance().toJson(fields)).end();
	}

	@EndpointMethod(uri = "/list-system-configuration-fields", requestParams = {
			"sectionId" }, functionality = Functionality.VIEW_SYSTEM_CONFIGURATION)
	public void listSystemConfigurationFields(RoutingContext ctx) {

		String sectionId = ctx.request().getParam("sectionId");

		List<Question> fields = FormModel.getFields(FormSectionType.SYSTEM_CONFIGURATION, sectionId);
		ctx.response().setChunked(true).write(GsonFactory.newInstance().toJson(fields)).end();
	}

	@EndpointMethod(uri = "/list-system-configuration-field-names", requestParams = {
			"sectionId" }, functionality = Functionality.VIEW_SYSTEM_CONFIGURATION)
	public void listSystemConfigurationFieldNames(RoutingContext ctx) {

		String sectionId = ctx.request().getParam("sectionId");

		Map<String, String> fields = FormModel.getFieldNames(FormSectionType.SYSTEM_CONFIGURATION, sectionId);
		ctx.response().setChunked(true).write(GsonFactory.newInstance().toJson(fields)).end();
	}

	@EndpointMethod(uri = "/list-all-system-configuration-fields", bodyParams = {
			"sectionIds" }, method = HttpMethod.POST, functionality = Functionality.VIEW_SYSTEM_CONFIGURATION)
	public void listAllSystemConfigurationFields(RoutingContext ctx) {

		JsonObject body = ctx.getBodyAsJson();

		List<String> sectionIds = body.getJsonArray("sectionIds").getList();

		Map<String, List<Question>> fields = FormModel.getAllFields(FormSectionType.SYSTEM_CONFIGURATION, sectionIds);
		ctx.response().setChunked(true).write(GsonFactory.newInstance().toJson(fields)).end();
	}

	@EndpointMethod(uri = "/delete-application-form-section", requestParams = {
			"sectionId" }, method = HttpMethod.DELETE, functionality = Functionality.MANAGE_APPLICATION_FORMS)
	public void deleteApplicationFormSection(RoutingContext ctx) {

		String sectionId = ctx.request().getParam("sectionId");
		FormModel.deleteSection(sectionId, FormSectionType.APPLICATION_FORM);
	}

	@EndpointMethod(uri = "/delete-system-configuration-section", requestParams = {
			"sectionId" }, method = HttpMethod.DELETE, functionality = Functionality.MANAGE_SYSTEM_CONFIGURATION_FORM)
	public void deleteSystemConfigurationSection(RoutingContext ctx) {

		String sectionId = ctx.request().getParam("sectionId");
		FormModel.deleteSection(sectionId, FormSectionType.SYSTEM_CONFIGURATION);
	}

	@EndpointMethod(uri = "/delete-application-form-field", requestParams = {
			"fieldId" }, method = HttpMethod.DELETE, functionality = Functionality.MANAGE_APPLICATION_FORMS)
	public void deleteApplicationFormField(RoutingContext ctx) {

		String fieldId = ctx.request().getParam("fieldId");
		FormModel.deleteField(FormSectionType.APPLICATION_FORM, fieldId);
	}

	@EndpointMethod(uri = "/delete-system-configuration-field", requestParams = {
			"fieldId" }, method = HttpMethod.DELETE, functionality = Functionality.MANAGE_SYSTEM_CONFIGURATION_FORM)
	public void deleteSystemConfigurationField(RoutingContext ctx) {

		String fieldId = ctx.request().getParam("fieldId");
		FormModel.deleteField(FormSectionType.SYSTEM_CONFIGURATION, fieldId);
	}

	@EndpointMethod(uri = "/create-application-form-simple-field", bodyParams = { "sectionId",
			"spec" }, method = HttpMethod.PUT, functionality = Functionality.MANAGE_APPLICATION_FORMS)
	public void newApplicationFormSimpleField(RoutingContext ctx) {

		JsonObject body = ctx.getBodyAsJson();

		String sectionId = body.getString("sectionId");
		SimpleEntry spec = GsonFactory.newInstance().fromJson(body.getJsonObject("spec").encode(), SimpleEntry.class);

		String id = FormModel.newSimpleField(FormSectionType.APPLICATION_FORM, sectionId, spec);

		ctx.response().setChunked(true).write(id).end();
	}

	@EndpointMethod(uri = "/create-application-form-composite-field", bodyParams = { "sectionId",
			"spec" }, method = HttpMethod.PUT, functionality = Functionality.MANAGE_APPLICATION_FORMS)
	public void newApplicationFormCompositeField(RoutingContext ctx) {

		JsonObject body = ctx.getBodyAsJson();

		String sectionId = body.getString("sectionId");
		CompositeEntry spec = GsonFactory.newInstance().fromJson(body.getJsonObject("spec").encode(),
				CompositeEntry.class);

		String id = FormModel.newCompositeField(FormSectionType.APPLICATION_FORM, sectionId, spec);

		ctx.response().setChunked(true).write(id).end();
	}

	@EndpointMethod(uri = "/create-system-configuration-simple-field", bodyParams = { "sectionId",
			"spec" }, method = HttpMethod.PUT, functionality = Functionality.MANAGE_SYSTEM_CONFIGURATION_FORM)
	public void newSystemConfigurationSimpleField(RoutingContext ctx) {

		JsonObject body = ctx.getBodyAsJson();

		String sectionId = body.getString("sectionId");
		SimpleEntry spec = GsonFactory.newInstance().fromJson(body.getJsonObject("spec").encode(), SimpleEntry.class);

		String id = FormModel.newSimpleField(FormSectionType.SYSTEM_CONFIGURATION, sectionId, spec);

		ctx.response().setChunked(true).write(id).end();
	}

	@EndpointMethod(uri = "/create-system-configuration-composite-field", bodyParams = { "sectionId",
			"spec" }, method = HttpMethod.PUT, functionality = Functionality.MANAGE_SYSTEM_CONFIGURATION_FORM)
	public void newSystemConfigurationCompositeField(RoutingContext ctx) {

		JsonObject body = ctx.getBodyAsJson();

		String sectionId = body.getString("sectionId");
		CompositeEntry spec = GsonFactory.newInstance().fromJson(body.getJsonObject("spec").encode(),
				CompositeEntry.class);

		String id = FormModel.newCompositeField(FormSectionType.SYSTEM_CONFIGURATION, sectionId, spec);

		ctx.response().setChunked(true).write(id).end();
	}

	@EndpointMethod(uri = "/get-application-form-field-ids", requestParams = {
			"realm" }, functionality = Functionality.GET_FORM_FIELD_IDS)
	public void getApplicationFormFieldIds(RoutingContext ctx) {

		RoleRealm roleRealm = RoleRealm.from(Integer.parseInt(ctx.request().getParam("realm")));

		Map<FieldType, String> result = FormFieldRepository.getFieldIds(roleRealm);

		ctx.response().setChunked(true).write(GsonFactory.newInstance().toJson(result)).end();
	}

}
