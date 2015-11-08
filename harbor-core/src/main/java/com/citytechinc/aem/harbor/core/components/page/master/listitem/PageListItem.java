package com.citytechinc.aem.harbor.core.components.page.master.listitem;

import com.citytechinc.aem.bedrock.api.page.PageDecorator;
import org.apache.commons.lang3.StringUtils;

import com.citytechinc.cq.component.annotations.Component;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Model;

import javax.inject.Inject;

@Component(value = "Page List Item", group = ".hidden", noDecoration = true, editConfig = false, path = "page/common/master/listitem")
@Model(adaptables = Resource.class)
public class PageListItem {

	@Inject
	private PageDecorator currentPage;

	public String getListItemTitle() {
		if (StringUtils.isNotEmpty(currentPage.getPageTitle())) {
			return currentPage.getPageTitle();
		}

		return currentPage.getTitle();
	}

}
