/**
 */
package invariants;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Xsts Arg Metrics</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link invariants.XstsArgMetrics#getKeyValuePairs <em>Key Value Pairs</em>}</li>
 * </ul>
 *
 * @see invariants.InvariantsPackage#getXstsArgMetrics()
 * @model
 * @generated
 */
public interface XstsArgMetrics extends EObject
{
	/**
	 * Returns the value of the '<em><b>Key Value Pairs</b></em>' containment reference list.
	 * The list contents are of type {@link invariants.KeyValuePair}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Key Value Pairs</em>' containment reference list.
	 * @see invariants.InvariantsPackage#getXstsArgMetrics_KeyValuePairs()
	 * @model containment="true"
	 * @generated
	 */
	EList<KeyValuePair> getKeyValuePairs();

} // XstsArgMetrics
