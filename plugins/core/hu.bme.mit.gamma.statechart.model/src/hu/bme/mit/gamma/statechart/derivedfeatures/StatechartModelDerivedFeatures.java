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
package hu.bme.mit.gamma.statechart.derivedfeatures;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;

import hu.bme.mit.gamma.action.derivedfeatures.ActionModelDerivedFeatures;
import hu.bme.mit.gamma.action.model.Action;
import hu.bme.mit.gamma.expression.model.ArgumentedElement;
import hu.bme.mit.gamma.expression.model.ElseExpression;
import hu.bme.mit.gamma.expression.model.Expression;
import hu.bme.mit.gamma.expression.model.FunctionAccessExpression;
import hu.bme.mit.gamma.expression.model.FunctionDeclaration;
import hu.bme.mit.gamma.expression.model.ParameterDeclaration;
import hu.bme.mit.gamma.statechart.composite.AbstractSynchronousCompositeComponent;
import hu.bme.mit.gamma.statechart.composite.AsynchronousAdapter;
import hu.bme.mit.gamma.statechart.composite.AsynchronousComponent;
import hu.bme.mit.gamma.statechart.composite.AsynchronousComponentInstance;
import hu.bme.mit.gamma.statechart.composite.AsynchronousCompositeComponent;
import hu.bme.mit.gamma.statechart.composite.BroadcastChannel;
import hu.bme.mit.gamma.statechart.composite.CascadeCompositeComponent;
import hu.bme.mit.gamma.statechart.composite.Channel;
import hu.bme.mit.gamma.statechart.composite.ComponentInstance;
import hu.bme.mit.gamma.statechart.composite.CompositeComponent;
import hu.bme.mit.gamma.statechart.composite.InstancePortReference;
import hu.bme.mit.gamma.statechart.composite.PortBinding;
import hu.bme.mit.gamma.statechart.composite.SimpleChannel;
import hu.bme.mit.gamma.statechart.composite.SynchronousComponent;
import hu.bme.mit.gamma.statechart.composite.SynchronousComponentInstance;
import hu.bme.mit.gamma.statechart.contract.AdaptiveContractAnnotation;
import hu.bme.mit.gamma.statechart.interface_.Component;
import hu.bme.mit.gamma.statechart.interface_.Event;
import hu.bme.mit.gamma.statechart.interface_.EventDeclaration;
import hu.bme.mit.gamma.statechart.interface_.EventDirection;
import hu.bme.mit.gamma.statechart.interface_.EventReference;
import hu.bme.mit.gamma.statechart.interface_.EventSource;
import hu.bme.mit.gamma.statechart.interface_.Interface;
import hu.bme.mit.gamma.statechart.interface_.InterfaceRealization;
import hu.bme.mit.gamma.statechart.interface_.Package;
import hu.bme.mit.gamma.statechart.interface_.PackageAnnotation;
import hu.bme.mit.gamma.statechart.interface_.Port;
import hu.bme.mit.gamma.statechart.interface_.RealizationMode;
import hu.bme.mit.gamma.statechart.interface_.TimeSpecification;
import hu.bme.mit.gamma.statechart.interface_.TopComponentArgumentsAnnotation;
import hu.bme.mit.gamma.statechart.interface_.UnfoldedPackageAnnotation;
import hu.bme.mit.gamma.statechart.statechart.AnyPortEventReference;
import hu.bme.mit.gamma.statechart.statechart.ClockTickReference;
import hu.bme.mit.gamma.statechart.statechart.CompositeElement;
import hu.bme.mit.gamma.statechart.statechart.DeepHistoryState;
import hu.bme.mit.gamma.statechart.statechart.EntryState;
import hu.bme.mit.gamma.statechart.statechart.InitialState;
import hu.bme.mit.gamma.statechart.statechart.PortEventReference;
import hu.bme.mit.gamma.statechart.statechart.PseudoState;
import hu.bme.mit.gamma.statechart.statechart.RaiseEventAction;
import hu.bme.mit.gamma.statechart.statechart.Region;
import hu.bme.mit.gamma.statechart.statechart.SetTimeoutAction;
import hu.bme.mit.gamma.statechart.statechart.ShallowHistoryState;
import hu.bme.mit.gamma.statechart.statechart.State;
import hu.bme.mit.gamma.statechart.statechart.StateAnnotation;
import hu.bme.mit.gamma.statechart.statechart.StateNode;
import hu.bme.mit.gamma.statechart.statechart.StatechartAnnotation;
import hu.bme.mit.gamma.statechart.statechart.StatechartDefinition;
import hu.bme.mit.gamma.statechart.statechart.TimeoutDeclaration;
import hu.bme.mit.gamma.statechart.statechart.TimeoutEventReference;
import hu.bme.mit.gamma.statechart.statechart.Transition;
import hu.bme.mit.gamma.statechart.statechart.TransitionAnnotation;
import hu.bme.mit.gamma.statechart.statechart.TransitionIdAnnotation;
import hu.bme.mit.gamma.statechart.statechart.TransitionPriority;

public class StatechartModelDerivedFeatures extends ActionModelDerivedFeatures {
	
	public static List<ParameterDeclaration> getParameterDeclarations(ArgumentedElement element) {
		if (element instanceof RaiseEventAction) {
			RaiseEventAction raiseEventAction = (RaiseEventAction) element;
			Event event = raiseEventAction.getEvent();
			return event.getParameterDeclarations();
		}
		if (element instanceof ComponentInstance) {
			ComponentInstance instance = (ComponentInstance) element;
			return getDerivedType(instance).getParameterDeclarations();
		}
		if (element instanceof FunctionAccessExpression) {
			FunctionAccessExpression functionAccess = (FunctionAccessExpression) element;
			FunctionDeclaration functionDeclaration = (FunctionDeclaration)
					expressionUtil.getDeclaration(functionAccess.getOperand());
			return functionDeclaration.getParameterDeclarations();
		}
		throw new IllegalArgumentException("Not supported element: " + element);
	}

	public static boolean isBroadcast(InterfaceRealization interfaceRealization) {
		return isProvided(interfaceRealization) &&
			interfaceRealization.getInterface().getEvents().stream().allMatch(it -> it.getDirection() == EventDirection.OUT);
	}
	
	public static boolean isProvided(InterfaceRealization interfaceRealization) {
		return interfaceRealization.getRealizationMode() == RealizationMode.PROVIDED;
	}
	
