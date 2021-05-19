/*
 * generated by Xtext 2.25.0
 */
package hu.bme.mit.gamma.invariants.language.ui.internal;

import com.google.common.collect.Maps;
import com.google.inject.Guice;
import com.google.inject.Injector;
import hu.bme.mit.gamma.invariants.language.InvariantsLanguageRuntimeModule;
import hu.bme.mit.gamma.invariants.language.ui.InvariantsLanguageUiModule;
import java.util.Collections;
import java.util.Map;
import org.apache.log4j.Logger;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.eclipse.xtext.ui.shared.SharedStateModule;
import org.eclipse.xtext.util.Modules2;
import org.osgi.framework.BundleContext;

/**
 * This class was generated. Customizations should only happen in a newly
 * introduced subclass. 
 */
public class LanguageActivator extends AbstractUIPlugin {

	public static final String PLUGIN_ID = "hu.bme.mit.gamma.invariants.language.ui";
	public static final String HU_BME_MIT_GAMMA_INVARIANTS_LANGUAGE_INVARIANTSLANGUAGE = "hu.bme.mit.gamma.invariants.language.InvariantsLanguage";
	
	private static final Logger logger = Logger.getLogger(LanguageActivator.class);
	
	private static LanguageActivator INSTANCE;
	
	private Map<String, Injector> injectors = Collections.synchronizedMap(Maps.<String, Injector> newHashMapWithExpectedSize(1));
	
	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		INSTANCE = this;
	}
	
	@Override
	public void stop(BundleContext context) throws Exception {
		injectors.clear();
		INSTANCE = null;
		super.stop(context);
	}
	
	public static LanguageActivator getInstance() {
		return INSTANCE;
	}
	
	public Injector getInjector(String language) {
		synchronized (injectors) {
			Injector injector = injectors.get(language);
			if (injector == null) {
				injectors.put(language, injector = createInjector(language));
			}
			return injector;
		}
	}
	
	protected Injector createInjector(String language) {
		try {
			com.google.inject.Module runtimeModule = getRuntimeModule(language);
			com.google.inject.Module sharedStateModule = getSharedStateModule();
			com.google.inject.Module uiModule = getUiModule(language);
			com.google.inject.Module mergedModule = Modules2.mixin(runtimeModule, sharedStateModule, uiModule);
			return Guice.createInjector(mergedModule);
		} catch (Exception e) {
			logger.error("Failed to create injector for " + language);
			logger.error(e.getMessage(), e);
			throw new RuntimeException("Failed to create injector for " + language, e);
		}
	}
	
	protected com.google.inject.Module getRuntimeModule(String grammar) {
		if (HU_BME_MIT_GAMMA_INVARIANTS_LANGUAGE_INVARIANTSLANGUAGE.equals(grammar)) {
			return new InvariantsLanguageRuntimeModule();
		}
		throw new IllegalArgumentException(grammar);
	}
	
	protected com.google.inject.Module getUiModule(String grammar) {
		if (HU_BME_MIT_GAMMA_INVARIANTS_LANGUAGE_INVARIANTSLANGUAGE.equals(grammar)) {
			return new InvariantsLanguageUiModule(this);
		}
		throw new IllegalArgumentException(grammar);
	}
	
	protected com.google.inject.Module getSharedStateModule() {
		return new SharedStateModule();
	}
	
	
}