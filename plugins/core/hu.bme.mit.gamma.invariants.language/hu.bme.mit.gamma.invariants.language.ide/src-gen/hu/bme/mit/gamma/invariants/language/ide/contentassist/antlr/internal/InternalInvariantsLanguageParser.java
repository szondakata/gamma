package hu.bme.mit.gamma.invariants.language.ide.contentassist.antlr.internal;

import java.io.InputStream;
import org.eclipse.xtext.*;
import org.eclipse.xtext.parser.*;
import org.eclipse.xtext.parser.impl.*;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.parser.antlr.XtextTokenStream;
import org.eclipse.xtext.parser.antlr.XtextTokenStream.HiddenTokens;
import org.eclipse.xtext.ide.editor.contentassist.antlr.internal.AbstractInternalContentAssistParser;
import org.eclipse.xtext.ide.editor.contentassist.antlr.internal.DFA;
import hu.bme.mit.gamma.invariants.language.services.InvariantsLanguageGrammarAccess;



import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

@SuppressWarnings("all")
public class InternalInvariantsLanguageParser extends AbstractInternalContentAssistParser {
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

    	public void setGrammarAccess(InvariantsLanguageGrammarAccess grammarAccess) {
    		this.grammarAccess = grammarAccess;
    	}

    	@Override
    	protected Grammar getGrammar() {
    		return grammarAccess.getGrammar();
    	}

    	@Override
    	protected String getValueForTokenName(String tokenName) {
    		return tokenName;
    	}



    // $ANTLR start "entryRuleHelloWorld"
    // InternalInvariantsLanguage.g:53:1: entryRuleHelloWorld : ruleHelloWorld EOF ;
    public final void entryRuleHelloWorld() throws RecognitionException {
        try {
            // InternalInvariantsLanguage.g:54:1: ( ruleHelloWorld EOF )
            // InternalInvariantsLanguage.g:55:1: ruleHelloWorld EOF
            {
             before(grammarAccess.getHelloWorldRule()); 
            pushFollow(FOLLOW_1);
            ruleHelloWorld();

            state._fsp--;

             after(grammarAccess.getHelloWorldRule()); 
            match(input,EOF,FOLLOW_2); 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end "entryRuleHelloWorld"


    // $ANTLR start "ruleHelloWorld"
    // InternalInvariantsLanguage.g:62:1: ruleHelloWorld : ( ( rule__HelloWorld__Group__0 ) ) ;
    public final void ruleHelloWorld() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalInvariantsLanguage.g:66:2: ( ( ( rule__HelloWorld__Group__0 ) ) )
            // InternalInvariantsLanguage.g:67:2: ( ( rule__HelloWorld__Group__0 ) )
            {
            // InternalInvariantsLanguage.g:67:2: ( ( rule__HelloWorld__Group__0 ) )
            // InternalInvariantsLanguage.g:68:3: ( rule__HelloWorld__Group__0 )
            {
             before(grammarAccess.getHelloWorldAccess().getGroup()); 
            // InternalInvariantsLanguage.g:69:3: ( rule__HelloWorld__Group__0 )
            // InternalInvariantsLanguage.g:69:4: rule__HelloWorld__Group__0
            {
            pushFollow(FOLLOW_2);
            rule__HelloWorld__Group__0();

            state._fsp--;


            }

             after(grammarAccess.getHelloWorldAccess().getGroup()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "ruleHelloWorld"


    // $ANTLR start "rule__HelloWorld__Group__0"
    // InternalInvariantsLanguage.g:77:1: rule__HelloWorld__Group__0 : rule__HelloWorld__Group__0__Impl rule__HelloWorld__Group__1 ;
    public final void rule__HelloWorld__Group__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalInvariantsLanguage.g:81:1: ( rule__HelloWorld__Group__0__Impl rule__HelloWorld__Group__1 )
            // InternalInvariantsLanguage.g:82:2: rule__HelloWorld__Group__0__Impl rule__HelloWorld__Group__1
            {
            pushFollow(FOLLOW_3);
            rule__HelloWorld__Group__0__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__HelloWorld__Group__1();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__HelloWorld__Group__0"


    // $ANTLR start "rule__HelloWorld__Group__0__Impl"
    // InternalInvariantsLanguage.g:89:1: rule__HelloWorld__Group__0__Impl : ( () ) ;
    public final void rule__HelloWorld__Group__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalInvariantsLanguage.g:93:1: ( ( () ) )
            // InternalInvariantsLanguage.g:94:1: ( () )
            {
            // InternalInvariantsLanguage.g:94:1: ( () )
            // InternalInvariantsLanguage.g:95:2: ()
            {
             before(grammarAccess.getHelloWorldAccess().getHelloWorldAction_0()); 
            // InternalInvariantsLanguage.g:96:2: ()
            // InternalInvariantsLanguage.g:96:3: 
            {
            }

             after(grammarAccess.getHelloWorldAccess().getHelloWorldAction_0()); 

            }


            }

        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__HelloWorld__Group__0__Impl"


    // $ANTLR start "rule__HelloWorld__Group__1"
    // InternalInvariantsLanguage.g:104:1: rule__HelloWorld__Group__1 : rule__HelloWorld__Group__1__Impl ;
    public final void rule__HelloWorld__Group__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalInvariantsLanguage.g:108:1: ( rule__HelloWorld__Group__1__Impl )
            // InternalInvariantsLanguage.g:109:2: rule__HelloWorld__Group__1__Impl
            {
            pushFollow(FOLLOW_2);
            rule__HelloWorld__Group__1__Impl();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__HelloWorld__Group__1"


    // $ANTLR start "rule__HelloWorld__Group__1__Impl"
    // InternalInvariantsLanguage.g:115:1: rule__HelloWorld__Group__1__Impl : ( 'HelloWorld' ) ;
    public final void rule__HelloWorld__Group__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalInvariantsLanguage.g:119:1: ( ( 'HelloWorld' ) )
            // InternalInvariantsLanguage.g:120:1: ( 'HelloWorld' )
            {
            // InternalInvariantsLanguage.g:120:1: ( 'HelloWorld' )
            // InternalInvariantsLanguage.g:121:2: 'HelloWorld'
            {
             before(grammarAccess.getHelloWorldAccess().getHelloWorldKeyword_1()); 
            match(input,11,FOLLOW_2); 
             after(grammarAccess.getHelloWorldAccess().getHelloWorldKeyword_1()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__HelloWorld__Group__1__Impl"

    // Delegated rules


 

    public static final BitSet FOLLOW_1 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_2 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_3 = new BitSet(new long[]{0x0000000000000800L});

}