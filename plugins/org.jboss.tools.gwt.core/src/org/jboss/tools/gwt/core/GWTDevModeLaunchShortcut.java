package org.jboss.tools.gwt.core;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationType;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.debug.ui.launchConfigurations.JavaApplicationLaunchShortcut;
import org.eclipse.jdt.internal.debug.ui.JDIDebugUIPlugin;
import org.eclipse.jdt.internal.debug.ui.launcher.LauncherMessages;
import org.eclipse.jdt.launching.IJavaLaunchConfigurationConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableContext;
import org.jboss.tools.common.EclipseUtil;

public class GWTDevModeLaunchShortcut extends JavaApplicationLaunchShortcut {

	public GWTDevModeLaunchShortcut() {
	}
	
	protected IType[] findTypes(Object[] elements, IRunnableContext context) throws InterruptedException, CoreException {
		for(Object element : elements){
			if(element instanceof IProject){
				IJavaProject javaProject = EclipseUtil.getJavaProject((IProject)element);
				if(javaProject != null){
					try {
						IType type = javaProject.findType("com.google.gwt.dev.DevMode");
						return new IType[]{type};
					} catch (JavaModelException e) {
						e.printStackTrace();
					}
				}
			}
		}
		return new IType[]{};
	}
	
	private String findModule(IProject project){
		for(IResource resource : EclipseUtil.getJavaSourceRoots(project)){
			IFile file = getFile((IContainer)resource);
			if(file != null){
				String name = file.getFullPath().makeRelativeTo(resource.getFullPath()).toString().replace("/", ".");
				
				int pos = name.indexOf(".gwt.xml");
				
				if(pos > 0){
					return name.substring(0, pos);
				}
			}
		}
		
		return "";
	}
	
	private IFile getFile(IContainer container) {
		if(container.isDerived()) {
			return null;
		}
		if(container.getName().startsWith(".")) //$NON-NLS-1$
			return null;
		
		try{
			for(IResource resource : container.members()){
				if(resource instanceof IFolder){
					IFile file = getFile((IFolder) resource);
					if(file != null)
						return file;
				}else if(resource instanceof IFile){
					if(resource.getName().endsWith(".gwt.xml")){
						return (IFile)resource;
					}
				}
			}
		}catch(CoreException ex){
			ex.printStackTrace();
		}
		return null;
	}
	
	@Override
	protected ILaunchConfiguration createConfiguration(IType type) {
		ILaunchConfiguration config = null;
		ILaunchConfigurationWorkingCopy wc = null;
		try {
			ILaunchConfigurationType configType = getConfigurationType();
			wc = configType.newInstance(null, DebugPlugin.getDefault().getLaunchManager().generateLaunchConfigurationName(type.getTypeQualifiedName('.')));
			wc.setAttribute(IJavaLaunchConfigurationConstants.ATTR_MAIN_TYPE_NAME, type.getFullyQualifiedName());
			wc.setAttribute(IJavaLaunchConfigurationConstants.ATTR_PROJECT_NAME, type.getJavaProject().getElementName());
			wc.setAttribute(IJavaLaunchConfigurationConstants.ATTR_VM_ARGUMENTS, "-Xmx256M");
			
			String moduleName = findModule(type.getJavaProject().getProject());
			
			wc.setAttribute(IJavaLaunchConfigurationConstants.ATTR_PROGRAM_ARGUMENTS, "-startupUrl index.html -war src/main/webapp"+" "+moduleName);
			
			wc.setMappedResources(new IResource[] {type.getUnderlyingResource()});
			config = wc.doSave();
		} catch (CoreException exception) {
			MessageDialog.openError(JDIDebugUIPlugin.getActiveWorkbenchShell(), LauncherMessages.JavaLaunchShortcut_3, exception.getStatus().getMessage());	
		} 
		return config;
	}
}
