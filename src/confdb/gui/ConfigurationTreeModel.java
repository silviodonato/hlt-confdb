package confdb.gui;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;

import java.util.ArrayList;

import confdb.gui.tree.AbstractTreeModel;

import confdb.data.*;


/**
 * ConfigurationTreeModel
 * ----------------------
 * @author Philipp Schieferdecker
 *
 * Display a configuration in a JTree structure.
 */
public class ConfigurationTreeModel extends AbstractTreeModel
{
    //
    // member data
    //

    /** root of the tree = configuration */
    private Configuration config = null;
    
    /** first level of nodes */
    private StringBuffer edsourcesNode = new StringBuffer();
    private StringBuffer essourcesNode = new StringBuffer();
    private StringBuffer servicesNode  = new StringBuffer();
    private StringBuffer pathsNode     = new StringBuffer();
    private StringBuffer modulesNode   = new StringBuffer();
    private StringBuffer sequencesNode = new StringBuffer();
    private ArrayList<StringBuffer> level1Nodes = new ArrayList<StringBuffer>();
    
    
    //
    // construction
    //

    /** standard constructor */
    public ConfigurationTreeModel(Configuration config)
    {
	this.config = config;
	updateLevel1Nodes();
	level1Nodes.add(edsourcesNode);
	level1Nodes.add(essourcesNode);
	level1Nodes.add(servicesNode);
	level1Nodes.add(pathsNode);
	level1Nodes.add(modulesNode);
	level1Nodes.add(sequencesNode);

	
    }


    //
    // member functions
    //

    /** get the EDSources root node */
    public StringBuffer edsourcesNode() { return edsourcesNode; }
    
    /** get the ESSources root node */
    public StringBuffer essourcesNode() { return essourcesNode; }
    
    /** get the EDSource root node */
    public StringBuffer servicesNode() { return servicesNode; }
    
    /** get the EDSource root node */
    public StringBuffer pathsNode() { return pathsNode; }
    
    /** get the EDSource root node */
    public StringBuffer modulesNode() { return modulesNode; }
    
    /** get the EDSource root node */
    public StringBuffer sequencesNode() { return sequencesNode; }
    
    /** set the configuration to be displayed */
    public void setConfiguration(Configuration config)
    {
	this.config = config;
	updateLevel1Nodes();
    }
    
    /** update information of level1 nodes */
    public void updateLevel1Nodes()
    {
	// EDSources node
	int edsourceCount = config.edsourceCount();
	int unsetEDSourceCount = config.unsetTrackedEDSourceParameterCount();
	edsourcesNode.delete(0,edsourcesNode.length());
	edsourcesNode.append("<html>EDSource (");
	edsourcesNode.append(edsourceCount);
	edsourcesNode.append(")");
	if (unsetEDSourceCount>0) {
	    edsourcesNode.append(" <font color=#ff0000>[");
	    edsourcesNode.append(unsetEDSourceCount);
	    edsourcesNode.append("]</font>");
	}
	edsourcesNode.append("</html>");
	
	// ESSource node
	int essourceCount = config.essourceCount();
	int unsetESSourceCount = config.unsetTrackedESSourceParameterCount();
	essourcesNode.delete(0,essourcesNode.length());
	essourcesNode.append("<html>ESSources (");
	essourcesNode.append(essourceCount);
	essourcesNode.append(")");
	if (unsetESSourceCount>0) {
	    essourcesNode.append(" <font color=#ff0000>[");
	    essourcesNode.append(unsetESSourceCount);
	    essourcesNode.append("]</font>");
	}
	essourcesNode.append("</html>");
	
	// Service node
	int serviceCount = config.serviceCount();
	int unsetServiceCount = config.unsetTrackedServiceParameterCount();
	servicesNode.delete(0,servicesNode.length());
	servicesNode.append("<html>Services (");
	servicesNode.append(serviceCount);
	servicesNode.append(")");
	if (unsetServiceCount>0) {
	    servicesNode.append(" <font color=#ff0000>[");
	    servicesNode.append(unsetServiceCount);
	    servicesNode.append("]</font>");
	}
	servicesNode.append("</html>");
	
	// Module node
	int moduleCount = config.moduleCount();
	int unsetModuleCount = config.unsetTrackedModuleParameterCount();
	modulesNode.delete(0,modulesNode.length());
	modulesNode.append("<html>Modules (");
	modulesNode.append(moduleCount);
	modulesNode.append(")");
	if (unsetModuleCount>0) {
	    modulesNode.append(" <font color=#ff0000>[");
	    modulesNode.append(unsetModuleCount);
	    modulesNode.append("]</font>");
	}
	modulesNode.append("</html>");
	
	// Paths node
	int pathCount = config.pathCount();
	pathsNode.delete(0,pathsNode.length());
	pathsNode.append("<html>Paths (");
	pathsNode.append(pathCount);
	pathsNode.append(")</html>");
	
	// Sequences node
	int sequenceCount = config.sequenceCount();
	sequencesNode.delete(0,sequencesNode.length());
	sequencesNode.append("<html>Sequences (");
	sequencesNode.append(sequenceCount);
	sequencesNode.append(")</html>");
    }
    
