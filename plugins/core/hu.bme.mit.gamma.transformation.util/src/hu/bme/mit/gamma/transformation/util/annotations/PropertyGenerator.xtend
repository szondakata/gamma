/** 
 * Copyright (c) 2018-2020 Contributors to the Gamma project
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * SPDX-License-Identifier: EPL-1.0
 */
package hu.bme.mit.gamma.transformation.util.annotations

import hu.bme.mit.gamma.expression.model.BooleanTypeDefinition
import hu.bme.mit.gamma.expression.model.DirectReferenceExpression
import hu.bme.mit.gamma.expression.model.EnumerationTypeDefinition
import hu.bme.mit.gamma.expression.model.Expression
import hu.bme.mit.gamma.expression.model.ExpressionModelFactory
import hu.bme.mit.gamma.expression.model.ParameterDeclaration
import hu.bme.mit.gamma.expression.model.VariableDeclaration
import hu.bme.mit.gamma.property.model.CommentableStateFormula
import hu.bme.mit.gamma.property.model.ComponentInstanceVariableReference
import hu.bme.mit.gamma.property.model.PropertyModelFactory
import hu.bme.mit.gamma.property.model.PropertyPackage
import hu.bme.mit.gamma.property.util.PropertyUtil
import hu.bme.mit.gamma.statechart.composite.ComponentInstance
import hu.bme.mit.gamma.statechart.composite.ComponentInstanceReference
import hu.bme.mit.gamma.statechart.composite.CompositeModelFactory
import hu.bme.mit.gamma.statechart.composite.SynchronousComponentInstance
import hu.bme.mit.gamma.statechart.derivedfeatures.StatechartModelDerivedFeatures
import hu.bme.mit.gamma.statechart.interface_.Component
import hu.bme.mit.gamma.statechart.interface_.EventParameterReferenceExpression
import hu.bme.mit.gamma.statechart.interface_.Package
import hu.bme.mit.gamma.statechart.interface_.Port
import hu.bme.mit.gamma.statechart.statechart.RaiseEventAction
import hu.bme.mit.gamma.statechart.statechart.State
import hu.bme.mit.gamma.statechart.statechart.StateNode
import hu.bme.mit.gamma.statechart.statechart.StatechartDefinition
import hu.bme.mit.gamma.statechart.statechart.Transition
import hu.bme.mit.gamma.statechart.util.ExpressionSerializer
import hu.bme.mit.gamma.statechart.util.StatechartUtil
import hu.bme.mit.gamma.transformation.util.annotations.GammaStatechartAnnotator.DefUseReferences
import hu.bme.mit.gamma.transformation.util.annotations.GammaStatechartAnnotator.InteractionAnnotations
import hu.bme.mit.gamma.transformation.util.annotations.GammaStatechartAnnotator.TransitionAnnotations
import hu.bme.mit.gamma.transformation.util.annotations.GammaStatechartAnnotator.TransitionPairAnnotation
import hu.bme.mit.gamma.util.GammaEcoreUtil
import java.util.Collection
import java.util.Collections
import java.util.List
import java.util.Set
import org.eclipse.emf.ecore.EObject

import static extension hu.bme.mit.gamma.statechart.derivedfeatures.StatechartModelDerivedFeatures.*

class PropertyGenerator {
	// Single component reference or the whole chain is needed
	// That is, we reference the model AFTER or BEFORE the unfolding 
	protected boolean isSimpleComponentReference
	//
	protected final PropertyUtil propertyUtil = PropertyUtil.INSTANCE
	protected final extension StatechartUtil statechartUtil = StatechartUtil.INSTANCE
	protected final ExpressionSerializer expressionSerializer = ExpressionSerializer.INSTANCE
	protected final ExpressionModelFactory expressionFactory = ExpressionModelFactory.eINSTANCE
	protected final CompositeModelFactory compositeFactory = CompositeModelFactory.eINSTANCE
	protected final PropertyModelFactory factory = PropertyModelFactory.eINSTANCE
	protected final extension GammaEcoreUtil ecoreUtil = GammaEcoreUtil.INSTANCE

	new(boolean isSimpleComponentReference) {
		this.isSimpleComponentReference = isSimpleComponentReference
	}

	def PropertyPackage initializePackage(Component component) {
		val PropertyPackage propertyPackage = factory.createPropertyPackage
		val Package _package = StatechartModelDerivedFeatures.getContainingPackage(component)
		propertyPackage.getImport() += _package
		propertyPackage.setComponent(component)
		return propertyPackage
	}