	public static RealizationMode getOpposite(RealizationMode realizationMode) {
		switch (realizationMode) {
			case PROVIDED:
				return RealizationMode.REQUIRED;
			case REQUIRED:
				return RealizationMode.PROVIDED;
			default:
				throw new IllegalArgumentException("Not known realization mode: " + realizationMode);
		}
	}
	
	public static boolean isBroadcast(Port port) {
		return isBroadcast(port.getInterfaceRealization());
	}
	
	public static boolean isProvided(Port port) {
		return isProvided(port.getInterfaceRealization());
	}
	
	public static Interface getInterface(Port port) {
		return port.getInterfaceRealization().getInterface();
	}
	
	public static EventDirection getOpposite(EventDirection eventDirection) {
		switch (eventDirection) {
			case IN:
				return EventDirection.OUT;
			case OUT:
				return EventDirection.IN;
			default:
				throw new IllegalArgumentException("Not known event direction: " + eventDirection);
		}
	}
	
	public static List<Expression> getTopComponentArguments(Package unfoldedPackage) {
		List<Expression> topComponentArguments = new ArrayList<Expression>();
		for (PackageAnnotation annotation : unfoldedPackage.getAnnotations()) {
			if (annotation instanceof TopComponentArgumentsAnnotation) {
				TopComponentArgumentsAnnotation argumentsAnnotation =
						(TopComponentArgumentsAnnotation) annotation;
				topComponentArguments.addAll(argumentsAnnotation.getArguments());
				return topComponentArguments; // There must be only one annotation
			}
		}
		return topComponentArguments;
	}
	
	public static boolean isUnfolded(Package gammaPackage) {
		return gammaPackage.getAnnotations().stream().anyMatch(
				it -> it instanceof UnfoldedPackageAnnotation);
	}
	
	public static Set<Package> getSelfAndImports(Package gammaPackage) {
		Set<Package> imports = new HashSet<Package>();
		imports.add(gammaPackage);
		imports.addAll(gammaPackage.getImports());
		return imports;
	}
	
	public static Set<Package> getAllImports(Package gammaPackage) {
		Set<Package> imports = new HashSet<Package>();
		imports.addAll(gammaPackage.getImports());
		for (Component component : gammaPackage.getComponents()) {
			for (ComponentInstance componentInstance : getAllInstances(component)) {
				Component type = getDerivedType(componentInstance);
				Package referencedPackage = getContainingPackage(type);
				imports.add(referencedPackage);
				imports.addAll(referencedPackage.getImports());
			}
		}
		return imports;
	}
	
	public static Set<Component> getAllComponents(Package parentPackage) {
		Set<Component> types = new HashSet<Component>();
		for (Package importedPackage : parentPackage.getImports()) {
			for (Component importedComponent : importedPackage.getComponents()) {
				types.add(importedComponent);
			}
		}
		for (Component siblingComponent : parentPackage.getComponents()) {
			types.add(siblingComponent);
		}
		return types;
	}
	
	public static Set<SynchronousComponent> getAllSynchronousComponents(Package parentPackage) {
		Set<SynchronousComponent> types = new HashSet<SynchronousComponent>();
		for (Component component : getAllComponents(parentPackage)) {
			if (component instanceof SynchronousComponent) {
				types.add((SynchronousComponent) component);
			}
		}
		return types;
	}
	
	public static Set<AsynchronousComponent> getAllAsynchronousComponents(Package parentPackage) {
		Set<AsynchronousComponent> types = new HashSet<AsynchronousComponent>();
		for (Component component : getAllComponents(parentPackage)) {
			if (component instanceof AsynchronousComponent) {
				types.add((AsynchronousComponent) component);
			}
		}
		return types;
	}
	
	public static Set<StatechartDefinition> getAllStatechartComponents(Package parentPackage) {
		Set<StatechartDefinition> types = new HashSet<StatechartDefinition>();
		for (Component component : getAllSynchronousComponents(parentPackage)) {
			if (component instanceof StatechartDefinition) {
				types.add((StatechartDefinition) component);
			}
		}
		return types;
	}
	
	public static boolean areInterfacesEqual(Component lhs, Component rhs) {
		EList<Port> lhsPorts = lhs.getPorts();
		EList<Port> rhsPorts = rhs.getPorts();
		if (lhsPorts.size() != rhsPorts.size()) {
			return false;
		}
		for (Port lhsPort : lhsPorts) {
			if (!rhsPorts.stream().anyMatch(it -> ecoreUtil.helperEquals(lhsPort, it))) {
				return false;
			}
		}
		return true;
	}
	
	public static List<SynchronousComponentInstance> getAllSimpleInstances(Collection<? extends ComponentInstance> instances) {
		List<SynchronousComponentInstance> simpleInstances = new ArrayList<SynchronousComponentInstance>();
		for (ComponentInstance instance : instances) {
			simpleInstances.addAll(getAllSimpleInstances(instance));
		}
		return simpleInstances;
	}
	
	public static List<SynchronousComponentInstance> getAllSimpleInstances(ComponentInstance instance) {
		if (instance instanceof SynchronousComponentInstance) {
			SynchronousComponentInstance synchronousInstance = (SynchronousComponentInstance) instance;
			if (synchronousInstance.getType() instanceof StatechartDefinition) {
				// Atomic component
				return Collections.singletonList(synchronousInstance);
			}
			else {
				// Composite component
				return getAllSimpleInstances(synchronousInstance.getType());
			}
		}
		if (instance instanceof AsynchronousComponentInstance) {
			AsynchronousComponentInstance asynchronousInstance = (AsynchronousComponentInstance) instance;
			return getAllSimpleInstances(asynchronousInstance.getType());
		}
		throw new IllegalArgumentException("Not known instance type: " + instance);
	}
	
	public static List<SynchronousComponentInstance> getAllSimpleInstances(Component component) {
		List<SynchronousComponentInstance> simpleInstances = new ArrayList<SynchronousComponentInstance>();
		if (component instanceof AsynchronousCompositeComponent) {
			AsynchronousCompositeComponent asynchronousCompositeComponent = (AsynchronousCompositeComponent) component;
			for (AsynchronousComponentInstance instance : asynchronousCompositeComponent.getComponents()) {
				simpleInstances.addAll(getAllSimpleInstances(instance));
			}
		}
		else if (component instanceof AsynchronousAdapter) {
			AsynchronousAdapter asynchronousAdapter = (AsynchronousAdapter) component;
			simpleInstances.addAll(getAllSimpleInstances(asynchronousAdapter.getWrappedComponent()));
		}
		else if (component instanceof AbstractSynchronousCompositeComponent) {
			AbstractSynchronousCompositeComponent synchronousCompositeComponent = (AbstractSynchronousCompositeComponent) component;
			for (SynchronousComponentInstance instance : synchronousCompositeComponent.getComponents()) {
				simpleInstances.addAll(getAllSimpleInstances(instance));
			}
		}
		return simpleInstances;
	}
	
