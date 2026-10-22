/**
 * SortMaster Synchronized Source-Code Panel
 * Supports Java, Python, and Pseudocode with Syntax Highlighting,
 * Line Numbers, Synchronized Execution Highlighting, Copy Code, and Expand/Collapse.
 */

const CodePanel = {
  currentMetadata: null,
  activeLanguage: 'java', // 'java', 'python', 'pseudo'
  currentOrder: 'ASCENDING',
  isExpanded: false,

  init() {
    // Language tabs
    const javaTab = document.getElementById('tab-java-code');
    const pythonTab = document.getElementById('tab-python-code');
    const pseudoTab = document.getElementById('tab-pseudo-code');

    const tabs = [
      { el: javaTab, lang: 'java' },
      { el: pythonTab, lang: 'python' },
      { el: pseudoTab, lang: 'pseudo' }
    ];

    tabs.forEach(({ el, lang }) => {
      if (el) {
        el.addEventListener('click', () => {
          this.setLanguage(lang);
        });
      }
    });

    // Copy Code button
    const copyBtn = document.getElementById('btn-copy-code');
    if (copyBtn) {
      copyBtn.addEventListener('click', () => this.copyCurrentCode());
    }

    // Expand / Collapse button
    const expandBtn = document.getElementById('btn-toggle-expand-code');
    if (expandBtn) {
      expandBtn.addEventListener('click', () => this.toggleExpand());
    }
  },

  setLanguage(lang) {
    this.activeLanguage = lang;

    const javaTab = document.getElementById('tab-java-code');
    const pythonTab = document.getElementById('tab-python-code');
    const pseudoTab = document.getElementById('tab-pseudo-code');

    if (javaTab) {
      javaTab.classList.toggle('active', lang === 'java');
      javaTab.setAttribute('aria-selected', lang === 'java');
    }
    if (pythonTab) {
      pythonTab.classList.toggle('active', lang === 'python');
      pythonTab.setAttribute('aria-selected', lang === 'python');
    }
    if (pseudoTab) {
      pseudoTab.classList.toggle('active', lang === 'pseudo');
      pseudoTab.setAttribute('aria-selected', lang === 'pseudo');
    }

    this.render();
  },

  setOrder(order) {
    this.currentOrder = order ? order.toUpperCase() : 'ASCENDING';
    this.updateTitle();
    this.render();
  },

  loadMetadata(metadata) {
    this.currentMetadata = metadata;
    this.updateTitle();
    this.render();
  },

  updateTitle() {
    const titleEl = document.getElementById('code-algo-title');
    if (titleEl && this.currentMetadata) {
      const orderLabel = this.currentOrder === 'DESCENDING' ? ' (Descending)' : ' (Ascending)';
      titleEl.textContent = `${this.currentMetadata.name}${orderLabel} Implementation`;
    }
  },

  getRawCode() {
    if (!this.currentMetadata) return '// Select an algorithm to view source code';
    const isDesc = this.currentOrder === 'DESCENDING';

    if (this.activeLanguage === 'python') {
      if (isDesc && this.currentMetadata.pythonCodeDescending) {
        return this.currentMetadata.pythonCodeDescending;
      }
      return this.currentMetadata.pythonCode || '# Python implementation loading...';
    } else if (this.activeLanguage === 'pseudo') {
      if (isDesc && this.currentMetadata.pseudocodeDescending) {
        return this.currentMetadata.pseudocodeDescending;
      }
      return this.currentMetadata.pseudocode || '// Pseudocode representation loading...';
    } else {
      if (isDesc && this.currentMetadata.javaCodeDescending) {
        return this.currentMetadata.javaCodeDescending;
      }
      return this.currentMetadata.javaCode || '// Java implementation loading...';
    }
  },

  render() {
    const displayContainer = document.getElementById('code-display');
    const explanationText = document.getElementById('code-explanation-text');
    if (!displayContainer || !this.currentMetadata) return;

    displayContainer.innerHTML = '';
    const rawCode = this.getRawCode();
    const lines = rawCode.trim().split('\n');

    lines.forEach((lineText, idx) => {
      const lineEl = document.createElement('div');
      lineEl.className = 'code-line';
      lineEl.dataset.lineNum = idx + 1;

      const numEl = document.createElement('span');
      numEl.className = 'line-num';
      numEl.textContent = idx + 1;

      const contentEl = document.createElement('span');
      contentEl.className = 'line-content';
      contentEl.innerHTML = this.highlightSyntax(lineText, this.activeLanguage);

      lineEl.appendChild(numEl);
      lineEl.appendChild(contentEl);
      displayContainer.appendChild(lineEl);
    });

    if (explanationText) {
      explanationText.textContent = `Viewing ${this.getLanguageDisplayName()} implementation. Step through animation to observe synchronized line execution.`;
    }
  },

  getLanguageDisplayName() {
    if (this.activeLanguage === 'python') return 'Python 3';
    if (this.activeLanguage === 'pseudo') return 'Pseudocode';
    return 'Java 21';
  },

  escapeHtml(str) {
    if (!str) return '';
    return str
      .replace(/&/g, '&amp;')
      .replace(/</g, '&lt;')
      .replace(/>/g, '&gt;');
  },

  highlightSyntax(line, lang) {
    let result = '';
    let lastIndex = 0;

    let tokenRegex;
    if (lang === 'python') {
      tokenRegex = /(#.*$)|("""[\s\S]*?"""|'''[\s\S]*?'''|"(?:\\.|[^"\\])*"|'(?:\\.|[^'\\])*')|\b(def|return|for|in|while|if|elif|else|break|continue|pass|import|from|class|lambda|with|as|and|or|not|is|None|True|False)\b|\b(range|len|min|max|list|int|float|str|print|reversed|enumerate|List|Dict|Set)\b|\b(\d+)\b/g;
    } else if (lang === 'java') {
      tokenRegex = /(\/\/.*$|\/\*[\s\S]*?\*\/)|("(?:\\.|[^"\\])*"|'(?:\\.|[^'\\])*')|\b(public|private|protected|static|final|class|void|int|boolean|double|for|while|if|else|return|new|break|continue)\b|\b(Math|System|String|List|Arrays|Collections|out|println|min|max|floor)\b|\b(\d+)\b/g;
    } else {
      tokenRegex = /(\/\/.*$)|\b(procedure|end|for|to|downto|do|while|if|then|else|return|swap|and|or|not)\b|\b(\d+)\b/gi;
    }

    let match;
    while ((match = tokenRegex.exec(line)) !== null) {
      if (match.index > lastIndex) {
        result += this.escapeHtml(line.slice(lastIndex, match.index));
      }

      if (match[1]) {
        // Comment
        result += `<span class="syn-cmt">${this.escapeHtml(match[1])}</span>`;
      } else if (match[2]) {
        // String literal
        result += `<span class="syn-str">${this.escapeHtml(match[2])}</span>`;
      } else if (match[3]) {
        // Keyword
        result += `<span class="syn-kw">${this.escapeHtml(match[3])}</span>`;
      } else if (match[4]) {
        // Builtin / Function / Type
        result += `<span class="syn-fn">${this.escapeHtml(match[4])}</span>`;
      } else if (match[5]) {
        // Number
        result += `<span class="syn-num">${this.escapeHtml(match[5])}</span>`;
      } else {
        result += this.escapeHtml(match[0]);
      }
      lastIndex = tokenRegex.lastIndex;
    }

    if (lastIndex < line.length) {
      result += this.escapeHtml(line.slice(lastIndex));
    }

    return result;
  },

  highlightStep(conceptualStep) {
    if (!this.currentMetadata) return;

    const displayContainer = document.getElementById('code-display');
    const explanationText = document.getElementById('code-explanation-text');
    if (!displayContainer) return;

    // Clear previous line highlights
    displayContainer.querySelectorAll('.code-line').forEach(el => el.classList.remove('highlighted'));

    if (!conceptualStep) return;

    const lines = displayContainer.querySelectorAll('.code-line');
    let highlightedCount = 0;
    const stepKey = conceptualStep.toLowerCase();

    lines.forEach(lineEl => {
      const text = lineEl.textContent.toLowerCase();
      let match = false;

      if (stepKey.includes('compare')) {
        if (text.includes('>') || text.includes('<') || text.includes('<=') || text.includes('>=') || text.includes('compare')) {
          match = true;
        }
      } else if (stepKey.includes('swap')) {
        if (text.includes('swap') || text.includes('temp') || text.includes('a[j], a[j + 1]') || text.includes('arr[i], arr[min_idx]')) {
          match = true;
        }
      } else if (stepKey.includes('pivot') || stepKey.includes('partition')) {
        if (text.includes('pivot') || text.includes('partition') || text.includes('low') || text.includes('high')) {
          match = true;
        }
      } else if (stepKey.includes('merge')) {
        if (text.includes('merge') || text.includes('left') || text.includes('right') || text.includes('mid')) {
          match = true;
        }
      } else if (stepKey.includes('shift') || stepKey.includes('insert')) {
        if (text.includes('key') || text.includes('j - 1') || text.includes('gap') || text.includes('arr[j + 1] = arr[j]')) {
          match = true;
        }
      } else if (stepKey.includes('heap')) {
        if (text.includes('heap') || text.includes('largest') || text.includes('left') || text.includes('right')) {
          match = true;
        }
      } else if (stepKey.includes('count') || stepKey.includes('bucket') || stepKey.includes('radix')) {
        if (text.includes('count') || text.includes('bucket') || text.includes('exp') || text.includes('output')) {
          match = true;
        }
      }

      if (match && highlightedCount < 2) {
        lineEl.classList.add('highlighted');
        highlightedCount++;

        // Keep highlighted line visible inside code container without hijacking page scroll
        const container = document.getElementById('code-block-container');
        if (container) {
          const lineTop = lineEl.offsetTop - container.offsetTop;
          const containerScroll = container.scrollTop;
          const containerHeight = container.clientHeight;
          if (lineTop < containerScroll || lineTop > (containerScroll + containerHeight - 32)) {
            container.scrollTop = Math.max(0, lineTop - Math.floor(containerHeight / 3));
          }
        }
      }
    });

    // Update explanation message
    if (explanationText && this.currentMetadata.conceptualLineExplanations) {
      const expl = this.currentMetadata.conceptualLineExplanations[conceptualStep]
        || `Executing operation: ${conceptualStep.replace(/_/g, ' ').toUpperCase()}`;
      explanationText.textContent = expl;
    }
  },

  async copyCurrentCode() {
    const rawCode = this.getRawCode();
    const btnText = document.getElementById('copy-btn-text');

    try {
      if (navigator.clipboard && navigator.clipboard.writeText) {
        await navigator.clipboard.writeText(rawCode);
      } else {
        const temp = document.createElement('textarea');
        temp.value = rawCode;
        document.body.appendChild(temp);
        temp.select();
        document.execCommand('copy');
        document.body.removeChild(temp);
      }

      if (btnText) {
        const originalText = btnText.textContent;
        btnText.textContent = 'Copied!';
        setTimeout(() => {
          btnText.textContent = originalText;
        }, 2000);
      }
    } catch (err) {
      if (btnText) btnText.textContent = 'Failed to copy';
    }
  },

  toggleExpand() {
    this.isExpanded = !this.isExpanded;
    const panel = document.getElementById('code-editor-panel');
    if (panel) {
      panel.classList.toggle('expanded', this.isExpanded);
    }
  }
};

window.CodePanel = CodePanel;