	def List<CommentableStateFormula> createStateReachability(Collection<SynchronousComponentInstance> instances) {
		val List<CommentableStateFormula> formulas = newArrayList
		for (SynchronousComponentInstance instance : instances) {
			val Component type = instance.type
			if (type instanceof StatechartDefinition) {
				for (state : StatechartModelDerivedFeatures.getAllStates(type)) {
					val stateReference = factory.createComponentInstanceStateConfigurationReference
					val parentRegion = StatechartModelDerivedFeatures.getParentRegion(state)
					stateReference.setInstance(createInstanceReference(instance))
					stateReference.setRegion(parentRegion)
					stateReference.setState(state)
					val stateFormula = propertyUtil.createEF(propertyUtil.createAtomicFormula(stateReference))
					val commentableStateFormula = propertyUtil.
						createCommentableStateFormula('''«instance.name».«parentRegion.name».«state.name»''',
							stateFormula)
					formulas += commentableStateFormula
				}
			}
		}
		return formulas
	}

	def List<CommentableStateFormula> createOutEventReachability(Collection<Port> ports) {
		val List<CommentableStateFormula> formulas = newArrayList
		for (notNecessarilySimplePort : ports) {
			for (port : notNecessarilySimplePort.allConnectedSimplePorts) {
				val instance = port.containingComponentInstance
				for (outEvent : StatechartModelDerivedFeatures.getOutputEvents(port)) {
					val parameters = outEvent.parameterDeclarations
					if (parameters.empty) {
						val eventReference = propertyUtil.createEventReference(
							createInstanceReference(instance), port, outEvent)
						val stateFormula = propertyUtil.createEF(propertyUtil.createAtomicFormula(eventReference))
						val commentableStateFormula = propertyUtil.
							createCommentableStateFormula('''«instance.name».«port.name».«outEvent.name»''',
								stateFormula)
						formulas += commentableStateFormula
					}
					else {
						for (parameter : parameters) {
							val parameterValues = getValues(parameter)
							// Only bool and enum
							if (parameterValues.empty) {
								// E.g., integers - plain event
								val eventReference = propertyUtil.createEventReference(
									createInstanceReference(instance), port, outEvent)
								val stateFormula = propertyUtil.createEF(propertyUtil.createAtomicFormula(eventReference))
								val commentableStateFormula = propertyUtil.
									createCommentableStateFormula('''«instance.name».«port.name».«outEvent.name»''',
										stateFormula)
								formulas += commentableStateFormula
							}
							else {
								for (value : parameterValues) {
									val eventReference = propertyUtil.
										createEventReference(createInstanceReference(instance), port, outEvent)
									val parameterReference = propertyUtil.
										createParameterReference(createInstanceReference(instance), port, outEvent,
											parameter)
									val equalityExpression = expressionFactory.createEqualityExpression
									equalityExpression.setLeftOperand(parameterReference)
									equalityExpression.setRightOperand(value)
									val and = expressionFactory.createAndExpression
									and.operands += eventReference
									and.operands += equalityExpression
									val stateFormula = propertyUtil.createEF(propertyUtil.createAtomicFormula(and))
									val commentableStateFormula = propertyUtil.createCommentableStateFormula(
										'''«instance.name».«port.name».«outEvent.name».«parameter.name» == «expressionSerializer.serialize(value)»''',
										stateFormula)
									formulas += commentableStateFormula
								}
							}
						}
					}
				}
			}
		}
		return formulas
	}

	def protected Set<Expression> getValues(ParameterDeclaration parameter) {
		val typeDefinition = StatechartModelDerivedFeatures.getTypeDefinition(parameter.type)
		if (typeDefinition instanceof BooleanTypeDefinition) {
			return Set.of(expressionFactory.createTrueExpression, expressionFactory.createFalseExpression)
		}
		else if (typeDefinition instanceof EnumerationTypeDefinition) {
			val Set<Expression> literals = newHashSet
			for (literal : typeDefinition.literals) {
				val expression = expressionFactory.createEnumerationLiteralExpression
				expression.setReference(literal)
				literals += expression
			}
			return literals
		}
		return Collections.emptySet
	}

	def List<CommentableStateFormula> createTransitionReachability(TransitionAnnotations transitionAnnotations) {
		val List<CommentableStateFormula> formulas = newArrayList
		if (transitionAnnotations.empty) {
			return formulas
		}
		for (transition : transitionAnnotations.transitions) {
			val variable = transitionAnnotations.getVariable(transition)
			val reference = createVariableReference(variable)
			val stateFormula = propertyUtil.createEF(propertyUtil.createAtomicFormula(reference))
			// Comment
			val commentableStateFormula = propertyUtil.
				createCommentableStateFormula(getId(transition), stateFormula)
			formulas += commentableStateFormula
		}
		return formulas
	}