	public static List<AsynchronousComponentInstance> getAllAsynchronousSimpleInstances(Component component) {
		List<ComponentInstance> allInstances = getAllInstances(component);
		List<AsynchronousComponentInstance> asynchronousInstances = new ArrayList<AsynchronousComponentInstance>();
		for (ComponentInstance allInstance : allInstances) {
			if (getDerivedType(allInstance) instanceof AsynchronousAdapter) {
				asynchronousInstances.add((AsynchronousComponentInstance) allInstance);
			}
		}
		return asynchronousInstances;
	}
	
	public static List<ComponentInstance> getInstances(Component component) {
		List<ComponentInstance> instances = new ArrayList<ComponentInstance>();
		if (component instanceof AsynchronousCompositeComponent) {
			AsynchronousCompositeComponent asynchronousCompositeComponent = (AsynchronousCompositeComponent) component;
			for (AsynchronousComponentInstance instance : asynchronousCompositeComponent.getComponents()) {
				instances.add(instance);
			}
		}
		else if (component instanceof AsynchronousAdapter) {
			AsynchronousAdapter asynchronousAdapter = (AsynchronousAdapter) component;
			SynchronousComponentInstance wrappedComponent = asynchronousAdapter.getWrappedComponent();
			instances.add(wrappedComponent);
		}
		else if (component instanceof AbstractSynchronousCompositeComponent) {
			AbstractSynchronousCompositeComponent synchronousCompositeComponent = (AbstractSynchronousCompositeComponent) component;
			for (SynchronousComponentInstance instance : synchronousCompositeComponent.getComponents()) {
				instances.add(instance);
			}
		}
		return instances;
	}
	
	public static List<ComponentInstance> getAllInstances(Component component) {
		List<ComponentInstance> instances = new ArrayList<ComponentInstance>();
		if (component instanceof AsynchronousCompositeComponent) {
			AsynchronousCompositeComponent asynchronousCompositeComponent = (AsynchronousCompositeComponent) component;
			for (AsynchronousComponentInstance instance : asynchronousCompositeComponent.getComponents()) {
				instances.add(instance);
				AsynchronousComponent type = instance.getType();
				instances.addAll(getAllInstances(type));
			}
		}
		else if (component instanceof AsynchronousAdapter) {
			AsynchronousAdapter asynchronousAdapter = (AsynchronousAdapter) component;
			SynchronousComponentInstance wrappedComponent = asynchronousAdapter.getWrappedComponent();
			instances.add(wrappedComponent);
			instances.addAll(getAllInstances(wrappedComponent.getType()));
		}
		else if (component instanceof AbstractSynchronousCompositeComponent) {
			AbstractSynchronousCompositeComponent synchronousCompositeComponent = (AbstractSynchronousCompositeComponent) component;
			for (SynchronousComponentInstance instance : synchronousCompositeComponent.getComponents()) {
				instances.add(instance);
				SynchronousComponent type = instance.getType();
				instances.addAll(getAllInstances(type));
			}
		}
		return instances;
	}
	
	public static Collection<StatechartDefinition> getAllContainedStatecharts(SynchronousComponent component) {
		List<StatechartDefinition> statecharts = new ArrayList<StatechartDefinition>();
		for (SynchronousComponentInstance instance : getAllSimpleInstances(component)) {
			statecharts.add((StatechartDefinition) instance.getType());
		}
		return statecharts;
	}
	
	public static List<EventDeclaration> getAllEventDeclarations(Interface _interface) {
		List<EventDeclaration> eventDeclarations = new ArrayList<EventDeclaration>(_interface.getEvents());
		for (Interface parentInterface : _interface.getParents()) {
			eventDeclarations.addAll(getAllEventDeclarations(parentInterface));
		}
		return eventDeclarations;
	}
	
	public static List<Event> getAllEvents(Interface _interface) {
		return getAllEventDeclarations(_interface).stream().map(it -> it.getEvent()).collect(Collectors.toList());
	}
	
	public static EventDirection getDirection(Event event) {
		EventDeclaration eventDeclaration = ecoreUtil.getContainerOfType(event, EventDeclaration.class);
		return eventDeclaration.getDirection();
	}
	
	public static BigInteger getPriorityValue(Event event) {
		BigInteger priority = event.getPriority();
		if (priority == null) {
			return BigInteger.ZERO;
		}
		return priority;
	}
	
	public static List<EventDeclaration> getAllEventDeclarations(Port port) {
		return getAllEventDeclarations(port.getInterfaceRealization().getInterface());
	}
	
	public static List<Event> getAllEvents(Port port) {
		return getAllEvents(port.getInterfaceRealization().getInterface());
	}
	
	public static List<Event> getInputEvents(Port port) {
		List<Event> events = new ArrayList<Event>();
		InterfaceRealization interfaceRealization = port.getInterfaceRealization();
		Interface _interface = interfaceRealization.getInterface();
		final Collection<EventDeclaration> allEventDeclarations = getAllEventDeclarations(_interface);
		if (interfaceRealization.getRealizationMode() == RealizationMode.PROVIDED) {
			events.addAll(allEventDeclarations.stream()
					.filter(it -> it.getDirection() != EventDirection.OUT)
					.map(it -> it.getEvent())
					.collect(Collectors.toList()));
		}
		if (interfaceRealization.getRealizationMode() == RealizationMode.REQUIRED) {
			events.addAll(allEventDeclarations.stream()
					.filter(it -> it.getDirection() != EventDirection.IN)
					.map(it -> it.getEvent())
					.collect(Collectors.toList()));
		}
		return events;
	}
	
	public static List<Event> getOutputEvents(Port port) {
		List<Event> events = new ArrayList<Event>();
		InterfaceRealization interfaceRealization = port.getInterfaceRealization();
		Interface _interface = interfaceRealization.getInterface();
		final Collection<EventDeclaration> allEventDeclarations = getAllEventDeclarations(_interface);
		if (interfaceRealization.getRealizationMode() == RealizationMode.PROVIDED) {
			events.addAll(allEventDeclarations.stream()
					.filter(it -> it.getDirection() != EventDirection.IN)
					.map(it -> it.getEvent())
					.collect(Collectors.toList()));
		}
		if (interfaceRealization.getRealizationMode() == RealizationMode.REQUIRED) {
			events.addAll(allEventDeclarations.stream()
					.filter(it -> it.getDirection() != EventDirection.OUT)
					.map(it -> it.getEvent())
					.collect(Collectors.toList()));
		}
		return events;
	}
	
