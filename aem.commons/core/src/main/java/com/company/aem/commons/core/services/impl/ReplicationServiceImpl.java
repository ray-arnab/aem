package com.company.aem.commons.core.services.impl;

import javax.jcr.Session;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.cq.replication.Agent;
import com.day.cq.replication.AgentFilter;
import com.day.cq.replication.ReplicationActionType;
import com.day.cq.replication.ReplicationOptions;
import com.day.cq.replication.Replicator;
import com.company.aem.commons.core.services.ReplicationService;
import com.company.aem.commons.core.services.exceptions.ServiceException;
import com.company.aem.commons.core.utils.ResolverFactoryHelper;


@Component(immediate = true, metatype = false)
@Service
public class ReplicationServiceImpl implements ReplicationService {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(ReplicationServiceImpl.class);
	
	private static final AgentFilter DISTRIBUTE_AGENT_FILTER = new AgentFilter() {
		public boolean isIncluded(Agent agent) {
			return agent.getConfiguration().isTriggeredOnDistribute();
		}
	};
	
	@Reference
	private ResourceResolverFactory resolverFactory;
	
	@Reference
    private Replicator replicator;

	@Override
	public void replicate(String path, ReplicationActionType replicationActionType) throws ServiceException {
		Session session = null;
		ResourceResolver resourceResolver = null;
		try {
			resourceResolver = ResolverFactoryHelper.getWriterResourceResolver(resolverFactory);
			LOGGER.debug("Created resourceResolver from factory");
			
			session = resourceResolver.adaptTo(Session.class);
			LOGGER.debug("Session created successfully");
			
			replicator.replicate(session, replicationActionType, path);
			LOGGER.debug("Replicated successfully : "+ path);
		} catch (Exception e) {
			LOGGER.error("Exception replicating " + path, e);
			throw new ServiceException(e);
		} finally{
			if (session != null) {
				session.logout();
			}
			if (resourceResolver != null && resourceResolver.isLive()) {
				resourceResolver.close();
			}
		}
	}

	@Override
	public void replicateToOutbox(String path)
			throws ServiceException {
		Session session = null;
		ResourceResolver resourceResolver = null;
		try {
			resourceResolver = ResolverFactoryHelper.getWriterResourceResolver(resolverFactory);
			LOGGER.debug("Created resourceResolver from factory");
			
			session = resourceResolver.adaptTo(Session.class);
			LOGGER.debug("Session created successfully");
			
			final ReplicationOptions replicationOptions = new ReplicationOptions();
			replicationOptions.setFilter(DISTRIBUTE_AGENT_FILTER);
			replicationOptions.setSynchronous(true);
		    replicator.replicate(session, ReplicationActionType.ACTIVATE, path, replicationOptions);
			LOGGER.debug("Replicated to outbox successfully : " + path);
			
		} catch (Exception e) {
			LOGGER.error("Exception replicating " + path, e);
			throw new ServiceException(e);
		} finally{
			if (session != null) {
				session.logout();
			}
			if (resourceResolver != null && resourceResolver.isLive()) {
				resourceResolver.close();
			}
		}
	}
}
