/**
 */
package invariants;

import hu.bme.mit.gamma.expression.model.Expression;
import hu.bme.mit.gamma.expression.model.LiteralExpression;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Variable Data Info</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link invariants.VariableDataInfo#getValues <em>Values</em>}</li>
 *   <li>{@link invariants.VariableDataInfo#getPredicates <em>Predicates</em>}</li>
 * </ul>
 *
 * @see invariants.InvariantsPackage#getVariableDataInfo()
 * @model abstract="true"
 * @generated
 */
public interface VariableDataInfo extends EObject
{
	/**
	 * Returns the value of the '<em><b>Values</b></em>' containment reference list.
	 * The list contents are of type {@link hu.bme.mit.gamma.expression.model.LiteralExpression}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Values</em>' containment reference list.
	 * @see invariants.InvariantsPackage#getVariableDataInfo_Values()
	 * @model containment="true"
	 * @generated
	 */
	EList<LiteralExpression> getValues();

	/**
	 * Returns the value of the '<em><b>Predicates</b></em>' containment reference list.
	 * The list contents are of type {@link hu.bme.mit.gamma.expression.model.Expression}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Predicates</em>' containment reference list.
	 * @see invariants.InvariantsPackage#getVariableDataInfo_Predicates()
	 * @model containment="true"
	 * @generated
	 */
	EList<Expression> getPredicates();

} // VariableDataInfo
