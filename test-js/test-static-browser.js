const http = require('http');
const fs = require('fs');
const path = require('path');
const { spawn } = require('child_process');
const assert = require('assert');

const ROOT_DIR = path.resolve(__dirname, '..');
const PORT = 5050;

// Simple static HTTP server (No Spring Boot backend!)
const server = http.createServer((req, res) => {
  let reqPath = req.url.split('?')[0];
  if (reqPath === '/' || reqPath === '') reqPath = '/index.html';
  const filePath = path.join(ROOT_DIR, reqPath);

  if (fs.existsSync(filePath) && fs.statSync(filePath).isFile()) {
    const ext = path.extname(filePath);
    const mimeTypes = {
      '.html': 'text/html',
      '.js': 'text/javascript',
      '.css': 'text/css',
      '.json': 'application/json',
      '.svg': 'image/svg+xml'
    };
    res.writeHead(200, { 'Content-Type': mimeTypes[ext] || 'text/plain' });
    fs.createReadStream(filePath).pipe(res);
  } else {
    res.writeHead(404);
    res.end('Not Found');
  }
});

server.listen(PORT, async () => {
  console.log(`Static server running on http://localhost:${PORT} (Zero backend)`);
  let chromeProc;
  try {
    const CHROME_PORT = 9225;
    chromeProc = spawn('google-chrome', [
      '--headless=new',
      `--remote-debugging-port=${CHROME_PORT}`,
      '--disable-gpu',
      '--no-sandbox',
      '--disable-extensions',
      `--user-data-dir=/tmp/static-test-profile-${Date.now()}`,
      `http://localhost:${PORT}`
    ]);

    // Wait for Chrome port
    await new Promise(r => setTimeout(r, 1500));
    const listRes = await fetch(`http://127.0.0.1:${CHROME_PORT}/json/list`);
    const pages = await listRes.json();
    const targetPage = pages.find(p => p.type === 'page' && !p.url.startsWith('chrome')) || pages[0];
    const ws = new WebSocket(targetPage.webSocketDebuggerUrl);

    let id = 0;
    const callbacks = new Map();
    const send = (method, params = {}) => new Promise((resolve, reject) => {
      const msgId = ++id;
      callbacks.set(msgId, { resolve, reject });
      ws.send(JSON.stringify({ id: msgId, method, params }));
    });

    ws.onmessage = (event) => {
      const msg = JSON.parse(event.data);
      if (msg.id && callbacks.has(msg.id)) {
        const { resolve } = callbacks.get(msg.id);
        callbacks.delete(msg.id);
        resolve(msg.result);
      }
    };

    await new Promise(resolve => ws.onopen = resolve);
    const evalJs = async (expr) => {
      const res = await send('Runtime.evaluate', { expression: expr, returnByValue: true, awaitPromise: true });
      return res.result ? res.result.value : undefined;
    };

    console.log('Verifying static page load...');
    await new Promise(r => setTimeout(r, 1000));
    const title = await evalJs('document.title');
    console.log('Static Page Title:', title);
    assert(title.includes('SortMaster'));

    // Check App loaded algorithms statically
    const count = await evalJs('window.App.algorithms.length');
    console.log(`Loaded ${count} algorithms statically!`);
    assert.strictEqual(count, 16);

    // Switch to visualizer view
    await evalJs('window.App.switchView("view-visualizer")');

    // Test Bubble sort [5, 2, 8, 1, 4] ascending & descending
    console.log('Testing Bubble Sort on purely static site...');
    await evalJs(`(async () => {
      window.App.currentArray = [5, 2, 8, 1, 4];
      window.App.currentAlgorithmId = "bubble-sort";
      window.App.currentOrder = "ASCENDING";
      await window.App.loadAlgorithmForVisualization("bubble-sort");
    })()`);
    await new Promise(r => setTimeout(r, 500));
    await evalJs('window.App.visualizer.jumpToEnd()');
    const ascBars = await evalJs(`Array.from(document.querySelectorAll('#visualizer-bars .v-block-val')).map(el => parseInt(el.textContent, 10))`);
    console.log('Ascending result:', ascBars);
    assert.deepStrictEqual(ascBars, [1, 2, 4, 5, 8]);

    await evalJs(`(async () => { await window.App.setOrder("DESCENDING"); })()`);
    await new Promise(r => setTimeout(r, 500));
    await evalJs('window.App.visualizer.jumpToEnd()');
    const descBars = await evalJs(`Array.from(document.querySelectorAll('#visualizer-bars .v-block-val')).map(el => parseInt(el.textContent, 10))`);
    console.log('\nVerifying all 16 algorithms on purely static site...');
    const allAlgos = await evalJs('window.App.algorithms.map(a => a.id)');
    const testInput = [19, 3, 42, 7, 12, 1, 28, 5];
    const expectedAsc = [...testInput].sort((a, b) => a - b);
    const expectedDesc = [...testInput].sort((a, b) => b - a);

    for (const algoId of allAlgos) {
      // Ascending
      await evalJs(`(async () => {
        window.App.currentArray = ${JSON.stringify(testInput)};
        window.App.currentAlgorithmId = "${algoId}";
        window.App.currentOrder = "ASCENDING";
        await window.App.loadAlgorithmForVisualization("${algoId}");
      })()`);
      await new Promise(r => setTimeout(r, 200));
      await evalJs('window.App.visualizer.jumpToEnd()');
      const asc = await evalJs(`Array.from(document.querySelectorAll('#visualizer-bars .v-block-val')).map(el => parseInt(el.textContent, 10))`);
      assert.deepStrictEqual(asc, expectedAsc, `${algoId} failed static ascending`);

      // Descending
      await evalJs(`(async () => { await window.App.setOrder("DESCENDING"); })()`);
      await new Promise(r => setTimeout(r, 200));
      await evalJs('window.App.visualizer.jumpToEnd()');
      const desc = await evalJs(`Array.from(document.querySelectorAll('#visualizer-bars .v-block-val')).map(el => parseInt(el.textContent, 10))`);
      assert.deepStrictEqual(desc, expectedDesc, `${algoId} failed static descending`);

      console.log(`✓ ${algoId}: Passed static Ascending and Descending in browser`);
    }

    console.log('\n🎉 ALL 16 ALGORITHMS VERIFIED ON 100% STATIC SITE! 🎉\n');
    ws.close();
  } catch (err) {
    console.error('Static verification error:', err);
    process.exitCode = 1;
  } finally {
    if (chromeProc) chromeProc.kill();
    server.close();
  }
});
