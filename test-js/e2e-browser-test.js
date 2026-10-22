/**
 * SortMaster End-to-End Automated Browser Tests
 * Controlled directly via Chrome DevTools Protocol (CDP) over WebSocket
 * Zero external npm dependencies (pure Node 20 + Google Chrome)
 */

const { spawn } = require('child_process');
const assert = require('assert');

const CHROME_PORT = 9222;
const APP_URL = 'http://localhost:8080';

class CdpClient {
  constructor(wsUrl) {
    this.ws = new WebSocket(wsUrl);
    this.id = 0;
    this.callbacks = new Map();
    this.pageErrors = [];

    this.ws.onmessage = (event) => {
      const msg = JSON.parse(event.data);
      if (msg.id && this.callbacks.has(msg.id)) {
        const { resolve, reject } = this.callbacks.get(msg.id);
        this.callbacks.delete(msg.id);
        if (msg.error) reject(new Error(msg.error.message));
        else resolve(msg.result);
      } else if (msg.method === 'Runtime.exceptionThrown') {
        this.pageErrors.push(msg.params.exceptionDetails);
      }
    };
  }

  static async connect(wsUrl) {
    const client = new CdpClient(wsUrl);
    await new Promise((resolve, reject) => {
      client.ws.onopen = resolve;
      client.ws.onerror = reject;
    });
    return client;
  }

  send(method, params = {}) {
    return new Promise((resolve, reject) => {
      const id = ++this.id;
      this.callbacks.set(id, { resolve, reject });
      this.ws.send(JSON.stringify({ id, method, params }));
    });
  }

  async eval(expr) {
    const res = await this.send('Runtime.evaluate', {
      expression: expr,
      returnByValue: true,
      awaitPromise: true
    });
    if (res.exceptionDetails) {
      throw new Error(`Eval error: ${JSON.stringify(res.exceptionDetails)}`);
    }
    return res.result ? res.result.value : undefined;
  }

  close() {
    this.ws.close();
  }
}

async function waitForPort(port, timeoutMs = 8000) {
  const start = Date.now();
  while (Date.now() - start < timeoutMs) {
    try {
      const res = await fetch(`http://127.0.0.1:${port}/json/version`);
      if (res.ok) return true;
    } catch (e) {
      // wait
    }
    await new Promise(r => setTimeout(r, 200));
  }
  throw new Error(`Timeout waiting for Chrome on port ${port}`);
}