    /** get root directory */
    public Object getRoot() { return config; }

    /** indicate if a node is a leaf node */
    public boolean isLeaf(Object node)
    {
	if (node instanceof PSetParameter) {
	    PSetParameter pset = (PSetParameter)node;
	    return (pset.parameterCount()>0) ? false : true;
	}
	else if (node instanceof VPSetParameter) {
	    VPSetParameter vpset = (VPSetParameter)node;
	    return (vpset.parameterSetCount()>0) ? false : true;
	}
	return (node instanceof Parameter) ? true : false;
    }
    
    /** number of child nodes */
    public int getChildCount(Object node)
    {
	if (node.equals(config)) {
	    return level1Nodes.size();
	}
	else if (node instanceof StringBuffer) {
	    if (node.equals(edsourcesNode)) return config.edsourceCount();
	    if (node.equals(essourcesNode)) return config.essourceCount();
	    if (node.equals(servicesNode))  return config.serviceCount();
	    if (node.equals(pathsNode))     return config.pathCount();
	    if (node.equals(modulesNode))   return config.moduleCount();
	    if (node.equals(sequencesNode)) return config.sequenceCount();
	}
	else if (node instanceof Instance) {
	    Instance instance = (Instance)node;
	    return instance.parameterCount();
	}
	else if (node instanceof ReferenceContainer) {
	    ReferenceContainer refContainer = (ReferenceContainer)node;
	    return refContainer.entryCount();
	}
	else if (node instanceof ModuleReference) {
	    ModuleReference reference = (ModuleReference)node;
	    return reference.parameterCount();
	}
	else if (node instanceof PathReference) {
	    PathReference reference = (PathReference)node;
	    Path path = (Path)reference.parent();
	    return path.entryCount();
	}
	else if (node instanceof SequenceReference) {
	    SequenceReference reference = (SequenceReference)node;
	    Sequence sequence = (Sequence)reference.parent();
	    return sequence.entryCount();
	}
	else if (node instanceof PSetParameter) {
	    PSetParameter pset = (PSetParameter)node;
	    return pset.parameterCount();
	}
	else if (node instanceof VPSetParameter) {
	    VPSetParameter vpset = (VPSetParameter)node;
	    return vpset.parameterSetCount();
	}
	return 0;
    }
    
