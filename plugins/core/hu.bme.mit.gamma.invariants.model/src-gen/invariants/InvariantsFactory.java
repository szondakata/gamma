/**
 */
package invariants;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see invariants.InvariantsPackage
 * @generated
 */
public interface InvariantsFactory extends EFactory {
	/**
	 * The singleton instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	InvariantsFactory eINSTANCE = invariants.impl.InvariantsFactoryImpl.init();

	/**
	 * Returns a new object of class '<em>Hello World</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Hello World</em>'.
	 * @generated
	 */
	HelloWorld createHelloWorld();

	/**
	 * Returns the package supported by this factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the package supported by this factory.
	 * @generated
	 */
	InvariantsPackage getInvariantsPackage();

} //InvariantsFactory
