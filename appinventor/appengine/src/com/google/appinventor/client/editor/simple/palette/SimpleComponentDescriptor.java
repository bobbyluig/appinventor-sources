// -*- mode: java; c-basic-offset: 2; -*-
// Copyright 2009-2011 Google, All Rights reserved
// Copyright 2011-2017 MIT, All rights reserved
// Released under the Apache License, Version 2.0
// http://www.apache.org/licenses/LICENSE-2.0

package com.google.appinventor.client.editor.simple.palette;

import com.google.appinventor.client.Images;
import com.google.appinventor.client.Ode;
import com.google.appinventor.client.editor.simple.SimpleComponentDatabase;
import com.google.appinventor.client.editor.simple.SimpleEditor;
import com.google.appinventor.client.editor.simple.components.MockBall;
import com.google.appinventor.client.editor.simple.components.MockButton;
import com.google.appinventor.client.editor.simple.components.MockCanvas;
import com.google.appinventor.client.editor.simple.components.MockCheckBox;
import com.google.appinventor.client.editor.simple.components.MockCircle;
import com.google.appinventor.client.editor.simple.components.MockCloudDB;
import com.google.appinventor.client.editor.simple.components.MockComponent;
import com.google.appinventor.client.editor.simple.components.MockContactPicker;
import com.google.appinventor.client.editor.simple.components.MockDatePicker;
import com.google.appinventor.client.editor.simple.components.MockEmailPicker;
import com.google.appinventor.client.editor.simple.components.MockFeatureCollection;
import com.google.appinventor.client.editor.simple.components.MockFirebaseDB;
import com.google.appinventor.client.editor.simple.components.MockGraphQL;
import com.google.appinventor.client.editor.simple.components.MockHorizontalArrangement;
import com.google.appinventor.client.editor.simple.components.MockImage;
import com.google.appinventor.client.editor.simple.components.MockImagePicker;
import com.google.appinventor.client.editor.simple.components.MockImageSprite;
import com.google.appinventor.client.editor.simple.components.MockLabel;
import com.google.appinventor.client.editor.simple.components.MockLineString;
import com.google.appinventor.client.editor.simple.components.MockListPicker;
import com.google.appinventor.client.editor.simple.components.MockListView;
import com.google.appinventor.client.editor.simple.components.MockMap;
import com.google.appinventor.client.editor.simple.components.MockMarker;
import com.google.appinventor.client.editor.simple.components.MockNonVisibleComponent;
import com.google.appinventor.client.editor.simple.components.MockPasswordTextBox;
import com.google.appinventor.client.editor.simple.components.MockPhoneNumberPicker;
import com.google.appinventor.client.editor.simple.components.MockPolygon;
import com.google.appinventor.client.editor.simple.components.MockRadioButton;
import com.google.appinventor.client.editor.simple.components.MockRectangle;
import com.google.appinventor.client.editor.simple.components.MockScrollHorizontalArrangement;
import com.google.appinventor.client.editor.simple.components.MockScrollVerticalArrangement;
import com.google.appinventor.client.editor.simple.components.MockSlider;
import com.google.appinventor.client.editor.simple.components.MockSpinner;
import com.google.appinventor.client.editor.simple.components.MockTableArrangement;
import com.google.appinventor.client.editor.simple.components.MockTextBox;
import com.google.appinventor.client.editor.simple.components.MockTimePicker;
import com.google.appinventor.client.editor.simple.components.MockVerticalArrangement;
import com.google.appinventor.client.editor.simple.components.MockVideoPlayer;
import com.google.appinventor.client.editor.simple.components.MockWebViewer;
import com.google.appinventor.shared.storage.StorageUtil;
import com.google.common.collect.Maps;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;

import java.util.Map;

/**
 * Descriptor for components on the component palette panel.
 * This class is immutable.
 *
 */
public final class SimpleComponentDescriptor {

  // Component display name
  private final String name;

  private final SimpleEditor editor;

  // Help information to display for component
  private final String helpString;

  // Whether External Component
  private final boolean external;

  // Goto documentation category URL piece
  private final String categoryDocUrlString;

  // Link to external documentation
  private final String helpUrl;

  // Whether to show the component on the palette
  private final boolean showOnPalette;

  // Whether the component has a visual representation in the app's UI
  private final boolean nonVisible;

  // an instantiated mockcomponent is currently necessary in order to
  // to get the image, category, and description
  private MockComponent cachedMockComponent = null;

  // The version of the extension (meaning is defined by the extension author).
  private final int version;

  private final String versionName;

  private final String dateBuilt;

  // Component database: information about components (including their properties and events)
  private final SimpleComponentDatabase COMPONENT_DATABASE;

