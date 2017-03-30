const angular = require('angular');
import Phylocanvas from 'phylocanvas';
import metadataPlugin from 'phylocanvas-plugin-metadata';
import {METADATA, TREE} from './../../constants';

const PHYLOCANVAS_DIV = 'phylocanvas';
const BOTTOM_PADDING = 250;

Phylocanvas.plugin(metadataPlugin);

const metadataFormat = {
  showHeaders: true,
  showLabels: true,
  blockLength: 32,
  blockSize: 32,
  padding: 25,
  columns: [],
  propertyName: 'data',
  underlineHeaders: true,
  headerAngle: 0,
  fillStyle: 'black',
  strokeStyle: 'black',
  lineWidth: 1
};

const setCanvasHeight = $window => {
  const canvas = document.querySelector(`#${PHYLOCANVAS_DIV}`);
  canvas.style.height = `${$window.innerHeight - BOTTOM_PADDING}px`;
};

/**
 * Angular controller function for this scope.
 * @param {object} $window AngularJS window object
 * @param {object} $scope AngularJS $scope object for current dom
 * @param {object} $q AngularJS promise object
 * @param {object} PhylocanvasService angular service for server exchanges for newick data
 */
function phylocanvasController($window, $scope, $q, PhylocanvasService) {
  // Make the canvas fill the viewable window.
  setCanvasHeight($window);

  // Initialize phylocanvas.
  const tree = Phylocanvas
    .createTree(PHYLOCANVAS_DIV, {
      metadata: metadataFormat
    });

  const promises = [];

  // Create a promise for when the metadata is given.
  const metadataPromise = $q.defer();
  promises.push(metadataPromise.promise);

  this.$onInit = () => {
    /**
     * Kick everything off by getting the newick file and the
     * initial metadata.
     */
    const newickPromise = PhylocanvasService
      .getNewickData(this.newickurl)
      .then(result => result, () => {
        $scope.$emit(TREE.NOT_LOADED);
      });
    promises.push(newickPromise);

    // Create the tree when the promises complete.
    $q
      .all(promises)
      .then(results => {
        const newick = results[1];
        if (angular.isString(newick)) {
          tree.load(newick);
        }
        $scope.$emit(TREE.COMPLETED);
      });
  };

  // Set tree defaults
  tree.setTreeType('rectangular');
  tree.alignLabels = true;

  /**
   * Listen for changes to the metadata structure and update
   * the phylocanvas accordingly.
   */
  $scope.$on(METADATA.UPDATED, (event, args) => {
    if (args.columns.length > 0) {
      tree.metadata.active = true;
      tree.metadata.columns = args.columns;
    } else {
      tree.metadata.active = false;
    }
    tree.draw();
    tree.fitInPanel();
    tree.draw();
  });

  $scope.$on(METADATA.EMPTY, () => {
    metadataPromise.resolve();
  });

  $scope.$on(METADATA.LOADED, (event, args) => {
    const {metadata} = args;
    // Add the metadata to the branches before the tree is drawn.
    tree.on('beforeFirstDraw', () => {
      for (const leaf of tree.leaves) {
        leaf.data = metadata[leaf.label];
      }
    });
    metadataPromise.resolve();
  });
}

phylocanvasController.$inject = [
  '$window',
  '$scope',
  '$q',
  'PhylocanvasService'
];

export const PhylocanvasComponent = {
  bindings: {
    newickurl: '@'
  },
  templateUrl: 'phylocanvas.tmpl.html',
  controller: phylocanvasController
};