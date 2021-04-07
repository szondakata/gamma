/*
 * generated by Xtext 2.25.0
 */
package hu.bme.mit.gamma.invariants.language.parser.antlr;

import com.google.inject.Inject;
import hu.bme.mit.gamma.invariants.language.parser.antlr.internal.InternalInvariantsLanguageParser;
import hu.bme.mit.gamma.invariants.language.services.InvariantsLanguageGrammarAccess;
import org.eclipse.xtext.parser.antlr.AbstractAntlrParser;
import org.eclipse.xtext.parser.antlr.XtextTokenStream;

public class InvariantsLanguageParser extends AbstractAntlrParser {

	@Inject
	private InvariantsLanguageGrammarAccess grammarAccess;

	@Override
	protected void setInitialHiddenTokens(XtextTokenStream tokenStream) {
		tokenStream.setInitialHiddenTokens("RULE_WS", "RULE_ML_COMMENT", "RULE_SL_COMMENT");
	}
	

	@Override
	protected InternalInvariantsLanguageParser createParser(XtextTokenStream stream) {
		return new InternalInvariantsLanguageParser(stream, getGrammarAccess());
	}

	@Override 
	protected String getDefaultRuleName() {
		return "HelloWorld";
	}

	public InvariantsLanguageGrammarAccess getGrammarAccess() {
		return this.grammarAccess;
	}

	public void setGrammarAccess(InvariantsLanguageGrammarAccess grammarAccess) {
		this.grammarAccess = grammarAccess;
	}
}
