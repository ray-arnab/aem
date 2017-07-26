package com.company.aem.commons.core.listeners;

import java.util.Arrays;

import javax.jcr.Property;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.observation.Event;
import javax.jcr.observation.EventIterator;
import javax.jcr.observation.EventListener;
import javax.jcr.observation.ObservationManager;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.osgi.service.component.ComponentContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.company.aem.commons.core.utils.ResolverFactoryHelper;

/**
 * 
 * sling:alias (if available) override the name of the page while forming the URL
 * However it may not have the same out-of-the-box protection from special characters
 * 
 * This listener ensures that illegal characters in alias are masked with -
 * 
 * @author aray5
 *
 */
@Component(immediate = true)
@Service
public class AliasPropertyListener implements EventListener {

	private Logger LOGGER = LoggerFactory.getLogger(this.getClass());

	private static final String ILLEGAL_CHARS = "[^a-zA-Z0-9-_]"; 
	  
	@Reference
	private ResourceResolverFactory resolverFactory;

	private Session observationSession = null;

	private ObservationManager observationManager;

	private ResourceResolver resourceResolver = null;

	protected void activate(ComponentContext ctx) {
		try {

			resourceResolver = ResolverFactoryHelper.getWriterResourceResolver(resolverFactory);

			observationSession = resourceResolver.adaptTo(Session.class);

			observationManager = observationSession.getWorkspace()
					.getObservationManager();
			final int events = Event.PROPERTY_ADDED | Event.PROPERTY_CHANGED;
			final String[] types = { "cq:PageContent" };
			final String path = "/content"; // define the path

			observationManager.addEventListener(this, events, path, true, null,
					types, true);

			LOGGER.info(
					"AliasPropertyListener :: activate :: Observing property changes to {} nodes under {}",
					Arrays.asList(types), path);

		} catch (Exception e) {
			LOGGER.error("Error in activate"
					+ Arrays.toString(e.getStackTrace()));
		}
	}

	protected void deactivate(ComponentContext componentContext)
			throws RepositoryException {
		LOGGER.info("AliasPropertyListener :: deactivate :: Stopping observation");

		if (observationManager != null) {
			observationManager.removeEventListener(this);
		}
		if (observationSession != null) {
			observationSession.logout();
			observationSession = null;
		}
		if (resourceResolver != null) {
			resourceResolver.close();
			resourceResolver = null;
		}
	}

	@Override
	public void onEvent(final EventIterator events) {
		// Handle events
		LOGGER.debug("AliasPropertyListener :: onEvent :: Event handling started! ");
		
		Session session = null;
		while (events.hasNext()) {
			try {
				Event event = events.nextEvent();

				
				final String path = event.getPath();
				LOGGER.debug("AliasPropertyListener :: onEvent :: Path = "
						+ path);

				session = resourceResolver.adaptTo(Session.class);
				Property changedProperty = session.getProperty(event.getPath());
				if (changedProperty.getName().equalsIgnoreCase(
						"sling:alias")) {
					
					String aliasProp = changedProperty.getString();
					LOGGER.info("AliasPropertyListener :: onEvent :: Alias : " + aliasProp);
					String newAliasProp = AliasPropertyListener.getMaskedAlias(aliasProp);
					LOGGER.info("AliasPropertyListener :: onEvent :: New Alias : " + newAliasProp);
					changedProperty.setValue(newAliasProp);
					
					LOGGER.info("AliasPropertyListener :: onEvent :: Saving session");
					session.save();
				}

			} catch (RepositoryException e) {
				LOGGER.error(
						"AliasPropertyListener :: onEvent :: ",
						e);
			} catch (Exception e) {
				LOGGER.error(
						"AliasPropertyListener :: onEvent :: ",
						Arrays.toString(e.getStackTrace()));
			}/*finally{
				if(session != null){
					session.logout();
					session = null;
				}
			}*/

		}
	}
	
	public static String getMaskedAlias (String originalAlias) {
		if(originalAlias != null) {
			return originalAlias.replaceAll(ILLEGAL_CHARS, "-").toLowerCase();
		}
		return null;
	}
	
	//TODO: Move these to JUnit
//	public static void main(String[] args) {
//		System.out.println(getMaskedAlias("travel"));
//		System.out.println(getMaskedAlias("travel-info"));
//		System.out.println(getMaskedAlias("travel info"));
//		System.out.println(getMaskedAlias("tRa-vel-info0"));
//		System.out.println(getMaskedAlias("travel0in_fo1"));
//		System.out.println(getMaskedAlias("travel{]}$$�$�info1"));
//	}
}