  /* We keep a static map of image names to images in the image bundle so
   * that we can avoid making individual calls to the server for static image
   * that are already in the bundle. This is purely an efficiency optimization
   * for mock non-visible components.
   */
  private static final Images images = Ode.getImageBundle();
  private static final Map<String, ImageResource> bundledImages = Maps.newHashMap();
  private static boolean imagesInitialized = false;

  private static void initBundledImages() {
    bundledImages.put("images/accelerometersensor.png", images.accelerometersensor());
    bundledImages.put("images/gyroscopesensor.png", images.gyroscopesensor());
    bundledImages.put("images/nearfield.png", images.nearfield());
    bundledImages.put("images/activityStarter.png", images.activitystarter());
    bundledImages.put("images/barcodeScanner.png", images.barcodeScanner());
    bundledImages.put("images/bluetooth.png", images.bluetooth());
    bundledImages.put("images/camera.png", images.camera());
    bundledImages.put("images/camcorder.png", images.camcorder());
    bundledImages.put("images/clock.png", images.clock());
    bundledImages.put("images/fusiontables.png", images.fusiontables());
    bundledImages.put("images/gameClient.png", images.gameclient());
    bundledImages.put("images/locationSensor.png", images.locationSensor());
    bundledImages.put("images/notifier.png", images.notifier());
    bundledImages.put("images/legoMindstormsNxt.png", images.legoMindstormsNxt());
    bundledImages.put("images/legoMindstormsEv3.png", images.legoMindstormsEv3());
    bundledImages.put("images/orientationsensor.png", images.orientationsensor());
    bundledImages.put("images/pedometer.png", images.pedometerComponent());
    bundledImages.put("images/phoneip.png", images.phonestatusComponent());
    bundledImages.put("images/phoneCall.png", images.phonecall());
    bundledImages.put("images/player.png", images.player());
    bundledImages.put("images/soundEffect.png", images.soundeffect());
    bundledImages.put("images/soundRecorder.png", images.soundRecorder());
    bundledImages.put("images/speechRecognizer.png", images.speechRecognizer());
    bundledImages.put("images/textToSpeech.png", images.textToSpeech());
    bundledImages.put("images/texting.png", images.texting());
    bundledImages.put("images/datePicker.png", images.datePickerComponent());
    bundledImages.put("images/timePicker.png", images.timePickerComponent());
    bundledImages.put("images/tinyDB.png", images.tinyDB());
    bundledImages.put("images/file.png", images.file());
    bundledImages.put("images/tinyWebDB.png", images.tinyWebDB());
    bundledImages.put("images/firebaseDB.png", images.firebaseDB());
    bundledImages.put("images/graphQL.png", images.graphQL());
    bundledImages.put("images/twitter.png", images.twitterComponent());
    bundledImages.put("images/voting.png", images.voting());
    bundledImages.put("images/web.png", images.web());
    bundledImages.put("images/mediastore.png", images.mediastore());
    bundledImages.put("images/sharing.png", images.sharingComponent());
    bundledImages.put("images/spinner.png", images.spinner());
    bundledImages.put("images/listView.png", images.listview());
    bundledImages.put("images/yandex.png", images.yandex());
    bundledImages.put("images/proximitysensor.png", images.proximitysensor());
    bundledImages.put("images/extension.png", images.extension());
    bundledImages.put("images/cloudDB.png", images.cloudDB());
    bundledImages.put("images/map.png", images.map());
    bundledImages.put("images/marker.png", images.marker());
    bundledImages.put("images/circle.png", images.circle());
    bundledImages.put("images/linestring.png", images.linestring());
    bundledImages.put("images/polygon.png", images.polygon());
    bundledImages.put("images/featurecollection.png", images.featurecollection());

    imagesInitialized = true;
  }

  /**
   * Creates a new component descriptor.
   *
   * @param name  component display name
   */
  public SimpleComponentDescriptor(String name,
                                   SimpleEditor editor,
                                   int version,
                                   String versionName,
                                   String dateBuilt,
                                   String helpString,
                                   String helpUrl,
                                   String categoryDocUrlString,
                                   boolean showOnPalette,
                                   boolean nonVisible,
                                   boolean external) {
    this.name = name;
    this.editor = editor;
    this.version = version;
    this.versionName = versionName;
    this.dateBuilt = dateBuilt;
    this.helpString = helpString;
    this.helpUrl = helpUrl;
    this.categoryDocUrlString = categoryDocUrlString;
    this.showOnPalette = showOnPalette;
    this.nonVisible = nonVisible;
    this.external = external;
    COMPONENT_DATABASE = SimpleComponentDatabase.getInstance(editor.getProjectId());
  }

