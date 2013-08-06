package org.jboss.tools.gwt.core;

import java.util.ArrayList;

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
import org.eclipse.jdt.launching.IRuntimeClasspathEntry;
import org.eclipse.jdt.launching.IRuntimeClasspathProvider;
import org.eclipse.jdt.launching.JavaRuntime;
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
				IType type = findType(javaProject);
				if(type != null){
					return new IType[]{type};
				}
			}else if(element instanceof IJavaProject){
				IType type = findType((IJavaProject)element);
				if(type != null){
					return new IType[]{type};
				}
				
			}
		}
		return new IType[]{};
	}
	
	private IType findType(IJavaProject javaProject){
		if(javaProject != null){
			try {
				return javaProject.findType("com.google.gwt.dev.DevMode");
			} catch (JavaModelException e) {
				e.printStackTrace();
			}
		}
		return null;
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
		IJavaProject javaProject = type.getJavaProject();
		IProject project = javaProject.getProject();
		
		String moduleName = findModule(project);
		
		ILaunchConfiguration config = null;
		ILaunchConfigurationWorkingCopy wc = null;
		try {
			ILaunchConfigurationType configType = getConfigurationType();
			wc = configType.newInstance(null, DebugPlugin.getDefault().getLaunchManager().generateLaunchConfigurationName(type.getTypeQualifiedName('.'))+" "+project.getName()+" "+moduleName);
			
			IRuntimeClasspathProvider provider = JavaRuntime.getClasspathProvider(wc);
			
			ArrayList<String> paths = new ArrayList<String>();
			IRuntimeClasspathEntry[] entries = provider.computeUnresolvedClasspath(wc);
			for(IRuntimeClasspathEntry entry : entries){
				paths.add(entry.getMemento());
			}
			
			// add source folder to the class path
			for(IResource resource : EclipseUtil.getJavaSourceRoots(project)){
				IRuntimeClasspathEntry entry = JavaRuntime.newArchiveRuntimeClasspathEntry(resource.getFullPath());
				paths.add(entry.getMemento());
			}

			// add default project class path
			paths.add(JavaRuntime.newDefaultProjectClasspathEntry(javaProject).getMemento());
			
			paths.add(JavaRuntime.newArchiveRuntimeClasspathEntry(type.getPath()).getMemento());
			
			wc.setAttribute(IJavaLaunchConfigurationConstants.ATTR_DEFAULT_CLASSPATH, false);
			
			wc.setAttribute(IJavaLaunchConfigurationConstants.ATTR_CLASSPATH, paths);
			
			wc.setAttribute(IJavaLaunchConfigurationConstants.ATTR_MAIN_TYPE_NAME, type.getFullyQualifiedName());
			wc.setAttribute(IJavaLaunchConfigurationConstants.ATTR_PROJECT_NAME, type.getJavaProject().getElementName());
			wc.setAttribute(IJavaLaunchConfigurationConstants.ATTR_VM_ARGUMENTS, "-Xmx256M");
			
			wc.setAttribute(IJavaLaunchConfigurationConstants.ATTR_PROGRAM_ARGUMENTS, 
					"-startupUrl index.html -war src/main/webapp -codeServerPort 9997 -port 8888 -remoteUI \"${gwt_remote_ui_server_port}:${unique_id}\" "
					+moduleName);
			
			wc.setMappedResources(new IResource[] {type.getUnderlyingResource()});
			config = wc.doSave();
		} catch (CoreException exception) {
			MessageDialog.openError(JDIDebugUIPlugin.getActiveWorkbenchShell(), LauncherMessages.JavaLaunchShortcut_3, exception.getStatus().getMessage());
			exception.printStackTrace();
		} 
		return config;
	}
}
