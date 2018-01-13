package com.kylantis.eaa.core.fusion.services;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import com.ce.ems.base.classes.spec.BlobSpec;
import com.ce.ems.base.core.GsonFactory;
import com.ce.ems.models.BlobStoreModel;
import com.kylantis.eaa.core.fusion.BaseService;
import com.kylantis.eaa.core.fusion.EndpointClass;
import com.kylantis.eaa.core.fusion.EndpointMethod;
import com.kylantis.eaa.core.users.Functionality;

import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpMethod;
import io.vertx.ext.web.RoutingContext;

@EndpointClass(uri = "/blobstore")
public class BlobstoreService extends BaseService {

	@EndpointMethod(uri = "/save", method = HttpMethod.PUT, isBlocking = true, enableMultipart = true,
			functionality = Functionality.SAVE_BINARY_DATA)
	public void saveBlob(RoutingContext ctx) {
		List<String> blobIds = new ArrayList<>();
		ctx.fileUploads().forEach(f -> {
			try {
				String blobId = BlobStoreModel.save(null, Files.newInputStream(Paths.get(f.uploadedFileName())));
				blobIds.add(blobId);
			} catch (IOException e) {
				// Silently fail
			}
		});
		ctx.response()
		.write(GsonFactory.newInstance().toJson(blobIds)).setChunked(true)
		.end();
	}

	@EndpointMethod(uri = "/get", requestParams = { "blobId" }, createXhrClient = false,
			functionality = Functionality.GET_BINARY_DATA)
	public void getBlob(RoutingContext ctx) {
		String blobId = ctx.request().getParam("blobId");
		BlobSpec blob = BlobStoreModel.get(blobId);
		ctx.response().putHeader("Content-Type", blob.getMimeType()).bodyEndHandler(v -> {
			ctx.response().end();
		}).write(Buffer.buffer(blob.getData()));
	} 
 
	@EndpointMethod(uri = "/delete", requestParams = { "blobId" },
			functionality = Functionality.MANAGE_BINARY_DATA)
	public void deleteBlob(RoutingContext ctx) {
		String blobId = ctx.request().getParam("blobId");
		BlobStoreModel.delete(blobId);
	}
	
	@EndpointMethod(uri = "/list",
			functionality = Functionality.MANAGE_BINARY_DATA)
	public void list(RoutingContext ctx) {
		List<BlobSpec> entries = BlobStoreModel.list();
		ctx.response()
		.write(GsonFactory.newInstance().toJson(entries)).setChunked(true)
		.end();
	}
}
