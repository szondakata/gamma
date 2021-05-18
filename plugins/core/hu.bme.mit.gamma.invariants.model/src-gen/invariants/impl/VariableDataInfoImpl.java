/**
 */
package invariants.impl;

import hu.bme.mit.gamma.expression.model.Expression;
import hu.bme.mit.gamma.expression.model.LiteralExpression;

import invariants.InvariantsPackage;
import invariants.VariableDataInfo;

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
 * An implementation of the model object '<em><b>Variable Data Info</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link invariants.impl.VariableDataInfoImpl#getValues <em>Values</em>}</li>
 *   <li>{@link invariants.impl.VariableDataInfoImpl#getPredicates <em>Predicates</em>}</li>
 * </ul>
 *
 * @generated
 */
public abstract class VariableDataInfoImpl extends MinimalEObjectImpl.Container implements VariableDataInfo
{
	/**
	 * The cached value of the '{@link #getValues() <em>Values</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getValues()
	 * @generated
	 * @ordered
	 */
	protected EList<LiteralExpression> values;

	/**
	 * The cached value of the '{@link #getPredicates() <em>Predicates</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPredicates()
	 * @generated
	 * @ordered
	 */
	protected EList<Expression> predicates;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected VariableDataInfoImpl()
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
		return InvariantsPackage.Literals.VARIABLE_DATA_INFO;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<LiteralExpression> getValues()
	{
		if (values == null)
		{
			values = new EObjectContainmentEList<LiteralExpression>(LiteralExpression.class, this, InvariantsPackage.VARIABLE_DATA_INFO__VALUES);
		}
		return values;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Expression> getPredicates()
	{
		if (predicates == null)
		{
			predicates = new EObjectContainmentEList<Expression>(Expression.class, this, InvariantsPackage.VARIABLE_DATA_INFO__PREDICATES);
		}
		return predicates;
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
			case InvariantsPackage.VARIABLE_DATA_INFO__VALUES:
				return ((InternalEList<?>)getValues()).basicRemove(otherEnd, msgs);
			case InvariantsPackage.VARIABLE_DATA_INFO__PREDICATES:
				return ((InternalEList<?>)getPredicates()).basicRemove(otherEnd, msgs);
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
			case InvariantsPackage.VARIABLE_DATA_INFO__VALUES:
				return getValues();
			case InvariantsPackage.VARIABLE_DATA_INFO__PREDICATES:
				return getPredicates();
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
			case InvariantsPackage.VARIABLE_DATA_INFO__VALUES:
				getValues().clear();
				getValues().addAll((Collection<? extends LiteralExpression>)newValue);
				return;
			case InvariantsPackage.VARIABLE_DATA_INFO__PREDICATES:
				getPredicates().clear();
				getPredicates().addAll((Collection<? extends Expression>)newValue);
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
			case InvariantsPackage.VARIABLE_DATA_INFO__VALUES:
				getValues().clear();
				return;
			case InvariantsPackage.VARIABLE_DATA_INFO__PREDICATES:
				getPredicates().clear();
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
			case InvariantsPackage.VARIABLE_DATA_INFO__VALUES:
				return values != null && !values.isEmpty();
			case InvariantsPackage.VARIABLE_DATA_INFO__PREDICATES:
				return predicates != null && !predicates.isEmpty();
		}
		return super.eIsSet(featureID);
	}

} //VariableDataInfoImpl
