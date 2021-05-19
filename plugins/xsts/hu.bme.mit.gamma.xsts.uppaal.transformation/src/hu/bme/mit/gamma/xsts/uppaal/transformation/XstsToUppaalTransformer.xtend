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
package hu.bme.mit.gamma.xsts.uppaal.transformation

import hu.bme.mit.gamma.expression.model.ArrayTypeDefinition
import hu.bme.mit.gamma.expression.model.Type
import hu.bme.mit.gamma.expression.model.VariableDeclaration
import hu.bme.mit.gamma.uppaal.util.AssignmentExpressionCreator
import hu.bme.mit.gamma.uppaal.util.NtaBuilder
import hu.bme.mit.gamma.uppaal.util.NtaOptimizer
import hu.bme.mit.gamma.uppaal.util.TypeTransformer
import hu.bme.mit.gamma.xsts.model.AssignmentAction
import hu.bme.mit.gamma.xsts.model.AssumeAction
import hu.bme.mit.gamma.xsts.model.EmptyAction
import hu.bme.mit.gamma.xsts.model.NonDeterministicAction
import hu.bme.mit.gamma.xsts.model.SequentialAction
import hu.bme.mit.gamma.xsts.model.VariableDeclarationAction
import hu.bme.mit.gamma.xsts.model.XSTS
import hu.bme.mit.gamma.xsts.util.XstsActionUtil
import java.util.List
import java.util.Set
import uppaal.NTA
import uppaal.declarations.DataVariablePrefix
import uppaal.declarations.ValueIndex
import uppaal.declarations.VariableContainer
import uppaal.expressions.Expression
import uppaal.templates.Edge
import uppaal.templates.Location
import uppaal.templates.LocationKind

import static extension de.uni_paderborn.uppaal.derivedfeatures.UppaalModelDerivedFeatures.*
import static extension hu.bme.mit.gamma.expression.derivedfeatures.ExpressionModelDerivedFeatures.*
import static extension hu.bme.mit.gamma.uppaal.util.XstsNamings.*
import static extension hu.bme.mit.gamma.xsts.derivedfeatures.XstsDerivedFeatures.*

class XstsToUppaalTransformer {
	
	protected final XSTS xSts
	protected final Traceability traceability
	protected final NTA nta
	// Local variables
	protected final Set<VariableContainer> transientVariables = newHashSet
	// Auxiliary
	protected final extension NtaBuilder ntaBuilder
	protected final extension AssignmentExpressionCreator assignmentExpressionCreator
	protected final extension ExpressionTransformer expressionTransformer
	protected final extension TypeTransformer typeTransformer
	protected final extension NtaOptimizer ntaOptimizer
	
	protected final extension XstsActionUtil xStsActionUtil = XstsActionUtil.INSTANCE
	
	new(XSTS xSts) {
		this.xSts = xSts
		this.ntaBuilder = new NtaBuilder(xSts.name, false)
		this.nta = ntaBuilder.nta
		this.traceability = new Traceability(xSts, nta)
		this.assignmentExpressionCreator = new AssignmentExpressionCreator(ntaBuilder)
		this.expressionTransformer = new ExpressionTransformer(traceability)
		this.typeTransformer = new TypeTransformer(nta)
		this.ntaOptimizer = new NtaOptimizer(ntaBuilder)
	}
	
	def execute() {
		resetCommittedLocationName
		val initialLocation = createTemplateWithInitLoc(templateName, initialLocationName)
		initialLocation.locationTimeKind = LocationKind.COMMITED
		
		val initializingAction = xSts.initializingAction
		val environmentalAction = xSts.environmentalAction
		val mergedAction = xSts.mergedAction
		
		xSts.transformVariables
		
		val stableLocation = initializingAction.transformAction(initialLocation)
		stableLocation.name = stableLocationName
		stableLocation.locationTimeKind = LocationKind.NORMAL
		
		val environmentFinishLocation = environmentalAction.transformAction(stableLocation)
		// If there is no environmental action, the stable location should not be overwritten 
		if (environmentFinishLocation !== stableLocation) {
			environmentFinishLocation.name = environmentFinishLocationName
			environmentFinishLocation.locationTimeKind = LocationKind.NORMAL // So optimization does not delete it
		}
		
		val systemFinishLocation = mergedAction.transformAction(environmentFinishLocation)
		
		// If there is no merged action, the loop edge is unnecessary
		if (systemFinishLocation !== stableLocation) {
			val lastEdge = systemFinishLocation.createEdge(stableLocation)
			lastEdge.resetTransientVariables(transientVariables)
		}
		
		// Optimizing edges from these location
		initialLocation.optimizeSubsequentEdges
		stableLocation.optimizeSubsequentEdges
		environmentFinishLocation.optimizeSubsequentEdges
		
		if (environmentFinishLocation !== stableLocation) {
			// Model checking is faster if the environment finish location is committed
			environmentFinishLocation.locationTimeKind = LocationKind.COMMITED
		}
		
		ntaBuilder.instantiateTemplates
		
		return ntaBuilder.nta
	}
	