	def protected ComponentInstanceVariableReference createVariableReference(VariableDeclaration variable) {
		val statechart = StatechartModelDerivedFeatures.getContainingStatechart(variable)
		val instance = StatechartModelDerivedFeatures.getReferencingComponentInstance(statechart)
		val reference = propertyUtil.createVariableReference(
			createInstanceReference(instance), variable)
		return reference
	}

	def List<CommentableStateFormula> createTransitionPairReachability(
			List<TransitionPairAnnotation> transitionPairAnnotations) {
		val List<CommentableStateFormula> formulas = newArrayList
		if (transitionPairAnnotations.empty) {
			return formulas
		}
		for (transitionPairAnnotation : transitionPairAnnotations) {
			val incomingAnnotation = transitionPairAnnotation.incomingAnnotation
			val outgoingAnnotation = transitionPairAnnotation.outgoingAnnotation
			
			val firstTransition = incomingAnnotation.transition
			val secondTransition = outgoingAnnotation.transition
			val firstVariable = incomingAnnotation.transitionVariable
			val secondVariable = outgoingAnnotation.transitionVariable
			val firstId = incomingAnnotation.transitionId
			val secondId = outgoingAnnotation.transitionId
			
			// In-out transition pair
			val firstVariableReference = firstVariable.createVariableReference
			val secondVariableReference = secondVariable.createVariableReference
			val and = expressionFactory.createAndExpression => [
				it.operands += firstVariableReference.createEqualityExpression(firstId.toIntegerLiteral)
				it.operands += secondVariableReference.createEqualityExpression(secondId.toIntegerLiteral)
			]
			val stateFormula = propertyUtil.createEF(propertyUtil.createAtomicFormula(and))
			// Comment
			var String comment = '''«getId(firstTransition)» -p- «getId(secondTransition)»'''
			val commentableStateFormula = propertyUtil.createCommentableStateFormula(comment, stateFormula)
			formulas += commentableStateFormula
		}
		return formulas
	}

	def List<CommentableStateFormula> createInteractionReachability(InteractionAnnotations interactionAnnotations) {
		val List<CommentableStateFormula> formulas = newArrayList
		if (interactionAnnotations.empty) {
			return formulas
		}
		for (interaction : interactionAnnotations.uniqueInteractions) {
			val sender = interaction.sender
			val receiver = interaction.receiver
			val variablePair = interaction.variablePair
			val senderVariable = variablePair.first
			val senderId = interaction.senderId
			val receiverVariable = variablePair.second
			val receiverId = interaction.receiverId
			//
			val senderComment = sender.id
			var receiverComment = "<any>"
			var Expression finalExpression = null
			// Sender
			val senderReference = senderVariable.createVariableReference
			val senderLiteral = senderId.toIntegerLiteral
			val senderEqualityExpression = senderReference.createEqualityExpression(senderLiteral)
			finalExpression = senderEqualityExpression
			// Receiver
			if (variablePair.hasSecond) {
				val receiverReference = receiverVariable.createVariableReference
				val receiverLiteral = receiverId.toIntegerLiteral
				val receiverEqualityExpression = receiverReference.createEqualityExpression(receiverLiteral)
				finalExpression = expressionFactory.createAndExpression => [
					it.operands += senderEqualityExpression
					it.operands += receiverEqualityExpression
				]
				receiverComment = receiver.id
			}
			//
			val stateFormula = propertyUtil.createEF(propertyUtil.createAtomicFormula(finalExpression))
			// Comment
			val commentableStateFormula = propertyUtil.
				createCommentableStateFormula('''«senderComment» -i- «receiverComment»''', stateFormula)
			formulas += commentableStateFormula
		}
		return formulas
	}
	
