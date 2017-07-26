# AEM projects

## Introduction 

The purpose is to explore and demonstrate certain development strategies involving AEM.


## Features explored

- Project setup for multi-project / multi tenant situation

	- aem.commons : project to hold common funcitonality

		- basic components

		- basic functionality (eg: CheckPermissionServlet, MailService with attachments, AliasPropertyListener, any other instance specific housekeeping)

		- configurations that apply on whole instance (eg: Externalizer, ServiceUserMapper, JcrResourceResolverFactory)

	- aem.intranet : holds project specific funcitonality for a given site/brand (say, company-intranet)

		- specialized page templates

		- factory configurations that can be project specific (eg: Logger)
	
	
- Content hierarchy

```
	/content
		/company-intranet
			/language-masters			... base-page	[blueprint]
				/en						... home-page
					/products			... category-page (1st level of IA)
						/child-page		... content page
					/services
					..
					/errors
						/404			... error-page
						/default		... error-page
				/fr							[live copy/language copy of 'en']
			/us								[live copy from blueprint]		
				/en							{externalizes local.us.company.net/en/}
					...
			/fr								[live copy from blueprint]
				/en							{externalizes local.fr.company.net/en/}
					...
				/fr							{externalizes local.fr.company.net/fr/}	
					...
			...
		some-other-brand
			...
```


- Authorable error pages (using ACS AEM commons)


- Versioned clientlibs for the intranet website (using ACS AEM commons)

	- /apps/company-intranet/config/rewriter


- Embedded clientlibs for the intranet website

	- /etc/designs/company-intranet/clientlib-all


- SEO module in company-commons
 
	- A separate page property tab for SEO
	
	/apps/company-commons/components/structure/page/tab_SEO

		1. whether page is excluded in xml sitemap

		2. meta opengraph props that should apply on page

		3. meta twitter props that should apply on page

		4. meta robots props that shuld apply on page
		

	- Corresponding models and sightly to inject markup
	
	/apps/company-commons/utils/seo
	
	- Usage in company-intranet

		1. Configurations : /apps/company-intranet/config.dev/com.company.aem.commons.core.config.impl.SEO...	

		2. Page dialog: /apps/company-intranet/components/pages/base-page/dialog/items/tabs/items/seo

		3. Includes: /apps/company-intranet/components/pages/base-page/base-page.html includes /apps/company-commons/utils/seo/meta.html
		
		
	- XML Sitemap generator (using ACS AEM Commons)
	
	http://local.us.company.net/en.sitemap.xml
	
	- Configurations: /apps/company-intranet/config.publish/com.adobe.acs.commons.wcm.impl.SiteMapServlet...
	
	

- URL externalization (local env)

	Used regex based mappings in /apps/company-commons/config.publish/org.apache.sling.jcr.resource.internal.JcrResourceResolverFactoryImpl
	
	- /content/company-intranet/XX -> local.XX.company.net
	
	- replace .html with trailing slash
	
	
- Static domains for designs and assets (using ACS AEM Commons)

	- /apps/company-intranet/config.publish/com.adobe.acs.commons.rewriter.impl.StaticReferenceRewriteTransformerFactory-company-intranet

	
- Sling Dynamic Include

	- remove dispatcher caching of certain components in a page
	

- Apache configuration artefacts to support the intranet website