	 // Transform variables
	
	protected def transformVariables(XSTS xSts) {
		for (xStsVariable : xSts.variableDeclarations) {
			xStsVariable.transformAndTraceVariable
		}
	}
	
	protected def transformAndTraceVariable(VariableDeclaration variable) {
		val uppaalVariable = variable.transformVariable
		nta.globalDeclarations.declaration += uppaalVariable
		traceability.put(variable, uppaalVariable)
		return uppaalVariable
	}
	
	protected def transformVariable(VariableDeclaration variable) {
		val type = variable.type
		val uppaalType =
		if (xSts.clockVariables.contains(variable)) {
			nta.clock.createTypeReference
		}
		else {
			type.transformType
		}
		val uppaalVariable = uppaalType.createVariable(variable.uppaalId)
		// In UPPAAL, array sizes are stuck to variables
		uppaalVariable.onlyVariable.index += type.transformArrayIndexes
		
		return uppaalVariable
	}
	
	protected def List<ValueIndex> transformArrayIndexes(Type type) {
		val indexes = newArrayList
		val typeDefinition = type.typeDefinition
		if (typeDefinition instanceof ArrayTypeDefinition) {
			val size = typeDefinition.size
			val elementType = typeDefinition.elementType
			indexes += size.transform.createIndex
			indexes += elementType.transformArrayIndexes
		}
		return indexes
	}
	
	// Action dispatch
	
	protected def dispatch Location transformAction(EmptyAction action, Location source) {
		return source
	}
	
	protected def dispatch Location transformAction(AssignmentAction action, Location source) {
		val xStsDeclaration = action.lhs.declaration
		val xStsVariable = xStsDeclaration as VariableDeclaration
		val uppaalVariable = traceability.get(xStsVariable)
		val uppaalRhs = action.rhs.transform
		return source.createUpdateEdge(uppaalVariable, uppaalRhs)
	}
	
	protected def dispatch Location transformAction(VariableDeclarationAction action, Location source) {
		val xStsVariable = action.variableDeclaration
		val uppaalVariable = xStsVariable.transformAndTraceVariable
		uppaalVariable.prefix = DataVariablePrefix.META // Works if local variable assignments are deterministic
		uppaalVariable.extendNameWithHash // Needed for local declarations
		transientVariables += uppaalVariable
		val xStsInitialValue = xStsVariable.initialValue
		val uppaalRhs = xStsInitialValue?.transform
		return source.createUpdateEdge(uppaalVariable, uppaalRhs) 
	}
	
	protected def createUpdateEdge(Location source,
			VariableContainer uppaalVariable, Expression uppaalRhs) {
		val edge = source.createEdgeCommittedSource(nextCommittedLocationName)
		if (uppaalRhs !== null) {
			edge.update += uppaalVariable.createAssignmentExpression(uppaalRhs)
		}
		return edge.target
	}
	
	protected def void extendNameWithHash(VariableContainer uppaalContainer) {
		for (uppaalVariable : uppaalContainer.variable) {
			uppaalVariable.name = '''«uppaalVariable.name»_«uppaalVariable.hashCode»'''
		}
	}
	
	protected def dispatch Location transformAction(AssumeAction action, Location source) {
		val edge = source.createEdgeCommittedSource(nextCommittedLocationName)
		val uppaalExpression = action.assumption.transform
		edge.guard = uppaalExpression
		return edge.target
	}
	
	protected def dispatch Location transformAction(SequentialAction action, Location source) {
		val xStsActions = action.actions
		var actualSource = source
		for (xStsAction : xStsActions) {
			actualSource = xStsAction.transformAction(actualSource)
		}
		return actualSource
	}
	
	protected def dispatch Location transformAction(NonDeterministicAction action, Location source) {
		val xStsActions = action.actions
		val targets = newArrayList
		for (xStsAction : xStsActions) {
			targets += xStsAction.transformAction(source)
		}
		val parentTemplate = source.parentTemplate
		val target = parentTemplate.createLocation(LocationKind.COMMITED, nextCommittedLocationName)
		for (choiceTarget : targets) {
			choiceTarget.createEdge(target)
		}
		return target
	}
	
	// Reseting
	
	protected def resetTransientVariables(Edge edge, Set<VariableContainer> transientVariables) {
		for (transientVariable : transientVariables) {
			edge.update += transientVariable.createResetingAssignmentExpression
		}
	}
	
}