	def List<CommentableStateFormula> createDataflowReachability(DefUseReferences defs,
			DefUseReferences uses, DataflowCoverageCriterion criterion) {
		val List<CommentableStateFormula> formulas = newArrayList
		if (defs.variables.nullOrEmpty || uses.variables.nullOrEmpty) {
			return formulas
		}
		for (variable : defs.variables) {
			// Def
			val defExpressions = newArrayList
			val auxiliaryDefReferences = defs.getAuxiliaryReferences(variable)
			val size = auxiliaryDefReferences.size
			for (var i = 0; i < size; i++) {
				val ponatedReference = auxiliaryDefReferences.get(i)
				val auxiliaryVariable = ponatedReference.defUseVariable
				val defExpression = expressionFactory.createAndExpression => [
					it.operands += auxiliaryVariable.createVariableReference
				]
				// Not necessary to negate the other defs as the annotated assignments ensure that only one is true
				// Def-comment
				val originalReference = ponatedReference.originalVariableReference
				val defComment = originalReference.id
				defExpressions += new Pair(defExpression, defComment)
				//
			}
			// Use
			for (defExpression : defExpressions) {
				val and = defExpression.key
				val defComment = defExpression.value
				val auxiliaryUseReferences = uses.getAuxiliaryReferences(variable)
				if (criterion == DataflowCoverageCriterion.ALL_DEF) {
					if (auxiliaryUseReferences.size > 1) {
						val or = expressionFactory.createOrExpression
						for (auxiliaryUseReference : auxiliaryUseReferences) {
							val auxiliaryUseVariable = auxiliaryUseReference.defUseVariable
							or.operands += auxiliaryUseVariable.createVariableReference
						}
						and.operands += or
					}
					else {
						val auxiliaryUseReference = auxiliaryUseReferences.head
						val auxiliaryUseVariable = auxiliaryUseReference.defUseVariable
						and.operands += auxiliaryUseVariable.createVariableReference
					}
					
					val originalUseReferences = auxiliaryUseReferences.map[it.originalVariableReference]
							.filter(Expression).toSet // Uses are almost DirectReferenceExpressions
					val useComment = originalUseReferences.ids
					val stateFormula = propertyUtil.createEF(propertyUtil.createAtomicFormula(and))
					formulas += propertyUtil.createCommentableStateFormula(
							'''«defComment» -d-u- «useComment»''', stateFormula)
				}
				else {
					for (auxiliaryUseReference : auxiliaryUseReferences) {
						val auxiliaryUseVariable = auxiliaryUseReference.defUseVariable
						val clonedAnd = and.clone
						clonedAnd.operands += auxiliaryUseVariable.createVariableReference
						
						val useComment = auxiliaryUseReference.originalVariableReference.id
						val stateFormula = propertyUtil.createEF(propertyUtil.createAtomicFormula(clonedAnd))
						formulas += propertyUtil.createCommentableStateFormula(
								'''«defComment» -d-u- «useComment»''', stateFormula)
					}
				}
			}
		}
		return formulas
	}
	
	def List<CommentableStateFormula> createInteractionDataflowReachability(
			List<Pair<DefUseReferences, DefUseReferences>> interactionDefUses, DataflowCoverageCriterion criterion) {
		val stateFormulas = newArrayList
		for (interactionDefUse : interactionDefUses) {
			val defs = interactionDefUse.key
			val uses = interactionDefUse.value
			stateFormulas += defs.createDataflowReachability(uses, criterion)
		}
		return stateFormulas
	}
	
	def protected ComponentInstanceReference createInstanceReference(ComponentInstance instance) {
		if (isSimpleComponentReference) {
			val reference = compositeFactory.createComponentInstanceReference
			reference.componentInstanceHierarchy += instance
			return reference
		} else {
			return statechartUtil.createInstanceReference(instance)
		}
	}

	// Comments
	def protected String getInstanceId(EObject object) {
		val statechart = StatechartModelDerivedFeatures.getContainingStatechart(object)
		try {
			val instance = StatechartModelDerivedFeatures.getReferencingComponentInstance(statechart)
			return instance.name
		} catch (IllegalArgumentException e) {
			return ""
		}
	}

	def dispatch protected String getId(RaiseEventAction action) {
		val transition = ecoreUtil.getContainerOfType(action, Transition)
		if (transition === null) {
			val state = ecoreUtil.getContainerOfType(action, State)
			if (state === null) {
				throw new IllegalArgumentException('''Not known raise event: «action»''')
			}
			val containmentFeatureName = action.eContainmentFeature.name
			return '''«getId(state)»-«containmentFeatureName»'''
		}
		return getId(transition)
	}

	def dispatch protected String getId(StateNode state) {
		return '''«getInstanceId(state)».«state.parentRegion.name».«state.name»'''
	}

	def dispatch protected String getId(Transition transition) {
		return '''«transition.sourceState.id» --> «transition.targetState.id»'''
	}
	
	def dispatch protected String getId(DirectReferenceExpression reference) {
		val transitionOrState = reference.containingTransitionOrState
		val variable = reference.declaration
		return '''«transitionOrState.id»::«variable.name»'''
	}
	
	def dispatch protected String getId(EventParameterReferenceExpression reference) {
		val transitionOrState = reference.containingTransitionOrState
		val port = reference.port
		val event = reference.event
		val parameter = reference.parameter
		return '''«transitionOrState.id»::«port.name».«event.name»::«parameter.name»'''
	}
	
	def protected String getIds(Collection<? extends Expression> references) {
		return '''«FOR reference : references SEPARATOR ' | '»«reference.id»«ENDFOR»'''
	}
	
}