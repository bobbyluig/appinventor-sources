package com.google.appinventor.client.editor.simple.components;

import com.google.appinventor.client.editor.simple.SimpleEditor;
import com.google.gwt.user.client.ui.Image;

public class MockGraphQL extends MockNonVisibleComponent {

  public static final String TYPE = "GraphQL";

  /**
   * Creates a new instance of a non-visible component whose icon is
   * loaded dynamically (not part of the icon image bundle)
   *
   * @param editor
   * @param type
   * @param iconImage
   */
  public MockGraphQL(SimpleEditor editor, String type, Image iconImage) {
    super(editor, type, iconImage);
  }
}
