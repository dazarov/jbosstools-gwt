package org.jboss.tools.gwt.core;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.debug.ui.ILaunchShortcut2;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.IEditorPart;
import org.jboss.tools.common.EclipseUtil;
import java.lang.reflect.Method;;

public class GWTDevModeLaunchShortcut implements ILaunchShortcut2{

	public void launch(ISelection selection, String mode) {
		if(mode.equals(ILaunchManager.RUN_MODE)){
			if(selection instanceof StructuredSelection){
				Object element = ((StructuredSelection) selection).getFirstElement();
				
				if(element instanceof IProject){
					IJavaProject javaProject = EclipseUtil.getJavaProject((IProject)element);
					if(javaProject != null){
						try {
							IType type = javaProject.findType("com.google.gwt.dev.DevMode");
							if(type != null){
								try {
									Method method = type.getClass().getMethod("main", new Class[]{String.class});
									if(method != null){
										
									}
								} catch (SecurityException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} catch (NoSuchMethodException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						} catch (JavaModelException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
	}

	public void launch(IEditorPart editor, String mode) {
	}

	public ILaunchConfiguration[] getLaunchConfigurations(ISelection selection) {
		return new GWTDevModeLaunchConfiguration[]{};
	}

	public ILaunchConfiguration[] getLaunchConfigurations(IEditorPart editorpart) {
		return new GWTDevModeLaunchConfiguration[]{};
	}

	public IResource getLaunchableResource(ISelection selection) {
		return null;
	}

	public IResource getLaunchableResource(IEditorPart editorpart) {
		return null;
	}

}
