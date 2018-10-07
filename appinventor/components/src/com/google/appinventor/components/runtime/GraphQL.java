package com.google.appinventor.components.runtime;

import android.app.Activity;
import android.os.Handler;
import com.apollographql.apollo.ApolloClient;
import com.apollographql.apollo.api.*;
import com.google.appinventor.components.annotations.*;
import com.google.appinventor.components.common.ComponentCategory;
import com.google.appinventor.components.common.PropertyTypeConstants;
import com.google.appinventor.components.common.YaVersion;
import okhttp3.OkHttpClient;
import org.jetbrains.annotations.NotNull;

@DesignerComponent(version = YaVersion.GRAPHQL_COMPONENT_VERSION,
    description = "Non-visible component that interacts with a GraphQL endpoint.",
    designerHelpDescription = "Non-visible component that interacts with a GraphQL endpoint.",
    category = ComponentCategory.EXPERIMENTAL,
    nonVisible = true,
    iconName = "images/graphQL.png")
@SimpleObject
@UsesPermissions(permissionNames = "android.permission.INTERNET")
@UsesLibraries(libraries = "apollo-runtime.jar, apollo-api.jar, okhttp.jar, okio.jar")
public class GraphQL extends AndroidNonvisibleComponent implements Component {

  private static final String LOG_TAG = "GraphQL";

  private final Handler androidUIHandler;
  private final Activity activity;

  private final OkHttpClient httpClient;

  private String endpointURL;
  private ApolloClient gqlClient;

  /**
   * Creates a new GraphQL component.
   *
   * @param container the form that this component is contained in.
   */
  public GraphQL(ComponentContainer container) {
    super(container.$form());

    this.androidUIHandler = new Handler();
    this.activity = container.$context();

    this.httpClient = new OkHttpClient();
  }

  /**
   * Getter for the GraphQL endpoint URL.
   *
   * @return the URL for this GraphQL endpoint.
   */
  @SimpleProperty(category = PropertyCategory.BEHAVIOR,
      description = "Gets the URL for this GraphQL endpoint.",
      userVisible = false)
  public String GqlEndpointUrl() {
    return endpointURL;
  }

  /**
   * Specifies the URL for this GraphQL endpoint.
   *
   * @param gqlUrl the URL for this GraphQL endpoint.
   */
  @DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_STRING)
  @SimpleProperty(description = "Sets the URL for this GraphQL endpoint.")
  public void GqlEndpointUrl(String gqlUrl) {
    endpointURL = gqlUrl;

    // Create a new GraphQL client on the endpoint URL.
    gqlClient = ApolloClient.builder()
        .serverUrl(endpointURL)
        .okHttpClient(httpClient)
        .build();
  }

  @SimpleEvent(description = "Event triggered by \"Query\" or \"Mutate\" methods.")
  public void GqlGotResponse(String gqlOperationName, String gqlResponse) {
    EventDispatcher.dispatchEvent(this, "GqlGotResponse", gqlOperationName, gqlResponse);
  }

  @SimpleEvent(description = "Indicates that the GraphQL endpoint responded with an error.")
  public void GqlGotError(String gqlError) {
    EventDispatcher.dispatchEvent(this, "GqlGotError", gqlError);
  }

  @SimpleFunction(description = "Execute a GraphQL query against the endpoint.")
  public void GqlQuery(String gqlOperationName, String gqlQuery) {
  }

  @SimpleFunction(description = "Execute a GraphQL mutation against the endpoint.")
  public void GqlMutate(String gqlOperationName, String gqlMutation) {
  }

  private static class BasicMutation implements Mutation<Operation.Data, Object, Operation.Variables> {

    private final String operationName;

    public BasicMutation(String operationName) {
      this.operationName = operationName;
    }

    @Override
    public String queryDocument() {
      return null;
    }

    @Override
    public Variables variables() {
      return null;
    }

    @Override
    public ResponseFieldMapper<Data> responseFieldMapper() {
      return null;
    }

    @Override
    public Object wrapData(Data data) {
      return null;
    }

    @NotNull
    @Override
    public OperationName name() {
      return null;
    }

    @NotNull
    @Override
    public String operationId() {
      return null;
    }
  }

  private static class BasicQuery implements Query<Operation.Data, Object, Operation.Variables> {

    @Override
    public String queryDocument() {
      return null;
    }

    @Override
    public Variables variables() {
      return null;
    }

    @Override
    public ResponseFieldMapper<Data> responseFieldMapper() {
      return null;
    }

    @Override
    public Object wrapData(Data data) {
      return null;
    }

    @NotNull
    @Override
    public OperationName name() {
      return null;
    }

    @NotNull
    @Override
    public String operationId() {
      return null;
    }
  }
}
