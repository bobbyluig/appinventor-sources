// 'use strict';
//
// goog.provide('AI.Blockly.Blocks.gql');
// goog.provide('AI.Blockly.GraphQL');
// goog.require('Blockly.Blocks.Utilities');
// goog.require('goog.net.XhrIo');

// Initialize namespace.
Blockly.Blocks.gql = {};
Blockly.GraphQLBlock = {};

// Constants for GraphQL blocks.
Blockly.GraphQLBlock.PRIMARY_COLOR = '#e535ab';
Blockly.GraphQLBlock.SECONDARY_COLOR = '#161e26';

// GraphQL introspection query.
// <editor-fold desc="INTROSPECTION_QUERY">
Blockly.GraphQLBlock.INTROSPECTION_QUERY =
  'query IntrospectionQuery { ' +
  '  __schema { ' +
  '    queryType { ' +
  '      name ' +
  '    } ' +
  '    mutationType { ' +
  '      name ' +
  '    } ' +
  '    types { ' +
  '      ...FullType ' +
  '    } ' +
  '  } ' +
  '} ' +
  ' ' +
  'fragment FullType on __Type { ' +
  '  kind ' +
  '  name ' +
  '  description ' +
  '  fields(includeDeprecated: true) { ' +
  '    name ' +
  '    description ' +
  '    args { ' +
  '      ...InputValue ' +
  '    } ' +
  '    type { ' +
  '      ...TypeRef ' +
  '    } ' +
  '    isDeprecated ' +
  '    deprecationReason ' +
  '  } ' +
  '  inputFields { ' +
  '    ...InputValue ' +
  '  } ' +
  '  interfaces { ' +
  '    ...TypeRef ' +
  '  } ' +
  '  enumValues(includeDeprecated: true) { ' +
  '    name ' +
  '    description ' +
  '    isDeprecated ' +
  '    deprecationReason ' +
  '  } ' +
  '  possibleTypes { ' +
  '    ...TypeRef ' +
  '  } ' +
  '} ' +
  ' ' +
  'fragment InputValue on __InputValue { ' +
  '  name ' +
  '  description ' +
  '  type { ' +
  '    ...TypeRef ' +
  '  } ' +
  '  defaultValue ' +
  '} ' +
  ' ' +
  'fragment TypeRef on __Type { ' +
  '  kind ' +
  '  name ' +
  '  ofType { ' +
  '    kind ' +
  '    name ' +
  '    ofType { ' +
  '      kind ' +
  '      name ' +
  '      ofType { ' +
  '        kind ' +
  '        name ' +
  '      } ' +
  '    } ' +
  '  } ' +
  '} ';
// </editor-fold>

// GraphQL introspection query cache.
Blockly.GraphQLBlock.schemas = {};

// Method to update cached introspection query and associated blocks.
Blockly.GraphQLBlock.updateSchema = function(endpoint) {
  // Build post data.
  var data = {
    'query': Blockly.GraphQLBlock.INTROSPECTION_QUERY,
    'operationName': 'IntrospectionQuery'
  };

  // Create headers.
  var headers = {
    'content-type': 'application/json'
  };

  // Send an introspection query.
  goog.net.XhrIo.send(endpoint, function(e) {
    // Get the XHR.
    var xhr = e.target;

    // Check if there were any errors sending the requests.
    if (xhr.getLastErrorCode() !== goog.net.ErrorCode.NO_ERROR) {
      console.log('Introspection query for ' + endpoint + ' failed with error code ' + xhr.getLastErrorCode() + '.');
      return;
    }

    // Get the response, which is in JSON format.
    var response = xhr.getResponseJson();

    // Check if there were any errors.
    if (response.hasOwnProperty('errors')) {
      console.log('Introspection query for ' + endpoint + ' failed with GraphQL errors.', response['errors']);
      return;
    }

    // Fetch the raw schema.
    var schema = response['data']['__schema'];

    // Create a type mapping for fast name lookup.
    var newTypes = {};

    // Modify the old type objects and add them to new types.
    for (var i = 0, type; type = schema.types[i]; i++) {
      var typeName = type['name'];
      newTypes[typeName] = type;
    }

    // Replace the old types with the new types.
    schema['types'] = newTypes;

    // Store the modified schema in cache.
    Blockly.GraphQLBlock.schemas[endpoint] = schema;

    // Fetch all blocks from the current workspace.
    var allBlocks = Blockly.mainWorkspace.getAllBlocks();

    // Go through blocks.
    // TODO(bobbyluig): Make this faster.
    for (var i = 0, block; block = allBlocks[i]; i++) {
      // Filter by GraphQL blocks with the matching endpoint.
      if (block.type === 'gql' && block.gqlUrl === endpoint) {
        // Inform the block that it should update its own schema.
        block.updateSchema();
      }
    }
  }, 'POST', JSON.stringify(data), headers);
};


Blockly.Blocks['gql_mutator'] = {
  init: function() {
    this.setColour(Blockly.GraphQLBlock.PRIMARY_COLOR);
    this.appendDummyInput().appendField('field');
    this.setPreviousStatement(true);
    this.setNextStatement(true);
    this.setTooltip('Add a field to this object.');
    this.contextMenu = false;
  }
};

