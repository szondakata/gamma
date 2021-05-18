/**
 */
package invariants;

import hu.bme.mit.gamma.expression.model.Expression;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Data Info</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link invariants.DataInfo#getVariables <em>Variables</em>}</li>
 *   <li>{@link invariants.DataInfo#getGlobalPredicates <em>Global Predicates</em>}</li>
 * </ul>
 *
 * @see invariants.InvariantsPackage#getDataInfo()
 * @model abstract="true"
 * @generated
 */
public interface DataInfo extends EObject
{
	/**
	 * Returns the value of the '<em><b>Variables</b></em>' containment reference list.
	 * The list contents are of type {@link invariants.VariablePair}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Variables</em>' containment reference list.
	 * @see invariants.InvariantsPackage#getDataInfo_Variables()
	 * @model containment="true"
	 * @generated
	 */
	EList<VariablePair> getVariables();

	/**
	 * Returns the value of the '<em><b>Global Predicates</b></em>' containment reference list.
	 * The list contents are of type {@link hu.bme.mit.gamma.expression.model.Expression}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Global Predicates</em>' containment reference list.
	 * @see invariants.InvariantsPackage#getDataInfo_GlobalPredicates()
	 * @model containment="true"
	 * @generated
	 */
	EList<Expression> getGlobalPredicates();

} // DataInfo
