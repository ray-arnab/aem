package com.company.aem.commons.core.models;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Optional;

@Model(adaptables=Resource.class)
public class GridRowModel {

    @Inject @Optional
    protected String xs;
    
    @Inject @Optional
    protected String sm;
    
    @Inject @Optional
    protected String md;
    
    @Inject @Optional
    protected String lg;
    
    @Inject
    protected int columns;
    
    private List<String> colClassStrings;

    @PostConstruct
    protected void init() {
    	
    	colClassStrings = new ArrayList<String>();
    	
    	String[] xsParts = null;
    	if(!StringUtils.isBlank(xs)) {
    		xsParts = xs.split(",");
		}
    	
    	String[] smParts = null;
    	if(!StringUtils.isBlank(sm)) {
    		smParts = sm.split(",");
		}
    	
    	String[] mdParts = null;
    	if(!StringUtils.isBlank(md)) {
    		mdParts = md.split(",");
		}
    	
    	String[] lgParts = null;
    	if(!StringUtils.isBlank(lg)) {
    		lgParts = lg.split(",");
		}
    
    	for(int i=0; i<columns; i++) {
    		StringBuffer colClass = new StringBuffer();
    		
    		if(xsParts != null) {
    			colClass.append(" col-xs-").append(xsParts[i % xsParts.length]);
    		}
    		
    		if(smParts != null) {
    			colClass.append(" col-sm-").append(smParts[i % smParts.length]);
    		}
    		
    		if(mdParts != null) {
    			colClass.append(" col-md-").append(mdParts[i % mdParts.length]);
    		}
    		
    		if(lgParts != null) {
    			colClass.append(" col-lg-").append(lgParts[i % lgParts.length]);
    		}
    		
    		colClassStrings.add(colClass.toString());
    		
    	}
    	
    }
    
    public List<String> getColClassStrings() {
    	return colClassStrings;
    }
    
//    public String getColClassString(int colNo) {
//        return colMap.get(colNo);
//    }
}