// The base GraphQL block type.
Blockly.Blocks['gql'] = {
  gqlTypeToYailType: function(gqlType) {
    switch (gqlType) {
      case 'Int':
      case 'Float':
        return 'number';
      case 'ID':
      case 'String':
        return 'text';
      case 'Boolean':
        return 'boolean';
    }
  },

  gqlTypeToBlocklyType: function(gqlType) {
    var yailType = this.gqlTypeToYailType(gqlType);
    return Blockly.Blocks.Utilities.YailTypeToBlocklyType(yailType, Blockly.Blocks.Utilities.INPUT);
  },

  mutationToDom: function() {
    // Create a new mutation element to store data.
    var mutation = document.createElement('mutation');

    // Set basic attributes for this block shared by all GraphQL blocks.
    mutation.setAttribute('gql_url', this.gqlUrl);
    mutation.setAttribute('gql_name', this.gqlName);
    mutation.setAttribute('gql_type', this.gqlType);

    // If this block is not a scalar, store its field count.
    if (this.gqlIsObject) {
      mutation.setAttribute('gql_fields', this.itemCount_);
    }

    // Set parameters if they exist.
    for (var i = 0; i < this.gqlParameters.length; i++) {
      var gqlParameter = document.createElement('gql_parameter');

      // Add parameter attributes.
      gqlParameter.setAttribute('gql_name', this.gqlParameters[i].gqlName);
      gqlParameter.setAttribute('gql_type', this.gqlParameters[i].gqlType);

      // Add parameter to mutation.
      mutation.appendChild(gqlParameter);
    }

    return mutation;
  },

  domToMutation: function(xmlElement) {
    // Extract basic mutation attributes shared by all GraphQL blocks.
    this.gqlUrl = xmlElement.getAttribute('gql_url');
    this.gqlName = xmlElement.getAttribute('gql_name');
    this.gqlType = xmlElement.getAttribute('gql_type');

    // Determine whether the block is an object or a scalar.
    this.gqlIsObject = xmlElement.hasAttribute('gql_fields');

    // Get any parameters for this GraphQL block.
    var gqlParameterElements = xmlElement.getElementsByTagName('gql_parameter');
    this.gqlParameters = [];

    // Populate parameters if any exist.
    for (var i = 0; i < gqlParameterElements.length; i++) {
      this.gqlParameters.push({
        gqlName: gqlParameterElements[i].getAttribute('gql_name'),
        gqlType: gqlParameterElements[i].getAttribute('gql_type')
      });
    }

    // Store the type prefix of this block. Note that encoding the URL guarantees that there will be no spaces.
    this.gqlTypePrefix = encodeURI(this.gqlUrl) + ' ';

    // Set the color of the block to a beautiful GraphQL pink.
    this.setColour(Blockly.GraphQLBlock.PRIMARY_COLOR);

    // Add the title row.
    this.appendDummyInput('GQL_TITLE').appendField(this.gqlName, 'GQL_TITLE_FIELD');

    // Add any parameters if they exist.
    for (var i = 0; i < this.gqlParameters.length; i++) {
      // TODO(bobbyluig): Handle parameter type checks.
      this.appendValueInput('GQL_PARAMETER' + i)
        .appendField(this.gqlParameters[i].gqlName)
        .setAlign(Blockly.ALIGN_RIGHT)
        .setCheck(['String']);
    }

    // All GraphQL blocks have outputs with a return type that encapsulates their endpoint.
    this.setOutput(true, this.gqlTypePrefix + this.gqlName);

    // For non-scalar blocks, users should be able add and remove fields.
    if (this.gqlIsObject) {
      // Initialize required mutator parameters.
      this.emptyInputName = null;
      this.repeatingInputName = 'GQL_FIELD';
      this.itemCount_ = parseInt(xmlElement.getAttribute('gql_fields'));

      // Set mutator.
      this.setMutator(new Blockly.Mutator(['gql_mutator']));

      // Populate initial field value inputs.
      for (var i = 0; i < this.itemCount_; i++) {
        this.addInput(i);
      }
    }

    // Try to perform a schema update.
    this.updateSchema();
  },

  updateContainerBlock: function(containerBlock) {
    containerBlock.setFieldValue('object', 'CONTAINER_TEXT');
    containerBlock.setTooltip('Add, remove, or reorder fields to reconfigure this GraphQL block.');
  },

  compose: Blockly.compose,
  decompose: function(workspace) {
    return Blockly.decompose(workspace, 'gql_mutator', this);
  },

  saveConnections: Blockly.saveConnections,

  addEmptyInput: function() {
  },
  addInput: function(inputNumber) {
    return this.appendIndentedValueInput(this.repeatingInputName + inputNumber);
  },

  updateSchema: function() {
    // If there is no schema, we can't update yet.
    if (!Blockly.GraphQLBlock.schemas.hasOwnProperty(this.gqlUrl)) {
      return;
    }

    // Fetch the schema.
    var schema = Blockly.GraphQLBlock.schemas[this.gqlUrl];

    // Perform type existence check.
    if (!schema.types.hasOwnProperty(this.gqlType)) {
      console.log("The type " + this.gqlType + " no longer exists.");
      return;
    }

    // Get the type.
    var type = schema.types[this.gqlType];

    // Perform object/scalar type check.
    if (type.kind === 'OBJECT' && !this.gqlIsObject || this.kind === 'SCALAR' && this.gqlIsObject) {
      console.log("Scalar/object mismatch.");
      return;
    }

    // Get the title input.
    var titleInput = this.getInput('GQL_TITLE');

    // If we are an object, enable field autocompletion.
    if (this.gqlIsObject) {
      titleInput.removeField('GQL_TITLE_FIELD');
      titleInput.appendField(new Blockly.GqlFlydown(this.gqlName, this.gqlUrl, this.gqlType), 'GQL_TITLE_FIELD');
    }

    // Update description.
    this.setTooltip(type.description);

    // TODO(bobbyluig): Do type checks on parameter here.
    // TODO(bobbyluig): Update parameters and parameter types here.
  }
};

