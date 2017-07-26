package com.company.aem.commons.core.utils;

import java.lang.reflect.Type;

import org.apache.sling.api.resource.Resource;

import com.day.cq.commons.inherit.HierarchyNodeInheritanceValueMap;
import com.day.cq.commons.inherit.InheritanceValueMap;

public class JCRHelper {
	
	private JCRHelper() {
		
	}

	public static Object getInheritedProperty (Resource resource, String propertyName, Type declaredType) {
        InheritanceValueMap iProperties = new HierarchyNodeInheritanceValueMap(resource);
        return iProperties.getInherited(propertyName, (Class<?>) declaredType);

    }
}
