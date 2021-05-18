/**
 */
package invariants;

import hu.bme.mit.gamma.statechart.statechart.State;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Key Value Pair</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link invariants.KeyValuePair#getKey <em>Key</em>}</li>
 *   <li>{@link invariants.KeyValuePair#getValue <em>Value</em>}</li>
 * </ul>
 *
 * @see invariants.InvariantsPackage#getKeyValuePair()
 * @model
 * @generated
 */
public interface KeyValuePair extends EObject
{
	/**
	 * Returns the value of the '<em><b>Key</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Key</em>' containment reference.
	 * @see #setKey(State)
	 * @see invariants.InvariantsPackage#getKeyValuePair_Key()
	 * @model containment="true" required="true"
	 * @generated
	 */
	State getKey();

	/**
	 * Sets the value of the '{@link invariants.KeyValuePair#getKey <em>Key</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Key</em>' containment reference.
	 * @see #getKey()
	 * @generated
	 */
	void setKey(State value);

	/**
	 * Returns the value of the '<em><b>Value</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Value</em>' containment reference.
	 * @see #setValue(DataInfo)
	 * @see invariants.InvariantsPackage#getKeyValuePair_Value()
	 * @model containment="true" required="true"
	 * @generated
	 */
	DataInfo getValue();

	/**
	 * Sets the value of the '{@link invariants.KeyValuePair#getValue <em>Value</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Value</em>' containment reference.
	 * @see #getValue()
	 * @generated
	 */
	void setValue(DataInfo value);

} // KeyValuePair
