'use strict';

goog.provide('AI.Blockly.Blocks.gql');
goog.provide('AI.Blockly.GraphQL');
goog.require('Blockly.Blocks.Utilities');

// Initialize namespace.
Blockly.Blocks.gql = {};
Blockly.GraphQLBlock = {};

// Constants for GraphQL blocks.
Blockly.GraphQLBlock.PRIMARY_COLOR = '#e535ab';
Blockly.GraphQLBlock.SECONDARY_COLOR = '#161e26';

Blockly.Blocks['gql_field_mutator'] = {
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

  mutationToDom: function() {
    // Create a new mutation element to store data.
    var mutation = document.createElement('mutation');

    // Set basic attributes for this block.
    mutation.setAttribute('gql_url', this.gqlUrl);
    mutation.setAttribute('gql_name', this.gqlName);
    mutation.setAttribute('gql_type', this.gqlType);
    mutation.setAttribute('gql_scalar', this.gqlIsScalar);
    mutation.setAttribute('gql_fields', this.itemCount_);

    // Set parameters if they exist.
    for (var i = 0; i < this.gqlParameters.length; i++) {
      var gqlParameter = document.createElement('gql_parameter');

      // Set the name and type of the parameter.
      gqlParameter.setAttribute('gql_name', this.gqlParameters[i].gqlName);
      gqlParameter.setAttribute('gql_type', this.gqlParameters[i].gqlType);

      // Add parameter to mutation.
      mutation.appendChild(gqlParameter);
    }

    return mutation;
  },

  domToMutation: function(xmlElement) {
    // Extract basic block mutation attributes.
    this.gqlUrl = xmlElement.getAttribute('gql_url');
    this.gqlName = xmlElement.getAttribute('gql_name');
    this.gqlType = xmlElement.getAttribute('gql_type');
    this.gqlIsScalar = xmlElement.getAttribute('gql_scalar');
    this.itemCount_ = xmlElement.getAttribute('gql_fields') || 1;

    // Not all GraphQL blocks have parameters.
    this.gqlParameters = [];

    // Populate parameters if any exist.
    var gqlParameterElements = xmlElement.getElementsByTagName('gql_parameter');
    for (var i = 0; i < gqlParameterElements.length; i++) {
      this.gqlParameters.push({
        gqlName: gqlParameterElements[i].getAttribute('gql_name'),
        gqlType: gqlParameterElements[i].getAttribute('gql_type')
      });
    }

    // Stuff.
    this.repeatingInputName = 'GQL_FIELD';
    this.emptyInputName = null;

    // Store the type prefix of this block. Note that encoding the URL guarantees that there will be no spaces.
    this.gqlTypePrefix = encodeURI(this.gqlUrl) + ' ';

    // Set the color of the block to a beautiful GraphQL pink.
    this.setColour(Blockly.GraphQLBlock.PRIMARY_COLOR);

    // Add the title row.
    this.appendDummyInput('GQL_TITLE').appendField(this.gqlName);

    // Add any parameters if they exist.
    for (var i = 0; i < this.gqlParameters.length; i++) {
      this.appendValueInput('GQL_PARAMETER' + i)
        .appendField(this.gqlParameters[i].gqlName)
        .setAlign(Blockly.ALIGN_RIGHT)
        .setCheck([this.gqlTypePrefix + this.gqlName]);
    }

    // All GraphQL blocks have outputs with a return type that encapsulates their endpoint.
    this.setOutput(true, this.gqlTypePrefix + this.gqlName);

    // Non-scalar types have nested fields.
    if (this.gqlIsScalar !== 'true') {
      this.addInput(0);
      this.addInput(1);
      this.setMutator(new Blockly.Mutator(['gql_field_mutator']));
    }
  },

  compose: Blockly.compose,
  saveConnections: Blockly.saveConnections,
  decompose: function(workspace) {
    return Blockly.decompose(workspace, 'gql_field_mutator', this);
  },
  addEmptyInput: function() {
  },
  addInput: function(inputNumber) {
    var input = this.appendIndentedValueInput(this.repeatingInputName + inputNumber);
    input.setCheck([this.gqlTypePrefix + this.gqlName]).setAlign(Blockly.ALIGN_LEFT);
    return input;
  }
};

var xmlText = '<xml>\n' +
  '    <block type="gql">\n' +
  '        <mutation gql_url="https://google.com" gql_name="someTypeOfThing" gql_type="nothing">\n' +
  '            <gql_parameter gql_name="parameter1" gql_type="String"/>\n' +
  '            <gql_parameter gql_name="parameter2" gql_type="String"/>\n' +
  '            <gql_parameter gql_name="parameter3" gql_type="String"/>\n' +
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
