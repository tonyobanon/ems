package com.ce.ems.models;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.annotation.Nullable;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.SerializationUtils;

import com.ce.ems.base.classes.EntityHelper;
import com.ce.ems.base.classes.FluentArrayList;
import com.ce.ems.base.classes.InstallOptions;
import com.ce.ems.base.classes.spec.BlobSpec;
import com.ce.ems.base.core.BlockerTodo;
import com.ce.ems.base.core.ModelMethod;
import com.ce.ems.base.core.Todo;
import com.ce.ems.entites.BlobEntity;
import com.ce.ems.utils.Utils;
import com.kylantis.eaa.core.fusion.WebRoutes;
import com.kylantis.eaa.core.users.Functionality;

@BlockerTodo("Deprecate this functionality in favour of the GAE Blob Service")
@Todo("Add functionality that enables the administrator to create arbitary rules for blob storage, use: MANAGE_BINARY_DATA")
public class BlobStoreModel extends BaseModel {

	@Override
	public String path() {
		return "core/blobstore";
	}
	
	@Override
	public void install(InstallOptions options) {
	}

	@Override
	public void start() {

	}

	protected static String save(String id, Serializable obj) throws IOException {
		return save(id, SerializationUtils.serialize(obj));
	}

	public static String save(InputStream in) throws IOException {
		return save(null, in);
	}

	@ModelMethod(functionality = Functionality.SAVE_BINARY_DATA)
	public static String save(@Nullable String id, InputStream in) throws IOException {
		return save(id, IOUtils.toByteArray(in));
	}

	@ModelMethod(functionality = Functionality.SAVE_BINARY_DATA)
	public static String save(String id, byte[] data) throws IOException {

		if (id == null) {
			id = Utils.newRandom();
		}

		String mimeType = WebRoutes.TIKA_INSTANCE.detect(data);

		int size = data.length;

		BlobEntity entity = new BlobEntity().setId(id).setData(data).setSize(size).setMimeType(mimeType)
				.setDateCreated(new Date());

		ofy().save().entity(entity).now();

		return entity.getId();
	}

	@ModelMethod(functionality = Functionality.MANAGE_BINARY_DATA)
	public static List<BlobSpec> list() {
		List<BlobSpec> result = new FluentArrayList<>();
		ofy().load().type(BlobEntity.class).forEach(e -> {
			result.add(EntityHelper.toObjectModel(e));
		});
		return result;
	}

	@ModelMethod(functionality = Functionality.MANAGE_BINARY_DATA)
	public static void delete(String id) {
		ofy().delete().type(BlobEntity.class).id(id).now();
	}

	@ModelMethod(functionality = Functionality.GET_BINARY_DATA)
	public static BlobSpec get(String id) {
		BlobEntity entity = ofy().load().type(BlobEntity.class).id(id).safe();
		return EntityHelper.toObjectModel(entity);
	}
}
