package org.jboss.tools.gwt.core;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationType;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.core.ILaunchDelegate;

public class GWTDevModeLaunchConfiguration implements ILaunchConfiguration {

	public GWTDevModeLaunchConfiguration() {
		// TODO Auto-generated constructor stub
	}

	public Object getAdapter(Class adapter) {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean contentsEqual(ILaunchConfiguration configuration) {
		// TODO Auto-generated method stub
		return false;
	}

	public ILaunchConfigurationWorkingCopy copy(String name) throws CoreException {
		// TODO Auto-generated method stub
		return null;
	}

	public void delete() throws CoreException {
		// TODO Auto-generated method stub

	}

	public boolean exists() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean getAttribute(String attributeName, boolean defaultValue) throws CoreException {
		// TODO Auto-generated method stub
		return false;
	}

	public int getAttribute(String attributeName, int defaultValue) throws CoreException {
		// TODO Auto-generated method stub
		return 0;
	}

	public List getAttribute(String attributeName, List defaultValue) throws CoreException {
		// TODO Auto-generated method stub
		return null;
	}

	public Set getAttribute(String attributeName, Set defaultValue) throws CoreException {
		// TODO Auto-generated method stub
		return null;
	}

	public Map getAttribute(String attributeName, Map defaultValue) throws CoreException {
		// TODO Auto-generated method stub
		return null;
	}

	public String getAttribute(String attributeName, String defaultValue) throws CoreException {
		// TODO Auto-generated method stub
		return null;
	}

	public Map getAttributes() throws CoreException {
		// TODO Auto-generated method stub
		return null;
	}

	public String getCategory() throws CoreException {
		// TODO Auto-generated method stub
		return null;
	}

	public IFile getFile() {
		// TODO Auto-generated method stub
		return null;
	}

	public IPath getLocation() {
		// TODO Auto-generated method stub
		return null;
	}

	public IResource[] getMappedResources() throws CoreException {
		// TODO Auto-generated method stub
		return null;
	}

	public String getMemento() throws CoreException {
		// TODO Auto-generated method stub
		return null;
	}

	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	public Set getModes() throws CoreException {
		// TODO Auto-generated method stub
		return null;
	}

	public ILaunchDelegate getPreferredDelegate(Set modes) throws CoreException {
		// TODO Auto-generated method stub
		return null;
	}

	public ILaunchConfigurationType getType() throws CoreException {
		// TODO Auto-generated method stub
		return null;
	}

	public ILaunchConfigurationWorkingCopy getWorkingCopy() throws CoreException {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean hasAttribute(String attributeName) throws CoreException {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isLocal() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isMigrationCandidate() throws CoreException {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isWorkingCopy() {
		// TODO Auto-generated method stub
		return false;
	}

	public ILaunch launch(String mode, IProgressMonitor monitor) throws CoreException {
		// TODO Auto-generated method stub
		return null;
	}

	public ILaunch launch(String mode, IProgressMonitor monitor, boolean build) throws CoreException {
		// TODO Auto-generated method stub
		return null;
	}

	public ILaunch launch(String mode, IProgressMonitor monitor, boolean build, boolean register) throws CoreException {
		// TODO Auto-generated method stub
		return null;
	}

	public void migrate() throws CoreException {
		// TODO Auto-generated method stub

	}

	public boolean supportsMode(String mode) throws CoreException {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isReadOnly() {
		// TODO Auto-generated method stub
		return false;
	}

}