async function runE2ETests() {
  console.log('--- Starting SortMaster E2E Browser Test Suite ---');
  let chromeProc;

  try {
    // 1. Launch Headless Google Chrome with clean isolated test profile
    console.log('Launching headless Google Chrome on port', CHROME_PORT);
    chromeProc = spawn('google-chrome', [
      '--headless=new',
      `--remote-debugging-port=${CHROME_PORT}`,
      '--disable-gpu',
      '--no-sandbox',
      '--disable-extensions',
      '--user-data-dir=/tmp/sortmaster-test-profile-' + Date.now(),
      '--disable-dev-shm-usage',
      APP_URL
    ]);

    chromeProc.on('error', (err) => console.error('Chrome spawn error:', err));

    await waitForPort(CHROME_PORT);

    // 2. Discover page target and connect CDP
    await new Promise(r => setTimeout(r, 1000));
    const listRes = await fetch(`http://127.0.0.1:${CHROME_PORT}/json/list`);
    const pages = await listRes.json();
    const targetPage = pages.find(p => p.type === 'page' && p.url.includes('localhost:8080')) || pages.find(p => p.type === 'page') || pages[0];
    const wsUrl = targetPage.webSocketDebuggerUrl;
    console.log('Connected to SortMaster page:', targetPage.title, wsUrl);

    const client = await CdpClient.connect(wsUrl);
    await client.send('Page.enable');
    await client.send('Runtime.enable');

    // 3. Navigate to SortMaster
    console.log(`Navigating to ${APP_URL}...`);
    await client.send('Page.navigate', { url: APP_URL });
    
    let title = '';
    for (let i = 0; i < 30; i++) {
      await new Promise(r => setTimeout(r, 300));
      try {
        const state = await client.eval('document.readyState');
        const t = await client.eval('document.title');
        if (state === 'complete' && t && t.includes('SortMaster')) {
          title = t;
          break;
        }
      } catch (e) {
        // wait
      }
    }

    console.log('Page Title:', title);
    assert(title.includes('SortMaster'), 'Page title should include SortMaster');

    const appReady = await client.eval('typeof window.App !== "undefined" && window.App.algorithms.length > 0');
    assert.strictEqual(appReady, true, 'App and 16 algorithms must be initialized');

    // =========================================================================
    // Requirement 6: Detailed Bubble Sort Verification
    // Array: [5, 2, 8, 1, 4]
    // =========================================================================
    console.log('\n--- Step 1-7: Testing Bubble Sort with [5, 2, 8, 1, 4] ---');

    // Switch to Visualizer view
    await client.eval('window.App.switchView("view-visualizer")');

    // Load custom array [5, 2, 8, 1, 4]
    console.log('Loading Bubble Sort on [5, 2, 8, 1, 4] in ASCENDING mode...');
    await client.eval(`(async () => {
      window.App.currentArray = [5, 2, 8, 1, 4];
      window.App.currentAlgorithmId = "bubble-sort";
      window.App.currentOrder = "ASCENDING";
      await window.App.loadAlgorithmForVisualization("bubble-sort");
    })()`);
    await new Promise(r => setTimeout(r, 400));

    // Jump to end to verify final array
    await client.eval('window.App.visualizer.jumpToEnd()');
    const finalAscArray = await client.eval(`
      Array.from(document.querySelectorAll('#visualizer-bars .v-block-val')).map(el => parseInt(el.textContent, 10))
    `);
    console.log('Visualizer final array (Ascending):', finalAscArray);
    assert.deepStrictEqual(finalAscArray, [1, 2, 4, 5, 8], 'Bubble Sort Ascending must yield [1, 2, 4, 5, 8]');

    // Descending Mode
    console.log('Running Bubble Sort in DESCENDING mode...');
    await client.eval('(async () => { await window.App.setOrder("DESCENDING"); })()');
    await new Promise(r => setTimeout(r, 400));

    await client.eval('window.App.visualizer.jumpToEnd()');
    const finalDescArray = await client.eval(`
      Array.from(document.querySelectorAll('#visualizer-bars .v-block-val')).map(el => parseInt(el.textContent, 10))
    `);
    console.log('Visualizer final array (Descending):', finalDescArray);
    assert.deepStrictEqual(finalDescArray, [8, 5, 4, 2, 1], 'Bubble Sort Descending must yield [8, 5, 4, 2, 1]');

    // =========================================================================
    // Step 8: Playback Controls: Pause, Resume, Step Backward, Reset, Restart
    // =========================================================================
    console.log('\n--- Step 8: Testing Animation Playback Controls ---');

    // Reset to start
    await client.eval('window.App.visualizer.resetToStart()');
    let currentStep = await client.eval('window.App.visualizer.currentStep');
    assert.strictEqual(currentStep, 0, 'Current step after reset must be 0');
    console.log('Reset to start verified: step = 0');

    // Step forward twice
    await client.eval('window.App.visualizer.stepForward()');
    await client.eval('window.App.visualizer.stepForward()');
    currentStep = await client.eval('window.App.visualizer.currentStep');
    assert.strictEqual(currentStep, 2, 'Current step after 2 steps forward must be 2');
    console.log('Step forward verified: step = 2');

    // Step backward once
    await client.eval('window.App.visualizer.stepBackward()');
    currentStep = await client.eval('window.App.visualizer.currentStep');
    assert.strictEqual(currentStep, 1, 'Current step after 1 step backward must be 1');
    console.log('Step backward verified: step = 1');

    // Play & Pause
    await client.eval('window.App.visualizer.play()');
    let isPlaying = await client.eval('window.App.visualizer.isPlaying');
    assert.strictEqual(isPlaying, true, 'Visualizer should be playing');
    await new Promise(r => setTimeout(r, 400));

    await client.eval('window.App.visualizer.pause()');
    isPlaying = await client.eval('window.App.visualizer.isPlaying');
    assert.strictEqual(isPlaying, false, 'Visualizer should be paused');
    const pausedStep = await client.eval('window.App.visualizer.currentStep');
    console.log(`Paused at step: ${pausedStep}`);
    assert(pausedStep >= 1, 'Paused step should be preserved');

    // Restart
    await client.eval('window.App.visualizer.restart()');
    isPlaying = await client.eval('window.App.visualizer.isPlaying');
    assert.strictEqual(isPlaying, true, 'Restart should commence playback');
    await client.eval('window.App.visualizer.pause()');
    console.log('Restart control verified successfully');

    // =========================================================================
    // Step 9: Verify All Other 15 Algorithms in Ascending & Descending
    // =========================================================================
    console.log('\n--- Step 9: Verifying All 16 Sorting Algorithms in Browser ---');
    const allAlgos = await client.eval('window.App.algorithms.map(a => a.id)');
    console.log(`Total registered algorithms to verify: ${allAlgos.length}`);

    const testInput = [19, 3, 42, 7, 12, 1, 28, 5];
    const expectedAsc = [...testInput].sort((a, b) => a - b);
    const expectedDesc = [...testInput].sort((a, b) => b - a);

    for (const algoId of allAlgos) {
      // Test Ascending
      await client.eval(`(async () => {
        window.App.currentArray = ${JSON.stringify(testInput)};
        window.App.currentAlgorithmId = "${algoId}";
        window.App.currentOrder = "ASCENDING";
        await window.App.loadAlgorithmForVisualization("${algoId}");
      })()`);
      await new Promise(r => setTimeout(r, 200));
      await client.eval('window.App.visualizer.jumpToEnd()');

      const ascResult = await client.eval(`
        Array.from(document.querySelectorAll('#visualizer-bars .v-block-val')).map(el => parseInt(el.textContent, 10))
      `);
      assert.deepStrictEqual(ascResult, expectedAsc, `Algorithm ${algoId} failed ascending browser verification`);

      // Test Descending
      await client.eval('(async () => { await window.App.setOrder("DESCENDING"); })()');
      await new Promise(r => setTimeout(r, 200));
      await client.eval('window.App.visualizer.jumpToEnd()');

      const descResult = await client.eval(`
        Array.from(document.querySelectorAll('#visualizer-bars .v-block-val')).map(el => parseInt(el.textContent, 10))
      `);
      assert.deepStrictEqual(descResult, expectedDesc, `Algorithm ${algoId} failed descending browser verification`);

      console.log(`✓ ${algoId}: Verified ASCENDING and DESCENDING in browser`);
    }

    // =========================================================================
    // Step 10: Check for any uncaught browser errors
    // =========================================================================
    console.log('\n--- Step 10: Checking for Uncaught Browser Errors ---');
    console.log('Total uncaught exceptions recorded:', client.pageErrors.length);
    if (client.pageErrors.length > 0) {
      console.error('Page errors encountered:', client.pageErrors);
    }
    assert.strictEqual(client.pageErrors.length, 0, 'There must be zero uncaught browser exceptions');

    client.close();
    console.log('\n🎉 ALL END-TO-END BROWSER TESTS PASSED SUCCESSFULLY! 🎉\n');
  } finally {
    if (chromeProc) {
      chromeProc.kill();
    }
  }
}

runE2ETests().catch((err) => {
  console.error('\n❌ E2E Test Failure:', err);
  process.exit(1);
});
