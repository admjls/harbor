package com.icfolson.aem.harbor.core.components.content.list.assets;

import java.util.List;

import com.icfolson.aem.library.api.components.annotations.AutoInstantiate;
import com.icfolson.aem.harbor.core.constants.groups.ComponentGroups;
import com.citytechinc.cq.component.annotations.Component;
import com.citytechinc.cq.component.annotations.DialogField;
import com.citytechinc.cq.component.annotations.widgets.DialogFieldSet;
import com.icfolson.aem.harbor.api.constants.lists.ListConstants;
import com.icfolson.aem.harbor.api.lists.construction.ListConstructionStrategy;
import com.icfolson.aem.harbor.api.lists.rendering.ListRenderingStrategy;
import com.icfolson.aem.harbor.core.components.content.list.AbstractListComponent;
import com.day.cq.dam.api.Asset;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;

import javax.inject.Inject;

@Component(value = "Asset List", group = ComponentGroups.HARBOR_LISTS, resourceSuperType = AbstractListComponent.RESOURCE_TYPE, name = "lists/assetlist")
@AutoInstantiate(instanceName = ListConstants.LIST_PAGE_CONTEXT_NAME)
@Model(adaptables = Resource.class)
public class AssetList extends AbstractListComponent<Asset, List<AssetListRenderingStrategy.RenderableAsset>> {

	@DialogField
	@DialogFieldSet(title = "List Construction")
	@Inject @Self
	private AssetListConstructionStrategy constructionStrategy;

	@DialogField
	@DialogFieldSet(title = "List Rendering")
	@Inject @Self
	private AssetListRenderingStrategy renderingStrategy;

	@Override
	protected ListConstructionStrategy<Asset> getListConstructionStrategy() {
		return constructionStrategy;

	}

	@Override
	protected ListRenderingStrategy<Asset, List<AssetListRenderingStrategy.RenderableAsset>> getListRenderingStrategy() {

		return renderingStrategy;

	}

    @Override
    public Boolean getIsUnorderedList() {
        return true;
    }
}