	public static boolean isInputEvent(Port port, Event event) {
		return getInputEvents(port).contains(event);
	}
	
	public static boolean isOutputEvent(Port port, Event event) {
		return getOutputEvents(port).contains(event);
	}
	
	public static Set<Interface> getInterfaces(Component component) {
		return getAllPorts(component).stream()
				.map(it -> getInterface(it)).collect(Collectors.toSet());
	}
	
	public static List<Port> getAllPorts(AsynchronousAdapter wrapper) {
		List<Port> allPorts = new ArrayList<Port>(wrapper.getPorts());
		allPorts.addAll(wrapper.getWrappedComponent().getType().getPorts());
		return allPorts;
	}
	
	public static List<Port> getAllPorts(Component component) {
		if (component instanceof AsynchronousAdapter) {
			return getAllPorts((AsynchronousAdapter)component);
		}		
		return component.getPorts();
	}
	
	public static Collection<PortBinding> getPortBindings(Port port) {
		EObject component = port.eContainer();
		List<PortBinding> portBindings = new ArrayList<PortBinding>();
		if (component instanceof CompositeComponent) {
			CompositeComponent compositeComponent = (CompositeComponent) component;
			for (PortBinding portBinding : compositeComponent.getPortBindings()) {
				if (portBinding.getCompositeSystemPort() == port) {
					portBindings.add(portBinding);
				}
			}
		}		
		return portBindings;
	}
	
	public static List<Port> getAllConnectedSimplePorts(Component component) {
		List<Port> simplePorts = new ArrayList<Port>();
		for (Port port : getAllPorts(component)) {
			simplePorts.addAll(getAllConnectedSimplePorts(port));
		}
		// Note that one port can be in the list multiple times iff the component is NOT unfolded
		return simplePorts;
	}
	
	public static List<Port> getAllConnectedSimplePorts(Port port) {
		List<Port> simplePorts = new ArrayList<Port>();
		Component component = getContainingComponent(port);
		if (component instanceof StatechartDefinition) {
			simplePorts.add(port);
		}
		else if (component instanceof CompositeComponent) {
			CompositeComponent composite = (CompositeComponent) component;
			for (PortBinding portBinding : composite.getPortBindings()) {
				if (portBinding.getCompositeSystemPort() == port) {
					// Makes sense only if the containment hierarchy is a tree structure
					simplePorts.addAll(getAllConnectedSimplePorts(portBinding.getInstancePortReference().getPort()));
				}
			}
		}
		// Note that one port can be in the list multiple times iff the component is NOT unfolded
		return simplePorts;
	}
	
	public static Port getConnectedTopComponentPort(Port port) {
		Package _package = getContainingPackage(port);
		List<PortBinding> portBindings = ecoreUtil.getAllContentsOfType(_package, PortBinding.class);
		for (PortBinding portBinding : portBindings) {
			if (portBinding.getInstancePortReference().getPort() == port) {
				Port systemPort = portBinding.getCompositeSystemPort();
				return getConnectedTopComponentPort(systemPort);
			}
		}
		return port;
	}
	
	public static boolean isInChannel(Port port) {
		Package _package = getContainingPackage(port);
		List<Channel> channels = ecoreUtil.getAllContentsOfType(_package, Channel.class);
		for (Channel channel : channels) {
			if (channel.getProvidedPort().getPort() == port ||
					getRequiredPorts(channel).stream().anyMatch(it -> it.getPort() == port)) {
				return true;
			}
		}
		List<PortBinding> portBindings = ecoreUtil.getAllContentsOfType(_package, PortBinding.class);
		for (PortBinding portBinding : portBindings) {
			if (portBinding.getInstancePortReference().getPort() == port) {
				Port systemPort = portBinding.getCompositeSystemPort();
				if (isInChannel(systemPort)) {
					return true;
				}
			}
		}
		return false;
	}
	
	public static List<InstancePortReference> getRequiredPorts(Channel channel) {
		if (channel instanceof SimpleChannel) {
			SimpleChannel simpleChannel = (SimpleChannel) channel;
			return Collections.singletonList(simpleChannel.getRequiredPort());
		}
		if (channel instanceof BroadcastChannel) {
			BroadcastChannel broadcastChannel = (BroadcastChannel) channel;
			return Collections.unmodifiableList(broadcastChannel.getRequiredPorts());
		}
		throw new IllegalArgumentException("Not known channel type: " + channel);
	}
	
	public static Set<Port> getUnusedPorts(ComponentInstance instance) {
		Component container = getContainingComponent(instance);
		Set<Port> usedPorts = ecoreUtil.getAllContentsOfType(container, InstancePortReference.class).stream()
				.filter(it -> it.getInstance() == instance).map(it -> it.getPort()).collect(Collectors.toSet());
		Set<Port> unusedPorts = new HashSet<Port>(getAllPorts(StatechartModelDerivedFeatures.getDerivedType(instance)));
		unusedPorts.removeAll(usedPorts);
		return unusedPorts;
	}
	
	public static EventSource getEventSource(EventReference eventReference) {
		if (eventReference instanceof PortEventReference) {
			return ((PortEventReference) eventReference).getPort();
		}
		if (eventReference instanceof AnyPortEventReference) {
			return ((AnyPortEventReference) eventReference).getPort();
		}
		if (eventReference instanceof ClockTickReference) {
			return ((ClockTickReference) eventReference).getClock();
		}
		if (eventReference instanceof TimeoutEventReference) {
			return ((TimeoutEventReference) eventReference).getTimeout();
		}
		throw new IllegalArgumentException("Not known type: " + eventReference);
	}
	
	public static Component getDerivedType(ComponentInstance instance) {
		if (instance instanceof SynchronousComponentInstance) {
			return ((SynchronousComponentInstance) instance).getType();
		}
		if (instance instanceof AsynchronousComponentInstance) {
			return ((AsynchronousComponentInstance) instance).getType();
		}
		throw new IllegalArgumentException("Not known type: " + instance);
	}
	
	public static EList<? extends ComponentInstance> getDerivedComponents(CompositeComponent composite) {
		if (composite instanceof AbstractSynchronousCompositeComponent) {
			return ((AbstractSynchronousCompositeComponent) composite).getComponents();
		}
		if (composite instanceof AsynchronousCompositeComponent) {
			return ((AsynchronousCompositeComponent) composite).getComponents();
		}
		throw new IllegalArgumentException("Not known type: " + composite);
	}
	