    /** get the i-th child node */
    public Object getChild(Object parent,int i)
    {
	if (parent.equals(config)) {
	    return level1Nodes.get(i);
	}
	else if (parent instanceof StringBuffer) {
	    if (parent.equals(edsourcesNode)) return config.edsource(i);
	    if (parent.equals(essourcesNode)) return config.essource(i);
	    if (parent.equals(servicesNode))  return config.service(i);
	    if (parent.equals(pathsNode))     return config.path(i);
	    if (parent.equals(modulesNode))   return config.module(i);
	    if (parent.equals(sequencesNode)) return config.sequence(i);
	}
	else if (parent instanceof Instance) {
	    Instance instance = (Instance)parent;
	    return instance.parameter(i);
	}
	else if (parent instanceof ReferenceContainer) {
	    ReferenceContainer refContainer = (ReferenceContainer)parent;
	    return refContainer.entry(i);
	}
	else if (parent instanceof ModuleReference) {
	    ModuleReference reference = (ModuleReference)parent;
	    return reference.parameter(i);
	}
	else if (parent instanceof PathReference) {
	    PathReference reference = (PathReference)parent;
	    Path path = (Path)reference.parent();
	    return path.entry(i);
	}
	else if (parent instanceof SequenceReference) {
	    SequenceReference reference = (SequenceReference)parent;
	    Sequence sequence = (Sequence)reference.parent();
	    return sequence.entry(i);
	}
	else if (parent instanceof PSetParameter) {
	    PSetParameter pset = (PSetParameter)parent;
	    return pset.parameter(i);
	}
	else if (parent instanceof VPSetParameter) {
	    VPSetParameter vpset = (VPSetParameter)parent;
	    return vpset.parameterSet(i);
	}
	
	return null;
    }
    
    /** get index of a certain child w.r.t. its parent dir */
    public int getIndexOfChild(Object parent,Object child)
    {
	if (parent.equals(config)) {
	    return level1Nodes.indexOf(child);
	}
	else if (parent instanceof StringBuffer) {
	    if (parent.equals(edsourcesNode)) {
		EDSourceInstance edsource = (EDSourceInstance)child;
		return config.indexOfEDSource(edsource);
	    }
	    if (parent.equals(essourcesNode)) {
		ESSourceInstance essource = (ESSourceInstance)child;
		return config.indexOfESSource(essource);
	    }
	    if (parent.equals(servicesNode)) {
		ServiceInstance service = (ServiceInstance)child;
		return config.indexOfService(service);
	    }
	    if (parent.equals(pathsNode)) {
		Path path = (Path)child;
		return config.indexOfPath(path);
	    }
	    if (parent.equals(modulesNode)) {
		ModuleInstance module = (ModuleInstance)child;
		return config.indexOfModule(module);
	    }
	    if (parent.equals(sequencesNode)) {
		Sequence sequence = (Sequence)child;
		return config.indexOfSequence(sequence);
	    }
	}
	else if (parent instanceof Instance) {
	    Instance instance = (Instance)parent;
	    Parameter parameter = (Parameter)child;
	    return instance.indexOfParameter(parameter);
	}
	else if (parent instanceof ReferenceContainer) {
	    ReferenceContainer refContainer = (ReferenceContainer)parent;
	    Reference reference = (Reference)child;
	    return refContainer.indexOfEntry(reference);
	}
	else if (parent instanceof ModuleReference) {
	    ModuleReference reference = (ModuleReference)parent;
	    Parameter parameter = (Parameter)child;
	    return reference.indexOfParameter(parameter);
	}
	else if (parent instanceof PathReference) {
	    PathReference pathreference = (PathReference)parent;
	    Path path = (Path)pathreference.parent();
	    Reference reference = (Reference)child;
	    return path.indexOfEntry(reference);
	}
	else if (parent instanceof SequenceReference) {
	    SequenceReference seqreference = (SequenceReference)parent;
	    Sequence sequence = (Sequence)seqreference.parent();
	    Reference reference = (Reference)child;
	    return sequence.indexOfEntry(reference);
	}
	else if (parent instanceof PSetParameter) {
	    PSetParameter pset = (PSetParameter)parent;
	    Parameter parameter = (Parameter)child;
	    return pset.indexOfParameter(parameter);
	}
	else if (parent instanceof VPSetParameter) {
	    VPSetParameter vpset = (VPSetParameter)parent;
	    PSetParameter pset = (PSetParameter)child;
	    return vpset.indexOfParameterSet(pset);
	}
	return -1;
    }
    

}
