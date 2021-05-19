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
package hu.bme.mit.gamma.expression.derivedfeatures;

import java.util.ArrayList;
import java.util.List;

import hu.bme.mit.gamma.expression.model.ArrayTypeDefinition;
import hu.bme.mit.gamma.expression.model.BooleanTypeDefinition;
import hu.bme.mit.gamma.expression.model.DecimalTypeDefinition;
import hu.bme.mit.gamma.expression.model.Declaration;
import hu.bme.mit.gamma.expression.model.EnumerationLiteralDefinition;
import hu.bme.mit.gamma.expression.model.EnumerationTypeDefinition;
import hu.bme.mit.gamma.expression.model.Expression;
import hu.bme.mit.gamma.expression.model.ExpressionModelFactory;
import hu.bme.mit.gamma.expression.model.FieldDeclaration;
import hu.bme.mit.gamma.expression.model.IntegerRangeLiteralExpression;
import hu.bme.mit.gamma.expression.model.IntegerTypeDefinition;
import hu.bme.mit.gamma.expression.model.ParameterDeclaration;
import hu.bme.mit.gamma.expression.model.ParametricElement;
import hu.bme.mit.gamma.expression.model.RationalTypeDefinition;
import hu.bme.mit.gamma.expression.model.RecordTypeDefinition;
import hu.bme.mit.gamma.expression.model.ReferenceExpression;
import hu.bme.mit.gamma.expression.model.ResetableVariableDeclarationAnnotation;
import hu.bme.mit.gamma.expression.model.TransientVariableDeclarationAnnotation;
import hu.bme.mit.gamma.expression.model.Type;
import hu.bme.mit.gamma.expression.model.TypeDeclaration;
import hu.bme.mit.gamma.expression.model.TypeDefinition;
import hu.bme.mit.gamma.expression.model.TypeReference;
import hu.bme.mit.gamma.expression.model.VariableDeclaration;
import hu.bme.mit.gamma.expression.util.ExpressionUtil;
import hu.bme.mit.gamma.util.GammaEcoreUtil;

public class ExpressionModelDerivedFeatures {
	
	protected static final ExpressionUtil expressionUtil = ExpressionUtil.INSTANCE;
	protected static final GammaEcoreUtil ecoreUtil = GammaEcoreUtil.INSTANCE;
	protected static final ExpressionModelFactory factory = ExpressionModelFactory.eINSTANCE;
	
	public static Expression getLeft(IntegerRangeLiteralExpression expression, boolean isInclusive) {
		Expression leftOperand = expression.getLeftOperand();
		boolean isLeftInclusive = expression.isLeftInclusive();
		if (isInclusive == isLeftInclusive) {
			return leftOperand;
		}
		if (isLeftInclusive) { // Literal is inclusive, but caller wants exclusive
			return expressionUtil.wrapIntoSubtract(ecoreUtil.clone(leftOperand), 1);
		}
		return expressionUtil.wrapIntoAdd(ecoreUtil.clone(leftOperand), 1); // Literal is exclusive, but caller wants inclusive
	}
	
	public static Expression getRight(IntegerRangeLiteralExpression expression, boolean isInclusive) {
		Expression rightOperand = expression.getRightOperand();
		boolean isRightInclusive = expression.isRightInclusive();
		if (isInclusive == isRightInclusive) {
			return rightOperand;
		}
		if (isRightInclusive) { // Literal is inclusive, but caller wants exclusive
			return expressionUtil.wrapIntoAdd(ecoreUtil.clone(rightOperand), 1);
		}
		return expressionUtil.wrapIntoSubtract(ecoreUtil.clone(rightOperand), 1); // Literal is exclusive, but caller wants inclusive
	}
	
	public static boolean isTransient(VariableDeclaration variable) {
		return variable.getAnnotations().stream()
				.anyMatch(it -> it instanceof TransientVariableDeclarationAnnotation);
	}
	
	public static boolean isResetable(VariableDeclaration variable) {
		return variable.getAnnotations().stream()
				.anyMatch(it -> it instanceof ResetableVariableDeclarationAnnotation);
	}
	
	// Types
	
	public static boolean isPrimitive(Type type) {
		TypeDefinition typeDefinition = getTypeDefinition(type);
		return typeDefinition instanceof BooleanTypeDefinition || typeDefinition instanceof IntegerTypeDefinition ||
				typeDefinition instanceof DecimalTypeDefinition || typeDefinition instanceof RationalTypeDefinition;
	}
	
	public static boolean isNative(Type type) {
		TypeDefinition typeDefinition = getTypeDefinition(type);
		return isPrimitive(typeDefinition) || typeDefinition instanceof EnumerationTypeDefinition;
	}
	
