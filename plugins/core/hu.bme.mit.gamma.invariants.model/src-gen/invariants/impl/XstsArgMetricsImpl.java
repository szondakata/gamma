/**
 */
package invariants.impl;

import invariants.InvariantsPackage;
import invariants.KeyValuePair;
import invariants.XstsArgMetrics;

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
 * An implementation of the model object '<em><b>Xsts Arg Metrics</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link invariants.impl.XstsArgMetricsImpl#getKeyValuePairs <em>Key Value Pairs</em>}</li>
 * </ul>
 *
 * @generated
 */
public class XstsArgMetricsImpl extends MinimalEObjectImpl.Container implements XstsArgMetrics
{
	/**
	 * The cached value of the '{@link #getKeyValuePairs() <em>Key Value Pairs</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getKeyValuePairs()
	 * @generated
	 * @ordered
	 */
	protected EList<KeyValuePair> keyValuePairs;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected XstsArgMetricsImpl()
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
		return InvariantsPackage.Literals.XSTS_ARG_METRICS;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<KeyValuePair> getKeyValuePairs()
	{
		if (keyValuePairs == null)
		{
			keyValuePairs = new EObjectContainmentEList<KeyValuePair>(KeyValuePair.class, this, InvariantsPackage.XSTS_ARG_METRICS__KEY_VALUE_PAIRS);
		}
		return keyValuePairs;
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
			case InvariantsPackage.XSTS_ARG_METRICS__KEY_VALUE_PAIRS:
				return ((InternalEList<?>)getKeyValuePairs()).basicRemove(otherEnd, msgs);
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
			case InvariantsPackage.XSTS_ARG_METRICS__KEY_VALUE_PAIRS:
				return getKeyValuePairs();
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
			case InvariantsPackage.XSTS_ARG_METRICS__KEY_VALUE_PAIRS:
				getKeyValuePairs().clear();
				getKeyValuePairs().addAll((Collection<? extends KeyValuePair>)newValue);
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
			case InvariantsPackage.XSTS_ARG_METRICS__KEY_VALUE_PAIRS:
				getKeyValuePairs().clear();
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
			case InvariantsPackage.XSTS_ARG_METRICS__KEY_VALUE_PAIRS:
				return keyValuePairs != null && !keyValuePairs.isEmpty();
		}
		return super.eIsSet(featureID);
	}

} //XstsArgMetricsImpl
