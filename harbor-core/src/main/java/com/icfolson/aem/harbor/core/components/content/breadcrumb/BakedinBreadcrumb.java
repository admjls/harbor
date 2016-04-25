package com.icfolson.aem.harbor.core.components.content.breadcrumb;

import com.icfolson.aem.library.api.components.annotations.AutoInstantiate;
import com.citytechinc.cq.component.annotations.Component;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Model;

@Component(
        value = "Bakedin Breadcrumb",
        group = ".hidden",
        resourceSuperType = Breadcrumb.RESOURCE_TYPE,
        disableTargeting = true,
        actions = { "text: Breadcrumb", "-", "edit" },
        suppressTouchUIDialog = true
    )
@AutoInstantiate(instanceName = "breadcrumb")
@Model(adaptables = Resource.class)
public class BakedinBreadcrumb extends InheritingBreadcrumb {

    public static final String RESOURCE_TYPE = "harbor/components/content/bakedinbreadcrumb";

}