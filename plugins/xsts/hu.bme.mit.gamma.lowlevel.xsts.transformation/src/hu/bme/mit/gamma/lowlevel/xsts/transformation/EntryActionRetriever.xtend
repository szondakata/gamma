/********************************************************************************
 * Copyright (c) 2018-2020 Contributors to the Gamma project
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * SPDX-License-Identifier: EPL-1.0
 ********************************************************************************/
package hu.bme.mit.gamma.lowlevel.xsts.transformation

import hu.bme.mit.gamma.expression.model.ExpressionModelFactory
import hu.bme.mit.gamma.statechart.lowlevel.model.Region
import hu.bme.mit.gamma.statechart.lowlevel.model.State
import hu.bme.mit.gamma.statechart.lowlevel.model.StateNode
import hu.bme.mit.gamma.xsts.model.model.Action
import hu.bme.mit.gamma.xsts.model.model.ParallelAction
import hu.bme.mit.gamma.xsts.model.model.XSTSModelFactory
import hu.bme.mit.gamma.xsts.model.util.XSTSActionUtil
import java.util.List

import static extension hu.bme.mit.gamma.statechart.lowlevel.model.derivedfeatures.LowlevelStatechartModelDerivedFeatures.*

class EntryActionRetriever {
	// Model factories
	protected final extension XSTSModelFactory factory = XSTSModelFactory.eINSTANCE
	protected final extension ExpressionModelFactory constraintFactory = ExpressionModelFactory.eINSTANCE
	// Auxiliary object
	protected final extension XSTSActionUtil actionFactory = XSTSActionUtil.INSTANCE
	protected final extension StateAssumptionCreator stateAssumptionCreator
	protected final extension ActionTransformer actionTransformer
	// Trace
	protected final Trace trace
	
	new(Trace trace) {
		this.trace = trace
		this.stateAssumptionCreator = new StateAssumptionCreator(this.trace)
		this.actionTransformer = new ActionTransformer(this.trace)
	}
	
	// Parent region handling
	
	protected def Action createRecursiveXStsParentStateEntryActions(StateNode lowlevelState,
			State lowlevelTopState, boolean inclusiveTopState) {
		val lowlevelParentState = lowlevelState.parentState
		val lowlevelParentRegion = lowlevelState.parentRegion
		if (lowlevelParentRegion.isTopRegion ||
				(inclusiveTopState && lowlevelState == lowlevelTopState) ||
				(!inclusiveTopState && lowlevelParentState == lowlevelTopState)) {
			// Recursion for the exit action of parent states IF the top level state is not yet reached
			return createEmptyAction
		}
		val xStsEntryAction = createSequentialAction => [
			// Recursion
			it.actions += lowlevelParentState.createRecursiveXStsParentStateEntryActions(lowlevelTopState, inclusiveTopState)
			// This level
			val xStsStateAssumption = lowlevelParentState.createSingleXStsStateAssumption
			it.actions += xStsStateAssumption.createIfAction(lowlevelParentState.entryAction.transformAction)
			// Action taken only if the state is "active" (assume action)
		]
		return xStsEntryAction
	}
	
	/**
	 * Creates the xSTS entry actions of the parent state (in correct order) and all ancestor
	 * states recursively up until the given top level state (its entry action is still regarded, but its parent states' are not).
	 */
	protected def Action createRecursiveXStsParentStateEntryActionsWithOrthogonality(
			StateNode lowlevelStateNode, State lowlevelTopState) {
		val lowlevelParentRegion = lowlevelStateNode.parentRegion
		if (lowlevelParentRegion.isTopRegion || lowlevelStateNode == lowlevelTopState) {
			// Recursion for the exit action of parent states IF the top level state is not yet reached
			return createEmptyAction
		}
		val lowlevelParentState = lowlevelStateNode.parentState
		val lowlevelGrandparentRegion = lowlevelParentState.parentRegion
		val xStsEntryAction = createSequentialAction => [
			// Recursion
			it.actions += lowlevelParentState.createRecursiveXStsParentStateEntryActionsWithOrthogonality(lowlevelTopState)
			// This level
			val xStsStateAssumption = lowlevelParentState.createSingleXStsStateAssumption
			// Action taken only if the state is "active" (assume action)
			val xStsStateEntryAction = xStsStateAssumption.createIfAction(lowlevelParentState.entryAction.transformAction)
			if (lowlevelGrandparentRegion.hasOrthogonalRegion) {
				// Orthogonal region exit actions
				it.actions += lowlevelGrandparentRegion.createRecursiveXStsOrthogonalRegionEntryActions as ParallelAction => [
					it.actions += xStsStateEntryAction
				]
			}
			// No orthogonality
			else {
				it.actions += xStsStateEntryAction
			}
		]
		return xStsEntryAction
	}
	
	
	// Subregion handling
	
