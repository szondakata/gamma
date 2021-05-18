/**
 */
package invariants;

import hu.bme.mit.gamma.expression.model.VariableDeclaration;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Variable Pair</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link invariants.VariablePair#getKey <em>Key</em>}</li>
 *   <li>{@link invariants.VariablePair#getValue <em>Value</em>}</li>
 * </ul>
 *
 * @see invariants.InvariantsPackage#getVariablePair()
 * @model
 * @generated
 */
public interface VariablePair extends EObject
{
	/**
	 * Returns the value of the '<em><b>Key</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Key</em>' reference.
	 * @see #setKey(VariableDeclaration)
	 * @see invariants.InvariantsPackage#getVariablePair_Key()
	 * @model required="true"
	 * @generated
	 */
	VariableDeclaration getKey();

	/**
	 * Sets the value of the '{@link invariants.VariablePair#getKey <em>Key</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Key</em>' reference.
	 * @see #getKey()
	 * @generated
	 */
	void setKey(VariableDeclaration value);

	/**
	 * Returns the value of the '<em><b>Value</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Value</em>' containment reference.
	 * @see #setValue(VariableDataInfo)
	 * @see invariants.InvariantsPackage#getVariablePair_Value()
	 * @model containment="true" required="true"
	 * @generated
	 */
	VariableDataInfo getValue();

	/**
	 * Sets the value of the '{@link invariants.VariablePair#getValue <em>Value</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Value</em>' containment reference.
	 * @see #getValue()
	 * @generated
	 */
	void setValue(VariableDataInfo value);

} // VariablePair
