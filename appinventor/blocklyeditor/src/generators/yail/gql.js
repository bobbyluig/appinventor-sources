'use strict';

goog.provide('Blockly.Yail.gql');

// Code generator for GraphQL blocks.
Blockly.Yail['gql'] = function() {
  // If the blocks is a scalar, then the code is just the field name.
  if (!this.gqlIsObject) {
    return [Blockly.Yail.quote_(this.gqlName), Blockly.Yail.ORDER_ATOMIC];
  }

  // Keep track of an array of items to concatenate.
  var combination = [];

  // The first item in the list is the list constructor, which is not part of the query string.
  combination.push(Blockly.Yail.YAIL_LIST_CONSTRUCTOR);

  // The field name and opening bracket is the first query element.
  combination.push(Blockly.Yail.quote_(this.gqlName + ' {\n'));

  // Add all object fields.
  for (var i = 0; i < this.itemCount_; i++) {
    combination.push('"  "');
    combination.push(Blockly.Yail.valueToCode(this, 'GQL_FIELD' + i, Blockly.Yail.ORDER_NONE) || '""');
    combination.push('"\n"');
  }

  // Begin generating string concatenation code.
  var code = Blockly.Yail.YAIL_CALL_YAIL_PRIMITIVE + "string-append" + Blockly.Yail.YAIL_SPACER;

  // Add all items in the combination.
  code += Blockly.Yail.YAIL_OPEN_COMBINATION;
  code += combination.join(Blockly.Yail.YAIL_SPACER);
  code += Blockly.Yail.YAIL_CLOSE_COMBINATION;

  // Prepare to create a new combination (for type coercions).
  code += Blockly.Yail.YAIL_SPACER + Blockly.Yail.YAIL_QUOTE;

  // Reuse the combination array by placing the text type into every entry.
  for (var i = 0; i < this.itemCount_; i++) {
    combination[i] = 'text';
  }

  // Create a combination for text type coercions of all elements in the query.
  code += Blockly.Yail.YAIL_OPEN_COMBINATION;
  code += combination.join(Blockly.Yail.YAIL_SPACER);
  code += Blockly.Yail.YAIL_CLOSE_COMBINATION;

  // Indicate that we are performing a join.
  code += Blockly.Yail.YAIL_SPACER + Blockly.Yail.YAIL_DOUBLE_QUOTE + "join" + Blockly.Yail.YAIL_DOUBLE_QUOTE;

  // Close final combination.
  code += Blockly.Yail.YAIL_CLOSE_COMBINATION;

  // Return code.
  return [code, Blockly.Yail.ORDER_ATOMIC];
};

