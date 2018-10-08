'use strict';

goog.provide('AI.Blockly.Blocks.dynamic');
goog.require('Blockly.Blocks.Utilities');

Blockly.Blocks['dynamic_gql'] = {
  category: 'Dynamic',

  mutationToDom: function() {
    var container = document.createElement('mutation');
  },

  domToMutation: function(xmlElement) {

  }
};