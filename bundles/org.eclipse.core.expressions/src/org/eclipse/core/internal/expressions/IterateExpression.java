/*******************************************************************************
 * Copyright (c) 2000, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.core.internal.expressions;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.w3c.dom.Element;

import org.eclipse.core.expressions.EvaluationResult;
import org.eclipse.core.expressions.ExpressionInfo;
import org.eclipse.core.expressions.IEvaluationContext;
import org.eclipse.core.expressions.IIterable;

import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;

public class IterateExpression extends CompositeExpression {

	private static class IteratePool implements IEvaluationContext {

		private Iterator fIterator;
		private Object fDefaultVariable;
		private IEvaluationContext fParent;

		public IteratePool(IEvaluationContext parent, Iterator iterator) {
			Assert.isNotNull(parent);
			Assert.isNotNull(iterator);
			fParent= parent;
			fIterator= iterator;
		}
		public IEvaluationContext getParent() {
			return fParent;
		}
		public IEvaluationContext getRoot() {
			return fParent.getRoot();
		}
		public Object getDefaultVariable() {
			return fDefaultVariable;
		}
		public boolean getAllowPluginActivation() {
			return fParent.getAllowPluginActivation();
		}
		public void setAllowPluginActivation(boolean value) {
			fParent.setAllowPluginActivation(value);
		}
		public void addVariable(String name, Object value) {
			fParent.addVariable(name, value);
		}
		public Object removeVariable(String name) {
			return fParent.removeVariable(name);
		}
		public Object getVariable(String name) {
			return fParent.getVariable(name);
		}
		public Object resolveVariable(String name, Object[] args) throws CoreException {
			return fParent.resolveVariable(name, args);
		}
		public Object next() {
			fDefaultVariable= fIterator.next();
			return fDefaultVariable;
		}
		public boolean hasNext() {
			return fIterator.hasNext();
		}
	}

	private static final String ATT_OPERATOR= "operator"; //$NON-NLS-1$
	private static final String ATT_IF_EMPTY= "ifEmpty"; //$NON-NLS-1$
	private static final int OR= 1;
	private static final int AND= 2;

	/**
	 * The seed for the hash code for all iterate expressions.
	 */
	private static final int HASH_INITIAL= IterateExpression.class.getName().hashCode();

	private int fOperator;
	private Boolean fEmptyResult;

	public IterateExpression(IConfigurationElement configElement) throws CoreException {
		String opValue= configElement.getAttribute(ATT_OPERATOR);
		initializeOperatorValue(opValue);
		initializeEmptyResultValue(configElement.getAttribute(ATT_IF_EMPTY));
	}

	public IterateExpression(Element element) throws CoreException {
		String opValue= element.getAttribute(ATT_OPERATOR);
		initializeOperatorValue(opValue.length() > 0 ? opValue : null);
		String ifEmpty= element.getAttribute(ATT_IF_EMPTY);
		initializeEmptyResultValue(ifEmpty.length() > 0 ? ifEmpty : null);
	}

	public IterateExpression(String opValue) throws CoreException {
		initializeOperatorValue(opValue);
	}

	public IterateExpression(String opValue, String ifEmpty) throws CoreException {
		initializeOperatorValue(opValue);
		initializeEmptyResultValue(ifEmpty);
	}

	private void initializeOperatorValue(String opValue) throws CoreException {
		if (opValue == null) {
			fOperator= AND;
		} else {
			Expressions.checkAttribute(ATT_OPERATOR, opValue, new String[] {"and", "or"});  //$NON-NLS-1$//$NON-NLS-2$
			if ("and".equals(opValue)) { //$NON-NLS-1$
				fOperator= AND;
			} else {
				fOperator= OR;
			}
		}
	}

	private void initializeEmptyResultValue(String value) {
		if (value == null) {
			fEmptyResult= null;
		} else {
			fEmptyResult= Boolean.valueOf(value);
		}
	}

	/* (non-Javadoc)
	 * @see Expression#evaluate(IVariablePool)
	 */
	public EvaluationResult evaluate(IEvaluationContext context) throws CoreException {
		Object var= context.getDefaultVariable();
		if (var instanceof Collection) {
			Collection col= (Collection)var;
			switch (col.size()) {
				case 0:
					if (fEmptyResult == null) {
						return fOperator == AND ? EvaluationResult.TRUE : EvaluationResult.FALSE;
					} else {
						return fEmptyResult.booleanValue() ? EvaluationResult.TRUE : EvaluationResult.FALSE;
					}
				case 1:
					if (col instanceof List)
						return evaluateAnd(new DefaultVariable(context, ((List)col).get(0)));
					//$FALL-THROUGH$
				default:
					IteratePool iter= new IteratePool(context, col.iterator());
					EvaluationResult result= fOperator == AND ? EvaluationResult.TRUE : EvaluationResult.FALSE;
					while (iter.hasNext()) {
						iter.next();
						switch(fOperator) {
							case OR:
								result= result.or(evaluateAnd(iter));
								if (result == EvaluationResult.TRUE)
									return result;
								break;
							case AND:
								result= result.and(evaluateAnd(iter));
								if (result != EvaluationResult.TRUE)
									return result;
								break;
						}
					}
					return result;
			}
		} else {
			IIterable iterable= Expressions.getAsIIterable(var, this);
			if (iterable == null)
				return EvaluationResult.NOT_LOADED;
			int count= 0;
			IteratePool iter= new IteratePool(context, iterable.iterator());
			EvaluationResult result= fOperator == AND ? EvaluationResult.TRUE : EvaluationResult.FALSE;
			while (iter.hasNext()) {
				iter.next();
				count++;
				switch(fOperator) {
					case OR:
						result= result.or(evaluateAnd(iter));
						if (result == EvaluationResult.TRUE)
							return result;
						break;
					case AND:
						result= result.and(evaluateAnd(iter));
						if (result != EvaluationResult.TRUE)
							return result;
						break;
				}
			}
			if (count > 0) {
				return result;
			} else {
				if (fEmptyResult == null) {
					return fOperator == AND ? EvaluationResult.TRUE : EvaluationResult.FALSE;
				} else {
					return fEmptyResult.booleanValue() ? EvaluationResult.TRUE : EvaluationResult.FALSE;
				}
			}
		}
	}

	public void collectExpressionInfo(ExpressionInfo info) {
		// Although we access every single variable we only mark the default
		// variable as accessed since we don't have single variables for the
		// elements.
		info.markDefaultVariableAccessed();
		super.collectExpressionInfo(info);
	}

	public boolean equals(final Object object) {
		if (!(object instanceof IterateExpression))
			return false;

		final IterateExpression that= (IterateExpression)object;
		return (this.fOperator == that.fOperator) && equals(this.fExpressions, that.fExpressions);
	}

	protected int computeHashCode() {
		return HASH_INITIAL * HASH_FACTOR + hashCode(fExpressions)
			* HASH_FACTOR + fOperator;
	}
}
