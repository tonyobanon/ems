package com.ce.ems.base.core;

import com.ce.ems.base.core.Logger.verboseLevel;
import com.ce.ems.models.BaseModel;
import com.ce.ems.models.PlatformModel;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.VoidWork;
import com.googlecode.objectify.annotation.Entity;

public class Application {

	public static Boolean isInstalled;
	private static boolean isStarted = false;

	public static void start() {

		if (isStarted) {
			return;
		}

		Logger.debug("Starting Engine ..");

		Logger.enableSystemOutput();

		// @DEV
		Logger.enableSystemOutput();
		Logger.verboseMode(verboseLevel.DEBUG.toString());

		Logger.debug("Scanning for entities");

		new ClasspathScanner<>("Entity", Entity.class, ClassIdentityType.ANNOTATION).scanClasses().forEach(e -> {

			Logger.debug("Registering " + e.getSimpleName());
			ObjectifyService.register(e);
		});

		BaseModel.scanModels();

		ObjectifyService.run(new VoidWork() {
			public void vrun() {

				Application.isInstalled = PlatformModel.isInstalled();

				if (!isInstalled) {

					Logger.debug("Preinstalling Models");

					// Pre install models

					BaseModel.getModels().forEach(e -> {
						Logger.debug("Pre-installing " + e.getClass().getSimpleName());
						e.preInstall();
					});

				} else {

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

}
