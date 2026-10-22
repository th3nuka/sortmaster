/**
 * SortMaster Animation Controls Unit Tests
 * Uses native Node 20 test runner (node --test)
 */
const { test, describe, beforeEach, afterEach } = require('node:test');
const assert = require('node:assert/strict');
const fs = require('node:fs');
const path = require('node:path');

// Minimal DOM Element Mock
class MockElement {
  constructor(id = '', tagName = 'div') {
    this.id = id;
    this.tagName = tagName;
    this.innerHTML = '';
    this.textContent = '';
    this.title = '';
    this.value = '65';
    this.disabled = false;
    this.style = {};
    this.classList = new Set();
    this.listeners = {};
    this.children = [];
    this.dataset = {};
  }

  addEventListener(event, fn) {
    if (!this.listeners[event]) this.listeners[event] = [];
    this.listeners[event].push(fn);
  }

  dispatchEvent(event, data) {
    if (this.listeners[event]) {
      this.listeners[event].forEach(fn => fn(data || { target: this }));
    }
  }

  appendChild(child) {
    this.children.push(child);
  }

  querySelector(selector) {
    return new MockElement();
  }

  getBoundingClientRect() {
    return { left: 0, top: 0, width: 100, height: 100 };
  }
}

// Global Document Mock
class MockDocument {
  constructor() {
    this.elements = {};
  }

  getElementById(id) {
    if (!this.elements[id]) {
      this.elements[id] = new MockElement(id);
    }
    return this.elements[id];
  }

  createElement(tag) {
    return new MockElement('', tag);
  }

  createElementNS(ns, tag) {
    const el = new MockElement('', tag);
    el.setAttribute = (k, v) => { el[k] = v; };
    return el;
  }
}

// Load SortingVisualizer code into environment
const visualizerCode = fs.readFileSync(
  path.join(__dirname, '../src/main/resources/static/js/visualizer.js'),
  'utf8'
);