    public static boolean isSynchronous(Component component) {
    	return component instanceof SynchronousComponent;
    }
    
    public static boolean isAsynchronous(Component component) {
    	return component instanceof AsynchronousComponent;
    }
	
    public static boolean isCascade(ComponentInstance instance) {
    	if (getDerivedType(instance) instanceof StatechartDefinition) {
    		// Statecharts are cascade if contained by cascade composite components
    		return instance.eContainer() instanceof CascadeCompositeComponent;
   		}
   		return getDerivedType(instance) instanceof CascadeCompositeComponent;
    }
    
    public static boolean isSynchronous(ComponentInstance instance) {
    	return isSynchronous(getDerivedType(instance));
    }
    
    public static boolean isAsynchronous(ComponentInstance instance) {
    	return isAsynchronous(getDerivedType(instance));
    }
    
    public static boolean isStatechart(ComponentInstance instance) {
    	return getDerivedType(instance) instanceof StatechartDefinition;
    }
    
    public static boolean isAdapter(ComponentInstance instance) {
    	return getDerivedType(instance) instanceof AsynchronousAdapter;
    }
	
	public static int getLevel(StateNode stateNode) {
		if (isTopRegion(getParentRegion(stateNode))) {
			return 1;
		}
		else {
			return getLevel(getParentState(stateNode)) + 1;
		}
	}
	
	public static List<Transition> getOutgoingTransitions(StateNode node) {
		StatechartDefinition statechart = getContainingStatechart(node);
		return statechart.getTransitions().stream().filter(it -> it.getSourceState() == node).collect(Collectors.toList());
	}
	
	public static List<Transition> getIncomingTransitions(StateNode node) {
		StatechartDefinition statechart = getContainingStatechart(node);
		return statechart.getTransitions().stream().filter(it -> it.getTargetState() == node).collect(Collectors.toList());
	}
	
	public static Collection<StateNode> getAllStateNodes(CompositeElement compositeElement) {
		Set<StateNode> stateNodes = new HashSet<StateNode>();
		for (Region region : compositeElement.getRegions()) {
			for (StateNode stateNode : region.getStateNodes()) {
				stateNodes.add(stateNode);
				if (stateNode instanceof State) {
					State state = (State) stateNode;
					stateNodes.addAll(getAllStateNodes(state));
				}
			}
		}
		return stateNodes;
	}
	
	public static Collection<State> getAllStates(CompositeElement compositeElement) {
		Set<State> states = new HashSet<State>();
		for (StateNode stateNode : getAllStateNodes(compositeElement)) {
			if (stateNode instanceof State) {
				State state = (State) stateNode;
				states.add(state);
			}
		}
		return states;
	}
	
	public static List<State> getStates(Region region) {
		List<State> states = new ArrayList<State>();
		for (StateNode stateNode : region.getStateNodes()) {
			if (stateNode instanceof State) {
				State state = (State) stateNode;
				states.add(state);
			}
		}
		return states;
	}
	
	public static List<PseudoState> getPseudoStates(Region region) {
		List<PseudoState> pseudoStates = new ArrayList<PseudoState>();
		for (StateNode stateNode : region.getStateNodes()) {
			if (stateNode instanceof PseudoState) {
				PseudoState pseudoState = (PseudoState) stateNode;
				pseudoStates.add(pseudoState);
			}
		}
		return pseudoStates;
	}
	
	public static Collection<StateNode> getAllStateNodes(Region region) {
		List<StateNode> states = new ArrayList<StateNode>();
		TreeIterator<Object> allContents = EcoreUtil.getAllContents(region, true);
		while (allContents.hasNext()) {
			Object next = allContents.next();
			if (next instanceof StateNode) {
				states.add((StateNode) next);
			}
		}
		return states;
	}
	
	public static Collection<Region> getAllRegions(CompositeElement compositeElement) {
		Set<Region> regions = new HashSet<Region>(compositeElement.getRegions());
		for (State state : getAllStates(compositeElement)) {
			regions.addAll(getAllRegions(state));
		}
		return regions;
	}
	
	public static Collection<Region> getAllRegions(Region region) {
		Set<Region> regions = new HashSet<Region>();
		regions.add(region);
		TreeIterator<Object> allContents = EcoreUtil.getAllContents(region, true);
		while (allContents.hasNext()) {
			Object next = allContents.next();
			if (next instanceof Region) {
				regions.add((Region) next);
			}
		}
		return regions;
	}
	
	public static State getParentState(StateAnnotation annotation) {
		return (State) annotation.eContainer();
	}
	
	public static Region getParentRegion(StateNode node) {
		return (Region) node.eContainer();
	}
	
	public static boolean isTopRegion(Region region) {
		return getContainingCompositeElement(region) instanceof StatechartDefinition;
	}
	
	public static boolean isSubregion(Region region) {
		return !isTopRegion(region);
	}
	
	public static boolean isOrthogonal(Region region) {
		CompositeElement compositeElement = getContainingCompositeElement(region);
		return compositeElement.getRegions().size() >= 2;
	}
	
	public static CompositeElement getContainingCompositeElement(Region region) {
		return (CompositeElement) region.eContainer();
	}

	public static State getParentState(Region region) {
		if (isTopRegion(region)) {
			throw new IllegalArgumentException("This region has no parent state: " + region);
		}
		return (State) getContainingCompositeElement(region);
	}
	
	public static State getParentState(StateNode node) {
		Region parentRegion = getParentRegion(node);
		return getParentState(parentRegion);
	}
	
	public static Region getParentRegion(Region region) {
		if (isTopRegion(region)) {
			return null;
		}
		return getParentRegion((State) getContainingCompositeElement(region));
	}
	
	public static List<Region> getParentRegions(Region region) {
		if (isTopRegion(region)) {
			return new ArrayList<Region>();
		}
		Region parentRegion = getParentRegion(region);
		List<Region> parentRegions = new ArrayList<Region>();
		parentRegions.add(parentRegion);
		parentRegions.addAll(getParentRegions(parentRegion));
		return parentRegions;
	}
	
	public static List<Region> getSubregions(Region region) {
		List<Region> subregions = new ArrayList<Region>();
		for (List<Region> stateSubregions : getStates(region).stream().map(it -> it.getRegions())
				.collect(Collectors.toList())) {
			for (Region subregion : stateSubregions) {
				subregions.add(subregion);
				subregions.addAll(getSubregions(subregion));
			}
		}
		return subregions;
	}
	