  /**
   * Returns the display name of the component.
   *
   * @return component display name
   */
  public String getName() {
    return name;
  }

  /**
   * Returns the help string for the component.  For more detail, see
   * javadoc for
   * {@link com.google.appinventor.client.editor.simple.ComponentDatabase#getHelpString(String)}.
   *
   * @return helpful message about the component
   */
  public String getHelpString() {
    return helpString;
  }

  /**
   * Returns the help URL for the component.  For more detail, see javadoc for
   * {@link com.google.appinventor.client.editor.simple.ComponentDatabase#getHelpUrl(String)}.
   *
   * @return URL to external documentation provided for an extension
   */
  public String getHelpUrl() {
    return helpUrl;
  }

  /**
   * Returns the origin of the component
   * @return true if component is external
   */
  public boolean getExternal() {
    return external;
  }
  /**
   * Returns the categoryDocUrl string for the component.  For more detail, see
   * javadoc for
   * {@link com.google.appinventor.client.editor.simple.ComponentDatabase#getCategoryDocUrlString(String)}.
   *
   * @return helpful message about the component
   */
  public String getCategoryDocUrlString() {
    return categoryDocUrlString;
  }

  /**
   * Returns whether this component should be shown on the palette.  For more
   * detail, see javadoc for
   * {@link com.google.appinventor.client.editor.simple.ComponentDatabase#getHelpString(String)}.
   *
   * @return whether the component should be shown on the palette
   */
  public boolean getShowOnPalette() {
    return showOnPalette;
  }

  /**
   * Returns whether this component is visible in the app's UI.  For more
   * detail, see javadoc for
   * {@link com.google.appinventor.client.editor.simple.ComponentDatabase#getHelpString(String)}.
   *
   * @return whether the component is non-visible
   */
  public boolean getNonVisible() {
    return nonVisible;
  }

  /**
   * Returns an image for display on the component palette.
   *
   * @return  image for component
   */
  public Image getImage() {
    if (nonVisible) {
      String type = COMPONENT_DATABASE.getComponentType(name);
      return getImageFromPath(COMPONENT_DATABASE.getIconName(name),
          type.substring(0, type.lastIndexOf('.')),
          editor.getProjectId());
    } else {
      return getCachedMockComponent(name, editor).getIconImage();
    }
  }

  /**
   * Returns the version of the component, if any.
   *
   * @return  component version string
   */
  public int getVersion() {
    return version;
  }

  /**
   * Returns the custom version name of the component, if any.
   *
   * @return  component version name
   */
  public String getVersionName() {
    return versionName;
  }

  /**
   * Returns the date the component was built, if any.
   *
   * @return  ISO 8601 formated date the component was built
   */
  public String getDateBuilt() {
    return dateBuilt;
  }

  /**
   * Returns a draggable image for the component. Used when dragging a
   * component from the palette onto the form.
   *
   * @return  draggable widget for component
   */
  public Widget getDragWidget() {
    return createMockComponent(name, COMPONENT_DATABASE.getComponentType(name), editor);
  }

  /**
   * Instantiates the corresponding mock component.
   *
   * @return  mock component
   */
  public MockComponent createMockComponentFromPalette() {
    MockComponent mockComponent = createMockComponent(name,
        COMPONENT_DATABASE.getComponentType(name), editor);
    mockComponent.onCreateFromPalette();
    return mockComponent;
  }

  /**
   * Gets cached mock component; creates if necessary.
   */
  private MockComponent getCachedMockComponent(String name, SimpleEditor editor) {
    if (cachedMockComponent == null) {
      cachedMockComponent = createMockComponent(name,
          COMPONENT_DATABASE.getComponentType(name), editor);
    }
    return cachedMockComponent;
  }

  public static Image getImageFromPath(String iconPath, String packageName, long projectId) {
    if (!imagesInitialized) {
      initBundledImages();
    }
    if (iconPath.startsWith("aiwebres/") && packageName != null) {
      // icon for extension
      Image image = new Image(StorageUtil.getFileUrl(projectId,
          "assets/external_comps/" + packageName + "/" + iconPath));
      image.setWidth("16px");
      image.setHeight("16px");
      return image;
    }
    if (bundledImages.containsKey(iconPath)) {
      return new Image(bundledImages.get(iconPath));
    } else {
      return new Image(iconPath);
    }
  }

