package com.company.aem.commons.core.services;

import com.day.cq.replication.ReplicationActionType;
import com.company.aem.commons.core.services.exceptions.ServiceException;

public interface ReplicationService {

	public void replicate(String path, ReplicationActionType replicationActionType) throws ServiceException;
	
	public void replicateToOutbox(String path) throws ServiceException;


}