	public static List<hu.bme.mit.gamma.statechart.statechart.State> getCommonAncestors(StateNode lhs, StateNode rhs) {
		List<hu.bme.mit.gamma.statechart.statechart.State> ancestors = getAncestors(lhs);
		ancestors.retainAll(getAncestors(rhs));
		return ancestors;
	}
	
	public static List<hu.bme.mit.gamma.statechart.statechart.State> getAncestors(StateNode node) {
		if (node.eContainer().eContainer() instanceof hu.bme.mit.gamma.statechart.statechart.State) {
			hu.bme.mit.gamma.statechart.statechart.State parentState = (hu.bme.mit.gamma.statechart.statechart.State) node.eContainer().eContainer();
			List<hu.bme.mit.gamma.statechart.statechart.State> ancestors = getAncestors(parentState);
			ancestors.add(parentState);
			return ancestors;
		}
		return new ArrayList<hu.bme.mit.gamma.statechart.statechart.State>();
	}
	
	public static List<hu.bme.mit.gamma.statechart.statechart.State> getAncestorsAndSelf(State node) {
		List<hu.bme.mit.gamma.statechart.statechart.State> ancestors = getAncestors(node);
		ancestors.add(node);
		return ancestors;
	}
	
	public static List<Region> getRegionAncestors(StateNode node) {
		if (node.eContainer().eContainer() instanceof hu.bme.mit.gamma.statechart.statechart.State) {
			hu.bme.mit.gamma.statechart.statechart.State parentState = (hu.bme.mit.gamma.statechart.statechart.State) node.eContainer().eContainer();
			List<Region> ancestors = getRegionAncestors(parentState);
			ancestors.add((Region) node.eContainer());
			return ancestors;
		}
		Region parentRegion = (Region) node.eContainer();
		List<Region> regionList = new ArrayList<Region>();
		regionList.add(parentRegion);
		return regionList;
	}
	
	public static List<Region> getCommonRegionAncestors(StateNode lhs, StateNode rhs) {
		List<Region> ancestors = getRegionAncestors(lhs);
		ancestors.retainAll(getRegionAncestors(rhs));
		return ancestors;
	}
	
	/**
	 * Returns whether the given region has deep history in one of its ancestor regions.
	 */
	private static boolean hasDeepHistoryAbove(Region region) {
		if (isTopRegion(region)) {
			return false;
		}
		Region parentRegion = getParentRegion(region);
		return parentRegion.getStateNodes().stream().anyMatch(it -> it instanceof DeepHistoryState) ||
			hasDeepHistoryAbove(parentRegion);
	}
	
	/**
	 * Returns whether the region has history or not.
	 */
	public static boolean hasHistory(Region region) {
		return hasDeepHistoryAbove(region) || 
			region.getStateNodes().stream().anyMatch(it -> it instanceof ShallowHistoryState) || 
			region.getStateNodes().stream().anyMatch(it -> it instanceof DeepHistoryState);
	}	
	
	public static String getFullContainmentHierarchy(State state) {
		if (state == null) {
			return "";
		}
		Region parentRegion = getParentRegion(state);
		State parentState = null;
		if (parentRegion.eContainer() instanceof State) {
			parentState = getParentState(parentRegion);
		}
		String parentRegionName = parentRegion.getName();
		if (parentState == null) {
			// Yakindu bug? First character is set to lowercase in the case of top regions
			parentRegionName = parentRegionName.substring(0, 1).toLowerCase() + parentRegionName.substring(1); // toFirstLowerCase
			return parentRegionName + "_" + state.getName();
		}
		return getFullContainmentHierarchy(parentState) + "_" + parentRegionName + "_" + state.getName();
	}
	
	public static String getFullRegionPathName(Region lowestRegion) {
		if (!(lowestRegion.eContainer() instanceof State)) {
			return lowestRegion.getName();
		}
		String fullParentRegionPathName = getFullRegionPathName(getParentRegion(lowestRegion));
		return fullParentRegionPathName + "." + lowestRegion.getName(); // Only regions are in path - states could be added too
	}
	
	public static StatechartDefinition getContainingStatechart(EObject object) {
		if (object.eContainer() instanceof StatechartDefinition) {
			return (StatechartDefinition) object.eContainer();
		}
		return getContainingStatechart(object.eContainer());
	}
	
	public static Component getContainingComponent(EObject object) {
		if (object.eContainer() == null) {
			throw new IllegalArgumentException("Not contained by a component: " + object);
		}
		if (object instanceof Component) {
			return (Component) object;
		}
		return getContainingComponent(object.eContainer());
	}
	
	public static Package getContainingPackage(EObject object) {
		if (object instanceof Package) {
			return (Package) object;
		}
//		if (object.eContainer() == null) {
//			throw new IllegalArgumentException("Not contained by a package: " + object);
//		}
		return getContainingPackage(object.eContainer());
	}
	
	public static Event getContainingEvent(ParameterDeclaration parameter) {
		return (Event) parameter.eContainer();
	}
	
	public static EventDeclaration getContainingEventDeclaration(Event event) {
		return (EventDeclaration) event.eContainer();
	}
	
	public static String getId(Transition transition) {
		for (TransitionAnnotation annotation : transition.getAnnotations()) {
			if (annotation instanceof TransitionIdAnnotation) {
				TransitionIdAnnotation transitionIdAnnotation = (TransitionIdAnnotation) annotation;
				return transitionIdAnnotation.getName();
			}
		}
		return null;
	}
	
	public static Collection<PortEventReference> getPortEventReferences(Transition transition) {
		return ecoreUtil.getAllContentsOfType(transition.getTrigger(),
				PortEventReference.class);
	}
	
	public static Collection<PortEventReference> getPortEventReferences(
			Collection<Transition> transitions) {
		Set<PortEventReference> portEventReferenes = new HashSet<PortEventReference>();
		for (Transition transition : transitions) {
			portEventReferenes.addAll(getPortEventReferences(transition));
		}
		return portEventReferenes;
	}
	
	public static Collection<Transition> getSelfAndPrecedingTransitions(Transition transition) {
		StateNode source = transition.getSourceState();
		Set<Transition> transitions = new HashSet<Transition>();
		transitions.add(transition);
		if (!(source instanceof State)) {
			for (Transition incomingTransition : getIncomingTransitions(source)) {
				transitions.addAll(getSelfAndPrecedingTransitions(incomingTransition));
			}
		}
		return transitions;
	}
	