  /**
   * Instantiates mock component by name.
   */
  public static MockComponent createMockComponent(String name, String type, SimpleEditor editor) {
    if (SimpleComponentDatabase.getInstance(editor.getProjectId()).getNonVisible(name)) {
      if (MockFirebaseDB.TYPE.equals(name)) {
        return new MockFirebaseDB(editor, name,
            getImageFromPath(SimpleComponentDatabase.getInstance(editor.getProjectId()).getIconName(name),
                null, editor.getProjectId()));
      } else if (MockCloudDB.TYPE.equals(name)) {
        return new MockCloudDB(editor, name,
            getImageFromPath(SimpleComponentDatabase.getInstance(editor.getProjectId()).getIconName(name),
                null, editor.getProjectId()));
      } else if (MockGraphQL.TYPE.equals(name)) {
        return new MockGraphQL(editor, name,
            getImageFromPath(SimpleComponentDatabase.getInstance(editor.getProjectId()).getIconName(name),
                null, editor.getProjectId()));
      } else {
        String pkgName = type.contains(".") ? type.substring(0, type.lastIndexOf('.')) : null;
        return new MockNonVisibleComponent(editor, name,
            getImageFromPath(SimpleComponentDatabase.getInstance(editor.getProjectId()).getIconName(name),
                pkgName, editor.getProjectId()));
      }
    } else if (name.equals(MockButton.TYPE)) {
      return new MockButton(editor);
    } else if (name.equals(MockCanvas.TYPE)) {
      return new MockCanvas(editor);
    } else if (name.equals(MockCheckBox.TYPE)) {
      return new MockCheckBox(editor);
    } else if (name.equals(MockImage.TYPE)) {
      return new MockImage(editor);
    } else if (name.equals(MockLabel.TYPE)) {
      return new MockLabel(editor);
    } else if (name.equals(MockListView.TYPE)) {
      return new MockListView(editor);
    } else if (name.equals(MockSlider.TYPE)) {
        return new MockSlider(editor);
    } else if (name.equals(MockPasswordTextBox.TYPE)) {
      return new MockPasswordTextBox(editor);
    } else if (name.equals(MockRadioButton.TYPE)) {
      return new MockRadioButton(editor);
    } else if (name.equals(MockTextBox.TYPE)) {
      return new MockTextBox(editor);
    } else if (name.equals(MockContactPicker.TYPE)) {
      return new MockContactPicker(editor);
    } else if (name.equals(MockPhoneNumberPicker.TYPE)) {
      return new MockPhoneNumberPicker(editor);
    } else if (name.equals(MockEmailPicker.TYPE)) {
      return new MockEmailPicker(editor);
    } else if (name.equals(MockListPicker.TYPE)) {
      return new MockListPicker(editor);
    } else if (name.equals(MockDatePicker.TYPE)) {
      return new MockDatePicker(editor);
    } else if (name.equals(MockTimePicker.TYPE)) {
      return new MockTimePicker(editor);
    } else if (name.equals(MockHorizontalArrangement.TYPE)) {
      return new MockHorizontalArrangement(editor);
    } else if (name.equals(MockScrollHorizontalArrangement.TYPE)) {
      return new MockScrollHorizontalArrangement(editor);
    } else if (name.equals(MockVerticalArrangement.TYPE)) {
      return new MockVerticalArrangement(editor);
    } else if (name.equals(MockScrollVerticalArrangement.TYPE)) {
      return new MockScrollVerticalArrangement(editor);
    } else if (name.equals(MockTableArrangement.TYPE)) {
      return new MockTableArrangement(editor);
    } else if (name.equals(MockImageSprite.TYPE)) {
      return new MockImageSprite(editor);
    } else if (name.equals(MockBall.TYPE)) {
      return new MockBall(editor);
    } else if (name.equals(MockImagePicker.TYPE)) {
      return new MockImagePicker(editor);
    } else if (name.equals(MockVideoPlayer.TYPE)) {
      return new MockVideoPlayer(editor);
    } else if (name.equals(MockWebViewer.TYPE)) {
      return new MockWebViewer(editor);
    } else if (name.equals(MockSpinner.TYPE)) {
      return new MockSpinner(editor);
    } else if (name.equals(MockMap.TYPE)) {
      return new MockMap(editor);
    } else if (name.equals(MockMarker.TYPE)) {
      return new MockMarker(editor);
    } else if (name.equals(MockCircle.TYPE)) {
      return new MockCircle(editor);
    } else if (name.equals(MockLineString.TYPE)) {
      return new MockLineString(editor);
    } else if (name.equals(MockPolygon.TYPE)) {
      return new MockPolygon(editor);
    } else if (name.equals(MockRectangle.TYPE)) {
      return new MockRectangle(editor);
    } else if (name.equals(MockFeatureCollection.TYPE)) {
      return new MockFeatureCollection(editor);
    } else {
      // TODO(user): add 3rd party mock component proxy here
      throw new UnsupportedOperationException("unknown component: " + name);
    }
  }
}
