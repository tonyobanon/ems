package com.ce.ems.base.core;

import java.io.IOException;

import com.ce.ems.base.classes.ObjectWrapper;
import com.ce.ems.base.core.Logger.verboseLevel;
import com.ce.ems.models.BaseModel;
import com.ce.ems.models.ConfigModel;
import com.ce.ems.models.PlatformModel;
import com.github.zafarkhaja.semver.Version;
import com.google.appengine.api.utils.SystemProperty;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.VoidWork;
import com.kylantis.eaa.core.fusion.APIRoutes;
import com.kylantis.eaa.core.fusion.CacheAdapter;
import com.kylantis.eaa.core.fusion.WebRoutes;
import com.kylantis.eaa.core.keys.ConfigKeys;

public class Application {

	public static final String SOFTRWARE_VENDER_NAME = "Compute Essentials, Inc";
	public static final String SOFTRWARE_VENDER_EMAIL = "corporate@compute-essentials.com";
	
	private static boolean isStarted = false;

	public static void start() {

		if (isStarted) {
			return;
		}

		Logger.debug("Starting Engine ..");

		// @DEV
		Logger.enableSystemOutput();

		// Detect runtime environment		
		//boolean isProduction = SystemProperty.environment.value() == SystemProperty.Environment.Value.Production;		
		//Logger.verboseMode(isProduction ? verboseLevel.TRACE : verboseLevel.DEBUG);
		
		Logger.verboseMode(verboseLevel.DEBUG);
		
		CacheAdapter.start();

		APIRoutes.scanRoutes();
		
		WebRoutes.scanRoutes();
		
		BaseModel.scanModels();

		ObjectifyService.run(new VoidWork() {
			public void vrun() {

				if (!PlatformModel.isInstalled()) {

					Logger.debug("Preinstalling Models");

					// Pre install models

					BaseModel.getModels().forEach(e -> {
						Logger.debug("Pre-installing " + e.getClass().getSimpleName());
						e.preInstall();
					});

					ConfigModel.put(ConfigKeys.CURRENT_PLATFORM_VERSION, "0.0.1-rc.1+build.1");					

				} else {

					Version platformVersion = Version.valueOf(ConfigModel.get(ConfigKeys.CURRENT_PLATFORM_VERSION));
					ObjectWrapper<Version> newPlatformVersion = new ObjectWrapper<Version>().set(platformVersion);
					
					// Check for model updates
					BaseModel.getModels().forEach(e -> {
						
						Class<?> c = e.getClass();
						
						// Silently ignore
						if (!c.isAnnotationPresent(Model.class)) {
							return;
						}

						Model classAnnotation = c.getAnnotation(Model.class);
						if(classAnnotation.version().equals(BaseModel.DEFAULT_MODEL_VERSION)) {
							return;
						}
						
						Version modelVersion = Version.valueOf(classAnnotation.version());
						
						if(modelVersion.greaterThan(platformVersion)) {
							
							Logger.debug("Updating " + c.getSimpleName());
							
							e.update();
							
							if(modelVersion.greaterThan(newPlatformVersion.get())){
								newPlatformVersion.set(modelVersion);
							}
							
						}
						
					});
					
					if(newPlatformVersion.get().greaterThan(platformVersion)) {

						Logger.debug("Updating platform version to: " + newPlatformVersion.get().toString());
						ConfigModel.put(ConfigKeys.CURRENT_PLATFORM_VERSION, newPlatformVersion.get().toString());	
					}

					// Start models

					BaseModel.getModels().forEach(e -> {
						Logger.debug("Starting " + e.getClass().getSimpleName());
						e.start();
					});
				}
			}
		});
		

		// Logger.info("Launching API web server ..");
		//
		// try {
		//
		// WebServer.start(new MicroServiceOptions().withPort(8181)
		// .withRouteSet(isInstalled ? RouteSet.ALL : RouteSet.SETUP_ONLY));
		//
		// } catch (IOException e) {
		// Exceptions.throwRuntime(e);
		// }

		isStarted = true;
	}

	public static boolean isStarted() {
		return isStarted;
	}

	public static void stop() {
		
		//Stop Vertx
		
		if(!Application.isProduction()) {

			WebRoutes.fileWatcherPool.shutdownNow();
			try {
				WebRoutes.watchService.close();
			} catch (IOException e) {
				Exceptions.throwRuntime(e);
			}
		}
		
	}
	
	public static final boolean isProduction() {
		return SystemProperty.environment.value() == SystemProperty.Environment.Value.Production;
	}
	
}