	public static Collection<Transition> getPrioritizedTransitions(Transition gammaTransition) {
		StatechartDefinition gammaStatechart = getContainingStatechart(gammaTransition);
		TransitionPriority transitionPriority = gammaStatechart.getTransitionPriority();
		Collection<Transition> prioritizedTransitions = new ArrayList<Transition>();
		if (transitionPriority != TransitionPriority.OFF) {
			StateNode source = gammaTransition.getSourceState();
			List<Transition> gammaOutgoingTransitions = getOutgoingTransitions(source);
			for (Transition gammaOutgoingTransition : gammaOutgoingTransitions) {
				if (calculatePriority(gammaTransition).longValue() <
						calculatePriority(gammaOutgoingTransition).longValue()) {
					prioritizedTransitions.add(gammaOutgoingTransition);
				}
			}
		}
		return prioritizedTransitions;
	}
	
	public static BigInteger calculatePriority(Transition transition) {
		StatechartDefinition statechart = getContainingStatechart(transition);
		TransitionPriority transitionPriority = statechart.getTransitionPriority();
		switch (transitionPriority) {
			case ORDER_BASED : {
				StateNode source = transition.getSourceState();
				List<Transition> outgoingTransitions = getOutgoingTransitions(source);
				int size = outgoingTransitions.size();
				int index = outgoingTransitions.indexOf(transition);
				int priority = size - index;
				return BigInteger.valueOf(priority);
			}
			case VALUE_BASED : {
				return transition.getPriority();
			}
			default: {
				throw new IllegalArgumentException("Not supported literal: " + transitionPriority);
			}
		}
	}
	
	public static boolean isSameRegion(Transition transition) {
		return getParentRegion(transition.getSourceState()) == getParentRegion(transition.getTargetState());
	}
	
	public static boolean isToHigher(Transition transition) {
		return isToHigher(transition.getSourceState(), transition.getTargetState());
	}
	
	public static boolean isToHigher(StateNode source, StateNode target) {
		Region sourceParentRegion = getParentRegion(source);
		if (isTopRegion(sourceParentRegion)) {
			return false;
		}
		State sourceParentState = getParentState(source);
		if (getParentRegion(sourceParentState) == getParentRegion(target)) {
			return true;
		}
		return isToHigher(sourceParentState, target);
	}
	
	public static boolean isToLower(Transition transition) {
		return isToLower(transition.getSourceState(), transition.getTargetState());
	}
	
	public static boolean isToLower(StateNode source, StateNode target) {
		Region targetParentRegion = getParentRegion(target);
		if (isTopRegion(targetParentRegion)) {
			return false;
		}
		State targetParentState = getParentState(target);
		if (getParentRegion(source) == getParentRegion(targetParentState)) {
			return true;
		}
		return isToLower(source, targetParentState);
	}
	
	public static boolean isToHigherAndLower(Transition transition) {
		return isToHigherAndLower(transition.getSourceState(), transition.getTargetState());
	}
	
	public static boolean isToHigherAndLower(StateNode source, StateNode target) {
		List<Region> sourceAncestors = getRegionAncestors(source);
		List<Region> targetAncestors = getRegionAncestors(target);
		List<Region> commonAncestors = new ArrayList<Region>(sourceAncestors);
		commonAncestors.retainAll(targetAncestors);
		if (commonAncestors.isEmpty()) {
			// Top region orthogonal invalid transitions
			return false;
		}
		sourceAncestors.removeAll(commonAncestors);
		if (sourceAncestors.isEmpty()) {
			// To lower level
			return false;
		}
		targetAncestors.removeAll(commonAncestors);
		if (targetAncestors.isEmpty()) {
			// To higher level
			return false;
		}
		return true;
	}
	
	public static boolean hasTrigger(Transition transition) {
		return transition.getTrigger() != null;
	}
	
	public static boolean hasGuard(Transition transition) {
		return transition.getGuard() != null;
	}
	
	public static boolean isEmpty(Transition transition) {
		return !hasTrigger(transition) && !hasGuard(transition) &&
			transition.getEffects().isEmpty();
	}
	
	public static boolean isElse(Transition transition) {
		return transition.getGuard() instanceof ElseExpression;
	}
	
	public static boolean isLoop(Transition transition) {
		return transition.getSourceState() == transition.getTargetState();
	}
	
	public static StateNode getSourceAncestor(Transition transition) {
		return getSourceAncestor(transition.getSourceState(), transition.getTargetState());
	}
	
	public static StateNode getSourceAncestor(StateNode source, StateNode target) {
		if (isToLower(source, target)) {
			return source;
		}
		Region sourceParentRegion = getParentRegion(source);
		if (isTopRegion(sourceParentRegion)) {
			throw new IllegalArgumentException("No source ancestor!");
		}
		State sourceParentState = getParentState(source);
		return getSourceAncestor(sourceParentState, target);
	}
	
	public static StateNode getTargetAncestor(Transition transition) {
		return getTargetAncestor(transition.getSourceState(), transition.getTargetState());
	}
	
	public static StateNode getTargetAncestor(StateNode source, StateNode target) {
		if (isToHigher(source, target)) {
			return source;
		}
		Region targetParentRegion = getParentRegion(target);
		if (isTopRegion(targetParentRegion)) {
			throw new IllegalArgumentException("No target ancestor!");
		}
		State targetParentState = getParentState(target);
		return getTargetAncestor(source, targetParentState);
	}
	
	public static boolean isComposite(StateNode node) {
		if (node instanceof State) {
			return isComposite((State) node);
		}
		return false;
	}
	
	public static boolean isComposite(State state) {
		return !state.getRegions().isEmpty();
	}
	
	public static EObject getContainingTransitionOrState(EObject object) {
		Transition containingTransition = ecoreUtil.getContainerOfType(
				object, Transition.class);
		if (containingTransition != null) {
			// Container is a transition
			return containingTransition;
		}
		// Container is a state
		return ecoreUtil.getContainerOfType(object, State.class);
	}
	
	public static StateNode getContainingOrSourceStateNode(EObject object) {
		EObject container = getContainingTransitionOrState(object);
		if (container instanceof Transition) {
			Transition transition = (Transition) container;
			return transition.getSourceState();
		}
		return (StateNode) container;
	}
	
	public static List<Action> getContainingActionList(EObject object) {
		EObject container = object.eContainer();
		if (container instanceof Transition) {
			Transition transition = (Transition) container;
			return transition.getEffects();
		}
		if (container instanceof State) {
			State state = (State) container;
			if (state.getEntryActions().contains(object)) {
				return state.getEntryActions();
			}
			if (state.getExitActions().contains(object)) {
				return state.getExitActions();
			}
		}
		// Nullptr if the object is not contained by any of the above
		return getContainingActionList(container);
	}
	
