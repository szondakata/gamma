/**
 */
package invariants.impl;

import hu.bme.mit.gamma.expression.model.Expression;

import invariants.DataInfo;
import invariants.InvariantsPackage;
import invariants.VariablePair;

import java.util.Collection;

import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Data Info</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link invariants.impl.DataInfoImpl#getVariables <em>Variables</em>}</li>
 *   <li>{@link invariants.impl.DataInfoImpl#getGlobalPredicates <em>Global Predicates</em>}</li>
 * </ul>
 *
 * @generated
 */
public abstract class DataInfoImpl extends MinimalEObjectImpl.Container implements DataInfo
{
	/**
	 * The cached value of the '{@link #getVariables() <em>Variables</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getVariables()
	 * @generated
	 * @ordered
	 */
	protected EList<VariablePair> variables;

	/**
	 * The cached value of the '{@link #getGlobalPredicates() <em>Global Predicates</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getGlobalPredicates()
	 * @generated
	 * @ordered
	 */
	protected EList<Expression> globalPredicates;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected DataInfoImpl()
	{
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass()
	{
		return InvariantsPackage.Literals.DATA_INFO;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<VariablePair> getVariables()
	{
		if (variables == null)
		{
			variables = new EObjectContainmentEList<VariablePair>(VariablePair.class, this, InvariantsPackage.DATA_INFO__VARIABLES);
		}
		return variables;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Expression> getGlobalPredicates()
	{
		if (globalPredicates == null)
		{
			globalPredicates = new EObjectContainmentEList<Expression>(Expression.class, this, InvariantsPackage.DATA_INFO__GLOBAL_PREDICATES);
		}
		return globalPredicates;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs)
	{
		switch (featureID)
		{
			case InvariantsPackage.DATA_INFO__VARIABLES:
				return ((InternalEList<?>)getVariables()).basicRemove(otherEnd, msgs);
			case InvariantsPackage.DATA_INFO__GLOBAL_PREDICATES:
				return ((InternalEList<?>)getGlobalPredicates()).basicRemove(otherEnd, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType)
	{
		switch (featureID)
		{
			case InvariantsPackage.DATA_INFO__VARIABLES:
				return getVariables();
			case InvariantsPackage.DATA_INFO__GLOBAL_PREDICATES:
				return getGlobalPredicates();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void eSet(int featureID, Object newValue)
	{
		switch (featureID)
		{
			case InvariantsPackage.DATA_INFO__VARIABLES:
				getVariables().clear();
				getVariables().addAll((Collection<? extends VariablePair>)newValue);
				return;
			case InvariantsPackage.DATA_INFO__GLOBAL_PREDICATES:
				getGlobalPredicates().clear();
				getGlobalPredicates().addAll((Collection<? extends Expression>)newValue);
				return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eUnset(int featureID)
	{
		switch (featureID)
		{
			case InvariantsPackage.DATA_INFO__VARIABLES:
				getVariables().clear();
				return;
			case InvariantsPackage.DATA_INFO__GLOBAL_PREDICATES:
				getGlobalPredicates().clear();
				return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID)
	{
		switch (featureID)
		{
			case InvariantsPackage.DATA_INFO__VARIABLES:
				return variables != null && !variables.isEmpty();
			case InvariantsPackage.DATA_INFO__GLOBAL_PREDICATES:
				return globalPredicates != null && !globalPredicates.isEmpty();
		}
		return super.eIsSet(featureID);
	}

} //DataInfoImpl
