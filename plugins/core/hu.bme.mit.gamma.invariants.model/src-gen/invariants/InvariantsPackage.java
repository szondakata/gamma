/**
 */
package invariants;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;

/**
 * <!-- begin-user-doc -->
 * The <b>Package</b> for the model.
 * It contains accessors for the meta objects to represent
 * <ul>
 *   <li>each class,</li>
 *   <li>each feature of each class,</li>
 *   <li>each operation of each class,</li>
 *   <li>each enum,</li>
 *   <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 * @see invariants.InvariantsFactory
 * @model kind="package"
 * @generated
 */
public interface InvariantsPackage extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "invariants";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "http://www.mit.bme.hu/gamma/invariants/Model/Invariants";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "hu.bme.mit.gamma.invariants";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	InvariantsPackage eINSTANCE = invariants.impl.InvariantsPackageImpl.init();

	/**
	 * The meta object id for the '{@link invariants.impl.HelloWorldImpl <em>Hello World</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see invariants.impl.HelloWorldImpl
	 * @see invariants.impl.InvariantsPackageImpl#getHelloWorld()
	 * @generated
	 */
	int HELLO_WORLD = 0;

	/**
	 * The number of structural features of the '<em>Hello World</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int HELLO_WORLD_FEATURE_COUNT = 0;

	/**
	 * The number of operations of the '<em>Hello World</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int HELLO_WORLD_OPERATION_COUNT = 0;


	/**
	 * Returns the meta object for class '{@link invariants.HelloWorld <em>Hello World</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Hello World</em>'.
	 * @see invariants.HelloWorld
	 * @generated
	 */
	EClass getHelloWorld();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	InvariantsFactory getInvariantsFactory();

	/**
	 * <!-- begin-user-doc -->
	 * Defines literals for the meta objects that represent
	 * <ul>
	 *   <li>each class,</li>
	 *   <li>each feature of each class,</li>
	 *   <li>each operation of each class,</li>
	 *   <li>each enum,</li>
	 *   <li>and each data type</li>
	 * </ul>
	 * <!-- end-user-doc -->
	 * @generated
	 */
	interface Literals {
		/**
		 * The meta object literal for the '{@link invariants.impl.HelloWorldImpl <em>Hello World</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see invariants.impl.HelloWorldImpl
		 * @see invariants.impl.InvariantsPackageImpl#getHelloWorld()
		 * @generated
		 */
		EClass HELLO_WORLD = eINSTANCE.getHelloWorld();

	}

} //InvariantsPackage