	public static int getLiteralIndex(State state) {
		Region parent = getParentRegion(state);
		List<State> states = getStates(parent);
		return states.indexOf(state) + 1 /* + 1 for __Inactive */;
	}
	
	public static EntryState getEntryState(Region region) {
		Collection<StateNode> entryStates = region.getStateNodes().stream()
				.filter(it -> it instanceof EntryState)
				.collect(Collectors.toList());
		Optional<StateNode> entryState = entryStates.stream().filter(it -> it instanceof InitialState).findFirst();
		if (entryState.isPresent()) {
			return (EntryState) entryState.get();
		}
		entryState = entryStates.stream().filter(it -> it instanceof DeepHistoryState).findFirst();
		if (entryState.isPresent()) {
			return (EntryState) entryState.get();
		}
		entryState = entryStates.stream().filter(it -> it instanceof ShallowHistoryState).findFirst();
		if (entryState.isPresent()) {
			return (EntryState) entryState.get();
		}
		throw new IllegalArgumentException("Not known initial states in the region. " + region.getName() + ": " + entryStates);
	}
	
	public static Transition getInitialTransition(Region region) {
		EntryState entryState = getEntryState(region);
		List<Transition> outgoingTransitions = getOutgoingTransitions(entryState);
		if (outgoingTransitions.size() != 1) {
			throw new IllegalArgumentException(outgoingTransitions.toString());
		}
		return outgoingTransitions.get(0);
	}
	
	public static Set<State> getPrecedingStates(StateNode node) {
		Set<State> precedingStates = new HashSet<State>();
		for (Transition incomingTransition : getIncomingTransitions(node)) {
			StateNode source = incomingTransition.getSourceState();
			if (source instanceof State) {
				precedingStates.add((State) source);
			}
			else {
				precedingStates.addAll(getReachableStates(source));
			}
		}
		return precedingStates;
	}
	
	public static Set<State> getReachableStates(StateNode node) {
		Set<State> reachableStates = new HashSet<State>();
		for (Transition outgoingTransition : getOutgoingTransitions(node)) {
			StateNode target = outgoingTransition.getTargetState();
			if (target instanceof State) {
				reachableStates.add((State) target);
			}
			else {
				reachableStates.addAll(getReachableStates(target));
			}
		}
		return reachableStates;
	}
	
	public static TimeSpecification getTimeoutValue(TimeoutDeclaration timeout) {
		StatechartDefinition statechart = getContainingStatechart(timeout);
		TimeSpecification time = null;
		TreeIterator<Object> contents = EcoreUtil.getAllContents(statechart, true);
		while (contents.hasNext()) {
			Object it = contents.next();
			if (it instanceof SetTimeoutAction) {
				SetTimeoutAction action = (SetTimeoutAction) it;
				if (action.getTimeoutDeclaration() == timeout) {
					if (time == null) {
						time = action.getTime();
					}
					else {
						throw new IllegalStateException("This timeout is assigned a value more than once: " + timeout);
					}
				}
			}
		}
		return time;
	}
	
	public static Component getMonitoredComponent(StatechartDefinition adaptiveContract) {
		StatechartAnnotation annotation = adaptiveContract.getAnnotation();
		if (annotation instanceof AdaptiveContractAnnotation) {
			AdaptiveContractAnnotation adaptiveContractAnnotation = (AdaptiveContractAnnotation) annotation;
			return adaptiveContractAnnotation.getMonitoredComponent();
		}
		throw new IllegalArgumentException("Not an adaptive contract statechart: " + adaptiveContract);
	}
	
	public static Collection<ComponentInstance> getReferencingComponentInstances(Component component) {
		Package _package = getContainingPackage(component);
		Collection<ComponentInstance> componentInstances = new HashSet<ComponentInstance>();
		for (Component siblingComponent : _package.getComponents()) {
			if (siblingComponent instanceof CompositeComponent) {
				CompositeComponent compositeComponent = (CompositeComponent) siblingComponent;
				for (ComponentInstance componentInstance : getDerivedComponents(compositeComponent)) {
					if (getDerivedType(componentInstance) == component) {
						componentInstances.add(componentInstance);
					}
				}
			}
			if (siblingComponent instanceof AsynchronousAdapter) {
				AsynchronousAdapter asynchronousAdapter = (AsynchronousAdapter) siblingComponent;
				SynchronousComponentInstance componentInstance = asynchronousAdapter.getWrappedComponent();
				if (componentInstance.getType() == component) {
					componentInstances.add(componentInstance);
				}
			}
		}
		return componentInstances;
	}
	
	public static ComponentInstance getReferencingComponentInstance(Component component) {
		Collection<ComponentInstance> instances = getReferencingComponentInstances(component);
		if (instances.size() != 1) {
			throw new IllegalArgumentException("Not one referencing instance: " + instances);
		}
		return instances.stream().findFirst().get();
	}
	
	public static ComponentInstance getContainingComponentInstance(EObject object) {
		StatechartDefinition statechart = getContainingStatechart(object);
		return getReferencingComponentInstance(statechart);
	}
	
	public static List<ComponentInstance> getParentComponentInstances(ComponentInstance instance) {
		Component container = ecoreUtil.getContainerOfType(instance, Component.class);
		try {
			ComponentInstance referencingComponentInstance = getReferencingComponentInstance(container);
			List<ComponentInstance> parentComponentInstances = getParentComponentInstances(referencingComponentInstance);
			parentComponentInstances.add(referencingComponentInstance);
			return parentComponentInstances;
		} catch (IllegalArgumentException e) {
			// Top component
			return new ArrayList<ComponentInstance>();
		}
	}
	
	public static List<ComponentInstance> getComponentInstanceChain(ComponentInstance instance) {
		List<ComponentInstance> parentComponentInstances = getParentComponentInstances(instance);
		parentComponentInstances.add(instance);
		return parentComponentInstances;
	}
	
	public static List<SynchronousComponentInstance> getScheduledInstances(AbstractSynchronousCompositeComponent component) {
		if (component instanceof CascadeCompositeComponent) {
			CascadeCompositeComponent cascade = (CascadeCompositeComponent) component;
			if (!cascade.getExecutionList().isEmpty()) {
				return cascade.getExecutionList();
			}
		}
		return component.getComponents();
	}
	
}