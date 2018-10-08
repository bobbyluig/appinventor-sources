package com.google.appinventor.client.editor.simple.components;

import com.google.appinventor.client.Ode;
import com.google.appinventor.client.editor.simple.SimpleComponentDatabase;
import com.google.appinventor.client.editor.simple.SimpleEditor;
import com.google.appinventor.client.editor.youngandroid.YaBlocksEditor;
import com.google.appinventor.client.properties.json.ClientJsonParser;
import com.google.appinventor.shared.simple.ComponentDatabaseInterface;
import com.google.gwt.user.client.ui.Image;

public class MockGraphQL extends MockNonVisibleComponent {

  public static final String TYPE = "GraphQL";
  private static final String PROPERTY_NAME_GQL_ENDPOINT_URL = "GqlEndpointUrl";

  /**
   * Creates a new instance of a non-visible component whose icon is
   * loaded dynamically (not part of the icon image bundle)
   */
  public MockGraphQL(SimpleEditor editor, String type, Image iconImage) {
    super(editor, type, iconImage);
  }

  @Override
  public void onPropertyChange(String propertyName, String newValue) {
    super.onPropertyChange(propertyName, newValue);

    if (PROPERTY_NAME_GQL_ENDPOINT_URL.equals(propertyName) && !newValue.isEmpty()) {
      final long projectId = Ode.getInstance().getCurrentYoungAndroidProjectId();

      if (projectId != 0) {
        final SimpleComponentDatabase database = SimpleComponentDatabase.getInstance(projectId);
      }
    }
  }

  @Override
  public void onCreateFromPalette() {
    super.onCreateFromPalette();
  }

  private void runIntrospectionQuery(String url) {
  }
}