	public static boolean isArray(Type type) {
		TypeDefinition typeDefinition = getTypeDefinition(type);
		return typeDefinition instanceof ArrayTypeDefinition;
	}
	
	public static boolean isRecord(Type type) {
		TypeDefinition typeDefinition = getTypeDefinition(type);
		return typeDefinition instanceof RecordTypeDefinition;
	}
	
	public static boolean isComplex(Type type) {
		TypeDefinition typeDefinition = getTypeDefinition(type);
		return isRecord(typeDefinition) || isArray(typeDefinition);
	}
	
	public static TypeDefinition getTypeDefinition(Declaration declaration) {
		Type type = declaration.getType();
		return getTypeDefinition(type);
	}
	
	public static TypeDefinition getTypeDefinition(Type type) {
		if (type instanceof TypeDefinition) {
			return (TypeDefinition) type;
		}
		if (type instanceof TypeReference) {
			TypeReference typeReference = (TypeReference) type;
			return getTypeDefinition(typeReference.getReference().getType());
		}
		throw new IllegalArgumentException("Not known type: " + type);
	}
	
	public static TypeDeclaration getTypeDeclaration(Type type) {
		TypeDeclaration declaration = ecoreUtil.getContainerOfType(type, TypeDeclaration.class);
		if (declaration == null) {
			throw new IllegalArgumentException("No type declaration: " + type);
		}
		return declaration;
	}
	
	public static TypeDeclaration getTypeDeclaration(EnumerationLiteralDefinition literal) {
		TypeDeclaration declaration = ecoreUtil.getContainerOfType(literal, TypeDeclaration.class);
		if (declaration == null) {
			throw new IllegalArgumentException("No type declaration: " + literal);
		}
		return declaration;
	}
	
	// Type references
	
	public static boolean refersToAnAlias(TypeReference typeReference) {
		TypeDeclaration typeDeclaration = typeReference.getReference();
		Type type = typeDeclaration.getType();
		return type instanceof TypeReference;
	}
	
	public static TypeReference getFinalTypeReference(TypeReference typeReference) {
		if (refersToAnAlias(typeReference)) {
			TypeDeclaration typeDeclaration = typeReference.getReference();
			Type type = typeDeclaration.getType();
			TypeReference aliasReference = (TypeReference) type;
			return getFinalTypeReference(aliasReference);
		}
		return typeReference;
	}
	
	//
	
	public static Expression getDefaultExpression(Type type) {
		return expressionUtil.getInitialValueOfType(type);
	}
	
	public static int getIndex(ParameterDeclaration parameter) {
		ParametricElement container = (ParametricElement) parameter.eContainer();
		return container.getParameterDeclarations().indexOf(parameter);
	}
	
	public static boolean isEvaluable(Expression expression) {
		return ecoreUtil.getSelfAndAllContentsOfType(
				expression, ReferenceExpression.class).isEmpty();
	}
	
	public static List<TypeDeclaration> getSelfAndAllTypeDeclarations(Type type) {
		List<TypeDeclaration> typeDeclarations = getAllTypeDeclarations(type);
		if (type instanceof TypeDefinition) {
			TypeDeclaration typeDeclaration = ecoreUtil.getContainerOfType(type, TypeDeclaration.class);
			typeDeclarations.add(0, typeDeclaration);
		}
		return typeDeclarations;
	}
	
	public static List<TypeDeclaration> getAllTypeDeclarations(Type type) {
		List<TypeDeclaration> typeDeclarations = new ArrayList<TypeDeclaration>();
		if (type instanceof TypeReference) {
			TypeReference typeReference = (TypeReference) type;
			TypeDeclaration typeDeclaration = typeReference.getReference();
			typeDeclarations.add(typeDeclaration);
			Type typeDefinition = typeDeclaration.getType();
			if (typeDefinition instanceof RecordTypeDefinition) {
				RecordTypeDefinition subrecord = (RecordTypeDefinition) typeDefinition;
				for (FieldDeclaration field : subrecord.getFieldDeclarations()) {
					Type fieldType = field.getType();
					typeDeclarations.addAll(getAllTypeDeclarations(fieldType));
				}
			}
			else if (typeDefinition instanceof ArrayTypeDefinition) {
				ArrayTypeDefinition array = (ArrayTypeDefinition) typeDefinition;
				Type elementType = array.getElementType();
				typeDeclarations.addAll(getAllTypeDeclarations(elementType));
			}
		}
		return typeDeclarations;
	}
	
}