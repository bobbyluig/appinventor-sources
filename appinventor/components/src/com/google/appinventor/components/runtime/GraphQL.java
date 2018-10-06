package com.google.appinventor.components.runtime;

import com.google.appinventor.components.annotations.DesignerComponent;
import com.google.appinventor.components.annotations.SimpleObject;
import com.google.appinventor.components.annotations.UsesPermissions;
import com.google.appinventor.components.common.ComponentCategory;
import com.google.appinventor.components.common.YaVersion;

@DesignerComponent(version = YaVersion.GRAPHQL_COMPONENT_VERSION,
    description = "Non-visible component that interacts with a GraphQL endpoint.",
    designerHelpDescription = "Non-visible component that interacts with a GraphQL endpoint.",
    category = ComponentCategory.EXPERIMENTAL,
    nonVisible = true,
    iconName = "images/graphQL.png")
@SimpleObject
@UsesPermissions(permissionNames = "android.permission.INTERNET")
public class GraphQL extends AndroidNonvisibleComponent implements Component {

  private static final String LOG_TAG = "GraphQL";

  /**
   * Creates a new GraphQL component.
   *
   * @param container the form that this component is contained in.
   */
  public GraphQL(ComponentContainer container) {
    super(container.$form());
  }
}
