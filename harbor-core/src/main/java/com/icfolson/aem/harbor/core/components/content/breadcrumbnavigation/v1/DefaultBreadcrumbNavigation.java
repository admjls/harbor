package com.icfolson.aem.harbor.core.components.content.breadcrumbnavigation.v1;

import com.google.common.collect.Lists;
import com.icfolson.aem.harbor.api.components.content.breadcrumb.*;
import com.icfolson.aem.harbor.api.content.page.HierarchicalPage;
import com.icfolson.aem.library.api.page.PageDecorator;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Model;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

@Model(adaptables = Resource.class, adapters = BreadcrumbNavigation.class, resourceType = DefaultBreadcrumbNavigation.RESOURCE_TYPE)
public class DefaultBreadcrumbNavigation implements BreadcrumbNavigation<BreadcrumbNavigationItem> {

    public static final String RESOURCE_TYPE = "harbor/components/content/breadcrumbnavigation/v1/breadcrumbnavigation";

    @Inject
    private PageDecorator currentPage;

    private Optional<PageDecorator> rootPageOptional;
    private List<BreadcrumbNavigationItem> trail;

    @Override
    public Iterable<BreadcrumbNavigationItem> getTrail() {
        return getTrailList();
    }

    @Override
    public boolean includeCurrentPage() {
        return true;
    }

    @Override
    public boolean includeRootPage() {
        return true;
    }

    @Override
    public int getLength() {
        return getTrailList().size();
    }

    protected List<BreadcrumbNavigationItem> getTrailList() {
        if (trail != null) {
            return trail;
        }

        trail = Lists.newArrayList();
        Optional<PageDecorator> rootPageOptional = getRootPage();

        if (!rootPageOptional.isPresent()) {
            return trail;
        }

        if (includeCurrentPage()) {
            trail.add(new DefaultBreadcrumbNavigationItem(currentPage,
                    currentPage.getPath().equals(rootPageOptional.get().getPath()),
                    true));
        }

        if (!currentPage.getPath().equals(rootPageOptional.get().getPath())) {
            PageDecorator currentPagePointer = currentPage.getParent();

            while (currentPagePointer != null && !currentPagePointer.getPath().equals(rootPageOptional.get().getPath())) {
                if (!currentPagePointer.isHideInNav()) {
                    trail.add(new DefaultBreadcrumbNavigationItem(currentPagePointer));
                }

                currentPagePointer = currentPagePointer.getParent();
            }

            if (includeRootPage()) {
                trail.add(new DefaultBreadcrumbNavigationItem(currentPagePointer, true, false));
            }
        }

        trail = Lists.reverse(trail);

        return trail;
    }

    protected Optional<PageDecorator> getRootPage() {
        if (this.rootPageOptional == null) {
            HierarchicalPage hierarchicalPage = currentPage.getContentResource().adaptTo(HierarchicalPage.class);

            if (hierarchicalPage.getSectionLandingPage().isPresent()) {
                this.rootPageOptional = Optional.of(hierarchicalPage.getSectionLandingPage().get());
            }
            else if (hierarchicalPage.getHomePage().isPresent()) {
                this.rootPageOptional = Optional.of(hierarchicalPage.getHomePage().get());
            }
            else {
                this.rootPageOptional = Optional.empty();
            }

        }

        return rootPageOptional;
    }

}
