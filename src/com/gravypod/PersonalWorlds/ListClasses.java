package com.gravypod.PersonalWorlds;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

public class ListClasses {
	
	/**
	 * Lists all of the classes in a package.
	 * 
	 * @param jarName
	 *            the name of the jar file. (Its file path)
	 * @param packageName
	 *            The package we want to list from.
	 * @return list of classes in the package.
	 * 
	 */
	public static List<String> getClasseNamesInPackage(final String jarName, String packageName) {
	
		final ArrayList<String> arrayList = new ArrayList<String>();
		
		packageName = packageName.replaceAll("\\.", "/");
		
		try {
			
			final JarInputStream jarFile = new JarInputStream(new FileInputStream(jarName));
			
			JarEntry jarEntry;
			while(true) {
				
				jarEntry = jarFile.getNextJarEntry();
				
				if (jarEntry == null) {
					break;
				}
				
				if (jarEntry.getName().startsWith(packageName) && jarEntry.getName().endsWith(".class") && !jarEntry.getName().contains("$")) {
					arrayList.add(jarEntry.getName().replaceAll("/", "\\."));
				}
				
			}
			
		} catch (final Exception e) {
			e.printStackTrace();
		}
		
		return arrayList;
	}
	
}