goog.require('AI.Blockly.FieldFlydown');

Blockly.GqlFlydown = function(name, gqlUrl, gqlType) {
  this.gqlUrl = gqlUrl;
  this.gqlType = gqlType;

  Blockly.GqlFlydown.superClass_.constructor.call(this, name, false, null);
};

goog.inherits(Blockly.GqlFlydown, Blockly.FieldFlydown);

Blockly.GqlFlydown.prototype.fieldCSSClassName = 'blocklyGqlField';
Blockly.GqlFlydown.prototype.flyoutCSSClassName = 'blocklyGqlFlydown';

Blockly.GqlFlydown.prototype.flydownBlocksXML_ = function() {
  // Fetch the associated type.
  var schema = Blockly.GraphQLBlock.schemas[this.gqlUrl];
  var type = schema.types[this.gqlType];

  // Create a new root element.
  var xml = document.createElement('xml');

  // Go through all fields for the type.
  for (var i = 0, field; field = type.fields[i]; i++) {
    // Create a new block.
    var block = document.createElement('block');
    block.setAttribute('type', 'gql');
    xml.appendChild(block);

    // Get field type.
    var fieldType = field.type;

    // Traverse type until we reach a base type.
    while (fieldType.kind === 'LIST' || fieldType.kind === 'NON_NULL') {
      fieldType = fieldType.ofType;
    }

    // Create a new mutation.
    var mutation = document.createElement('mutation');
    mutation.setAttribute('gql_url', this.gqlUrl);
    mutation.setAttribute('gql_type', fieldType.name);
    mutation.setAttribute('gql_name', field.name);
    block.appendChild(mutation);

    // If the field is an object, set its fields to 1.
    if (fieldType.kind === 'OBJECT') {
      mutation.setAttribute('gql_fields', '1');
    }

    // Add parameters into the mutation.
    for (var j = 0, arg; arg = field.args[j]; j++) {
      var gqlParameter = document.createElement('gql_parameter');

      // Get parameter type.
      var parameterType = arg.type;

      // Traverse type until we reach a base type.
      while (parameterType.kind === 'LIST' || parameterType.kind === 'NON_NULL') {
        parameterType = parameterType.ofType;
      }

      // Add parameter attributes.
      gqlParameter.setAttribute('gql_name', arg.name);
      gqlParameter.setAttribute('gql_type', parameterType.name);

      // Add parameter to mutation.
      mutation.appendChild(gqlParameter);
    }
  }

  // Return the string representation of the element.
  return xml.outerHTML;

  // TODO(bobbyluig): Handle interfaces.
  var xml = '<xml>\n' +
    '    <block type="gql">\n' +
    '        <mutation gql_url="https://graphql-pokemon.now.sh/" gql_name="person" gql_type="Person" gql_fields="1">\n' +
    '            <gql_parameter gql_name="id" gql_type="ID"/>\n' +
    '            <gql_parameter gql_name="personID" gql_type="ID"/>\n' +
    '        </mutation>\n' +
    '    </block>\n' +
    '    <block type="gql">\n' +
    '        <mutation gql_url="https://graphql-pokemon.now.sh/" gql_name="name" gql_type="String"></mutation>\n' +
    '    </block>\n' +
    '</xml>';
  return xml;
};


var xmlText = '<xml>\n' +
  '    <block type="gql">\n' +
  '        <mutation gql_url="https://graphql-pokemon.now.sh/" gql_name="pokemon" gql_type="Pokemon" gql_fields="1">\n' +
  '            <gql_parameter gql_name="id" gql_type="ID"/>\n' +
  '            <gql_parameter gql_name="personID" gql_type="ID"/>\n' +
  '        </mutation>\n' +
  '    </block>\n' +
  '</xml>';
var xmlBlockArray = [];
var xml = Blockly.Xml.textToDom(xmlText);
var children = goog.dom.getChildren(xml);
for (var i = 0; i < children.length; i++) {
  xmlBlockArray.push(children[i]);
}
Blockly.getMainWorkspace().flyout_.show(xmlBlockArray);
// Blockly.GraphQLBlock.updateSchema('https://graphql-pokemon.now.sh/')