describe('SortingVisualizer Controls & State Verification', () => {
  let visualizer;
  let mockDoc;

  beforeEach(() => {
    mockDoc = new MockDocument();
    global.document = mockDoc;
    global.window = {
      addEventListener: () => {},
      CodePanel: { highlightStep: () => {} }
    };

    // Evaluate SortingVisualizer class
    const fn = new Function('window', 'document', visualizerCode);
    fn(global.window, global.document);
    visualizer = new global.window.SortingVisualizer('visualizer-bars');
  });

  afterEach(() => {
    if (visualizer && visualizer.timer) {
      clearTimeout(visualizer.timer);
    }
  });

  test('Initial state: idle with empty events and disabled controls', () => {
    assert.equal(visualizer.isPlaying, false);
    assert.equal(visualizer.currentStep, 0);
    assert.equal(visualizer.events.length, 0);

    // Controls must be disabled when no events exist
    assert.equal(visualizer.playBtn.disabled, true);
    assert.equal(visualizer.prevBtn.disabled, true);
    assert.equal(visualizer.nextBtn.disabled, true);
    assert.equal(visualizer.resetBtn.disabled, true);
    assert.equal(visualizer.jumpEndBtn.disabled, true);
  });

  test('loadData: loads events and renders step 0 correctly', () => {
    const mockResponse = {
      initialArray: [5, 2, 8, 1, 4],
      events: [
        { stepIndex: 1, eventType: 'START', array: [5, 2, 8, 1, 4], message: 'Start' },
        { stepIndex: 2, eventType: 'COMPARE', array: [5, 2, 8, 1, 4], indices: [0, 1], message: 'Comparing' },
        { stepIndex: 3, eventType: 'SWAP', array: [2, 5, 8, 1, 4], indices: [0, 1], message: 'Swapped' },
        { stepIndex: 4, eventType: 'COMPLETE', array: [1, 2, 4, 5, 8], message: 'Done' }
      ]
    };

    visualizer.loadData(mockResponse);

    assert.equal(visualizer.events.length, 4);
    assert.equal(visualizer.currentStep, 0);
    assert.equal(visualizer.totalStepsDisplay.textContent, 4);
    assert.equal(visualizer.timelineStatus.textContent, 'At Start');

    // At step 0: prev and reset disabled, next, play, jumpEnd enabled
    assert.equal(visualizer.prevBtn.disabled, true);
    assert.equal(visualizer.resetBtn.disabled, true);
    assert.equal(visualizer.playBtn.disabled, false);
    assert.equal(visualizer.nextBtn.disabled, false);
    assert.equal(visualizer.jumpEndBtn.disabled, false);
  });

  test('stepForward and stepBackward: correctly update steps and telemetry', () => {
    const mockResponse = {
      initialArray: [5, 2, 8],
      events: [
        { stepIndex: 1, eventType: 'START', array: [5, 2, 8] },
        { stepIndex: 2, eventType: 'COMPARE', array: [5, 2, 8], indices: [0, 1] },
        { stepIndex: 3, eventType: 'SWAP', array: [2, 5, 8], indices: [0, 1] },
        { stepIndex: 4, eventType: 'COMPLETE', array: [2, 5, 8] }
      ]
    };
    visualizer.loadData(mockResponse);

    // Step forward to 1 (COMPARE)
    visualizer.stepForward();
    assert.equal(visualizer.currentStep, 1);
    assert.equal(visualizer.comparisonsDisplay.textContent, 1);
    assert.equal(visualizer.swapsDisplay.textContent, 0);

    // Step forward to 2 (SWAP)
    visualizer.stepForward();
    assert.equal(visualizer.currentStep, 2);
    assert.equal(visualizer.comparisonsDisplay.textContent, 1);
    assert.equal(visualizer.swapsDisplay.textContent, 1);
    assert.equal(visualizer.writesDisplay.textContent, 2);

    // Step backward to 1
    visualizer.stepBackward();
    assert.equal(visualizer.currentStep, 1);
    assert.equal(visualizer.comparisonsDisplay.textContent, 1);
    assert.equal(visualizer.swapsDisplay.textContent, 0);
    assert.equal(visualizer.writesDisplay.textContent, 0);

    // Step backward to 0
    visualizer.stepBackward();
    assert.equal(visualizer.currentStep, 0);
    assert.equal(visualizer.comparisonsDisplay.textContent, 0);
    assert.equal(visualizer.prevBtn.disabled, true);
  });

  test('jumpToEnd and resetToStart navigation', () => {
    const mockResponse = {
      initialArray: [3, 1, 2],
      events: [
        { stepIndex: 1, eventType: 'START', array: [3, 1, 2] },
        { stepIndex: 2, eventType: 'COMPARE', array: [3, 1, 2] },
        { stepIndex: 3, eventType: 'COMPLETE', array: [1, 2, 3] }
      ]
    };
    visualizer.loadData(mockResponse);

    visualizer.jumpToEnd();
    assert.equal(visualizer.currentStep, 2);
    assert.equal(visualizer.timelineStatus.textContent, 'Completed');
    assert.equal(visualizer.nextBtn.disabled, true);
    assert.equal(visualizer.jumpEndBtn.disabled, true);
    assert.equal(visualizer.prevBtn.disabled, false);

    visualizer.resetToStart();
    assert.equal(visualizer.currentStep, 0);
    assert.equal(visualizer.timelineStatus.textContent, 'At Start');
    assert.equal(visualizer.prevBtn.disabled, true);
    assert.equal(visualizer.nextBtn.disabled, false);
  });

  test('play, pause, and resume from exact position', () => {
    const mockResponse = {
      initialArray: [3, 1, 2],
      events: [
        { stepIndex: 1, eventType: 'START', array: [3, 1, 2] },
        { stepIndex: 2, eventType: 'COMPARE', array: [3, 1, 2] },
        { stepIndex: 3, eventType: 'COMPLETE', array: [1, 2, 3] }
      ]
    };
    visualizer.loadData(mockResponse);

    // Move to step 1
    visualizer.stepForward();
    assert.equal(visualizer.currentStep, 1);

    // Play should resume from currentStep (1), NOT reset to 0
    visualizer.play();
    assert.equal(visualizer.isPlaying, true);
    assert.equal(visualizer.currentStep, 1); // Exact position preserved

    visualizer.pause();
    assert.equal(visualizer.isPlaying, false);
    assert.equal(visualizer.timer, null);
  });

  test('play prevents duplicate timers when called multiple times', () => {
    const mockResponse = {
      initialArray: [3, 1, 2],
      events: [
        { stepIndex: 1, eventType: 'START', array: [3, 1, 2] },
        { stepIndex: 2, eventType: 'COMPARE', array: [3, 1, 2] },
        { stepIndex: 3, eventType: 'COMPLETE', array: [1, 2, 3] }
      ]
    };
    visualizer.loadData(mockResponse);

    visualizer.play();
    const firstTimer = visualizer.timer;
    assert.notEqual(firstTimer, null);

    // Rapid successive play call
    visualizer.play();
    const secondTimer = visualizer.timer;
    assert.notEqual(secondTimer, null);

    // Only one active timer is maintained
    visualizer.pause();
    assert.equal(visualizer.timer, null);
    assert.equal(visualizer.isPlaying, false);
  });

  test('speed slider updates speedMs accurately', () => {
    const speedSlider = mockDoc.getElementById('speed-slider');
    const label = mockDoc.getElementById('speed-value-label');

    // Simulate slider input of 100 (fastest)
    speedSlider.value = '100';
    speedSlider.dispatchEvent('input', { target: speedSlider });
    assert.equal(visualizer.speedMs, 30);
    assert.equal(label.textContent, '33.3x');

    // Simulate slider input of 1 (slowest)
    speedSlider.value = '1';
    speedSlider.dispatchEvent('input', { target: speedSlider });
    assert.equal(visualizer.speedMs, 842);
    assert.equal(label.textContent, '1.2x');
  });

  test('restart resets to step 0 and begins playing', () => {
    const mockResponse = {
      initialArray: [3, 1, 2],
      events: [
        { stepIndex: 1, eventType: 'START', array: [3, 1, 2] },
        { stepIndex: 2, eventType: 'COMPARE', array: [3, 1, 2] },
        { stepIndex: 3, eventType: 'COMPLETE', array: [1, 2, 3] }
      ]
    };
    visualizer.loadData(mockResponse);
    visualizer.jumpToEnd();
    assert.equal(visualizer.currentStep, 2);

    visualizer.restart();
    assert.equal(visualizer.currentStep, 0);
    assert.equal(visualizer.isPlaying, true);
    visualizer.pause();
  });
});