	protected def createRecursiveXStsRegionAndSubregionEntryActions(Region lowlevelRegion) {
		return createNonDeterministicAction => [
			for (lowlevelSubstate : lowlevelRegion.stateNodes.filter(State)) {
				it.actions += lowlevelSubstate.createRecursiveXStsStateAndSubstateEntryActions
			}
		]
	}
	
	protected def createRecursiveXStsOrthogonalRegionEntryActions(Region lowlevelRegion) {
		if (!lowlevelRegion.hasOrthogonalRegion) {
			return createEmptyAction
		}
		return createParallelAction => [
			for (lowlevelOrthogonalRegion : lowlevelRegion.orthogonalRegions) {
				it.actions += lowlevelOrthogonalRegion.createRecursiveXStsRegionAndSubregionEntryActions
			}
		]
	}
	
	/**
	 * Creates the xSTS entry actions of the given state (in correct order) all contained states 
	 * and all states of all orthogonal regions recursively.
	 */
	protected def Action createRecursiveXStsStateAndSubstateEntryActionsWithOrthogonality(State lowlevelState) {
		val XStsStateAndSubstateEntryActions = lowlevelState.createRecursiveXStsStateAndSubstateEntryActions
		val lowlevelParentRegion = lowlevelState.parentRegion
		if (!lowlevelParentRegion.hasOrthogonalRegion) {
			return XStsStateAndSubstateEntryActions
		}
		// Has orthogonal regions
		return createParallelAction => [
			it.actions += XStsStateAndSubstateEntryActions
			// Orthogonal region actions
			for (lowlevelOrthogonalRegion : lowlevelParentRegion.orthogonalRegions) {
				for (lowlevelSubstate : lowlevelOrthogonalRegion.stateNodes.filter(State)) {
					it.actions += lowlevelSubstate.createRecursiveXStsStateAndSubstateEntryActions
				}
			}
		]
	}
	
	protected def Action createRecursiveXStsStateAndSubstateEntryActions(State lowlevelState) {
		val xStsStateAssumption = lowlevelState.createSingleXStsStateAssumption
		// Action taken only if the state is "active" (assume action)
		val xStsStateEntryActions = lowlevelState.entryAction.transformAction
		val List<Action> xStsSubstateEntryActions = newLinkedList
		// Recursion for the entry action of contained states
		for (lowlevelSubregion : lowlevelState.regions) {
			xStsSubstateEntryActions += createParallelAction => [
				it.actions += createNonDeterministicAction => [
					for (lowlevelSubstate : lowlevelSubregion.stateNodes.filter(State)) {
						it.actions += lowlevelSubstate.createRecursiveXStsStateAndSubstateEntryActions
					}
				]
			]
		}
		// This is wrapped in a NonDeterministicAction, therefore it is createIfActionBranch and not createIfAction
		return xStsStateAssumption.createIfActionBranch(
			createSequentialAction => [
				it.actions += xStsStateEntryActions
				// Order is very important
				it.actions += xStsSubstateEntryActions
			]
		)
	}
	
	protected def Action createSingleXStsAssumeStateEntryActions(State lowlevelState) {
		val xStsStateExitActions = lowlevelState.entryAction.transformAction
		val xStsStateAssumption = lowlevelState.createSingleXStsStateAssumption
		// Action taken only if the state is "active" (assume action)
		return xStsStateAssumption.createIfActionBranch(xStsStateExitActions)
	}
	
	
}