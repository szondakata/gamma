package hu.bme.mit.gamma.invariants.language.parser.antlr.internal;

import org.eclipse.xtext.*;
import org.eclipse.xtext.parser.*;
import org.eclipse.xtext.parser.impl.*;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.parser.antlr.AbstractInternalAntlrParser;
import org.eclipse.xtext.parser.antlr.XtextTokenStream;
import org.eclipse.xtext.parser.antlr.XtextTokenStream.HiddenTokens;
import org.eclipse.xtext.parser.antlr.AntlrDatatypeRuleToken;
import hu.bme.mit.gamma.invariants.language.services.InvariantsLanguageGrammarAccess;



import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

@SuppressWarnings("all")
public class InternalInvariantsLanguageParser extends AbstractInternalAntlrParser {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "RULE_ID", "RULE_INT", "RULE_STRING", "RULE_ML_COMMENT", "RULE_SL_COMMENT", "RULE_WS", "RULE_ANY_OTHER", "'HelloWorld'"
    };
    public static final int RULE_ID=4;
    public static final int RULE_WS=9;
    public static final int RULE_STRING=6;
    public static final int RULE_ANY_OTHER=10;
    public static final int RULE_SL_COMMENT=8;
    public static final int RULE_INT=5;
    public static final int T__11=11;
    public static final int RULE_ML_COMMENT=7;
    public static final int EOF=-1;

    // delegates
    // delegators


        public InternalInvariantsLanguageParser(TokenStream input) {
            this(input, new RecognizerSharedState());
        }
        public InternalInvariantsLanguageParser(TokenStream input, RecognizerSharedState state) {
            super(input, state);
             
        }
        

    public String[] getTokenNames() { return InternalInvariantsLanguageParser.tokenNames; }
    public String getGrammarFileName() { return "InternalInvariantsLanguage.g"; }



     	private InvariantsLanguageGrammarAccess grammarAccess;

        public InternalInvariantsLanguageParser(TokenStream input, InvariantsLanguageGrammarAccess grammarAccess) {
            this(input);
            this.grammarAccess = grammarAccess;
            registerRules(grammarAccess.getGrammar());
        }

        @Override
        protected String getFirstRuleName() {
        	return "HelloWorld";
       	}

       	@Override
       	protected InvariantsLanguageGrammarAccess getGrammarAccess() {
       		return grammarAccess;
       	}




    // $ANTLR start "entryRuleHelloWorld"
    // InternalInvariantsLanguage.g:64:1: entryRuleHelloWorld returns [EObject current=null] : iv_ruleHelloWorld= ruleHelloWorld EOF ;
    public final EObject entryRuleHelloWorld() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleHelloWorld = null;


        try {
            // InternalInvariantsLanguage.g:64:51: (iv_ruleHelloWorld= ruleHelloWorld EOF )
            // InternalInvariantsLanguage.g:65:2: iv_ruleHelloWorld= ruleHelloWorld EOF
            {
             newCompositeNode(grammarAccess.getHelloWorldRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleHelloWorld=ruleHelloWorld();

            state._fsp--;

             current =iv_ruleHelloWorld; 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleHelloWorld"


    // $ANTLR start "ruleHelloWorld"
    // InternalInvariantsLanguage.g:71:1: ruleHelloWorld returns [EObject current=null] : ( () otherlv_1= 'HelloWorld' ) ;
    public final EObject ruleHelloWorld() throws RecognitionException {
        EObject current = null;

        Token otherlv_1=null;


        	enterRule();

        try {
            // InternalInvariantsLanguage.g:77:2: ( ( () otherlv_1= 'HelloWorld' ) )
            // InternalInvariantsLanguage.g:78:2: ( () otherlv_1= 'HelloWorld' )
            {
            // InternalInvariantsLanguage.g:78:2: ( () otherlv_1= 'HelloWorld' )
            // InternalInvariantsLanguage.g:79:3: () otherlv_1= 'HelloWorld'
            {
            // InternalInvariantsLanguage.g:79:3: ()
            // InternalInvariantsLanguage.g:80:4: 
            {

            				current = forceCreateModelElement(
            					grammarAccess.getHelloWorldAccess().getHelloWorldAction_0(),
            					current);
            			

            }

            otherlv_1=(Token)match(input,11,FOLLOW_2); 

            			newLeafNode(otherlv_1, grammarAccess.getHelloWorldAccess().getHelloWorldKeyword_1());
            		

            }


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleHelloWorld"

    // Delegated rules


 

    public static final BitSet FOLLOW_1 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_2 = new BitSet(new long[]{0x0000000000000002L});

}