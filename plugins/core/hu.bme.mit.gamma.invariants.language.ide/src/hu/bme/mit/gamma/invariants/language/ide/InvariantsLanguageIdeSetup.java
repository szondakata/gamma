/*
 * generated by Xtext 2.25.0
 */
package hu.bme.mit.gamma.invariants.language.ide;

import com.google.inject.Guice;
import com.google.inject.Injector;
import hu.bme.mit.gamma.invariants.language.InvariantsLanguageRuntimeModule;
import hu.bme.mit.gamma.invariants.language.InvariantsLanguageStandaloneSetup;
import org.eclipse.xtext.util.Modules2;

/**
 * Initialization support for running Xtext languages as language servers.
 */
public class InvariantsLanguageIdeSetup extends InvariantsLanguageStandaloneSetup {

	@Override
	public Injector createInjector() {
		return Guice.createInjector(Modules2.mixin(new InvariantsLanguageRuntimeModule(), new InvariantsLanguageIdeModule()));
	}
	
}
