/**
 * SortMaster Quiz & Practice Mode Engine
 * Conceptual University Questions & Performance Assessment (Zero Emojis)
 */

const QuizEngine = {
  currentQuestionIndex: 0,
  score: 0,
  selectedAnswer: null,
  currentOrder: 'ASCENDING',

  conceptualQuestions: [
    {
      question: "Which of the following sorting algorithms is guaranteed to have O(n log n) time complexity in the worst-case while sorting in-place (O(1) auxiliary space)?",
      options: [
        "Merge Sort",
        "Heap Sort",
        "Quick Sort",
        "Bubble Sort"
      ],
      correctIndex: 1,
      explanation: "Heap Sort sorts strictly in-place using O(1) extra space and guarantees O(n log n) runtime even in the worst case. Merge Sort requires O(n) auxiliary memory, while Quick Sort degrades to O(n²) in the worst case."
    },
    {
      question: "Why is standard Selection Sort considered an UNSTABLE sorting algorithm?",
      options: [
        "It uses a divide-and-conquer strategy",
        "It compares elements that are far apart and may swap an earlier identical item past another identical item",
        "It always requires O(n²) space",
        "It requires a randomized pivot"
      ],
      correctIndex: 1,
      explanation: "Selection Sort swaps the extreme element with the boundary element. For example, in [5a, 5b, 2], '2' is swapped with '5a', placing '5a' after '5b', thereby disrupting their original relative order."
    },
    {
      question: "What is the best-case time complexity of an optimized Bubble Sort when given an array that is already in target order?",
      options: [
        "O(n²)",
        "O(n log n)",
        "O(n)",
        "O(1)"
      ],
      correctIndex: 2,
      explanation: "With the swapped boolean flag optimization, Bubble Sort completes a single pass over the n elements without any swaps and terminates immediately in O(n) time."
    },
    {
      question: "Which sorting algorithm is specifically designed to minimize the number of memory write operations, making it ideal for Flash EEPROM memory?",
      options: [
        "Cycle Sort",
        "Radix Sort",
        "Comb Sort",
        "Gnome Sort"
      ],
      correctIndex: 0,
      explanation: "Cycle Sort is theoretically optimal in terms of memory writes. Each element is written either 0 times (if already in place) or at most once to its target cycle position."
    },
    {
      question: "Tim Sort, the standard sorting algorithm in Java for Objects and Python's default sort, is a hybrid combination of which two sorting algorithms?",
      options: [
        "Quick Sort and Heap Sort",
        "Insertion Sort and Merge Sort",
        "Bubble Sort and Selection Sort",
        "Radix Sort and Counting Sort"
      ],
      correctIndex: 1,
      explanation: "Tim Sort detects natural runs, extends short runs using Insertion Sort (which has very low constant factors on small segments), and merges runs using an optimized Merge Sort."
    }
  ],

  ascendingPracticeQuestions: [
    {
      question: "In ASCENDING Bubble Sort on array [8, 3, 12, 1, 5], what occurs during the first adjacent comparison between index 0 (8) and index 1 (3)?",
      options: [
        "8 and 3 are swapped because 8 > 3 violates ascending order (array[i] <= array[i+1])",
        "8 and 3 remain untouched because 8 is larger",
        "12 is immediately swapped with 8",
        "No comparison occurs on the first pass"
      ],
      correctIndex: 0,
      explanation: "In ascending mode, adjacent elements arr[j] and arr[j+1] are swapped whenever arr[j] > arr[j+1]. Since 8 > 3, they swap to [3, 8, 12, 1, 5]."
    },
    {
      question: "In ASCENDING Selection Sort on array [14, 33, 27, 10, 35], which value is placed at index 0 after Pass 1 completes?",
      options: [
        "35 (the maximum element)",
        "10 (the global minimum element)",
        "14 (the original first element)",
        "27 (the median element)"
      ],
      correctIndex: 1,
      explanation: "Ascending Selection Sort scans the unsorted segment for the minimum value (10) and swaps it with the element at the beginning (14), yielding [10, 33, 27, 14, 35]."
    },
    {
      question: "When partitioning array [7, 2, 9, 1, 5] with pivot = 5 using Lomuto partitioning in ASCENDING order, which elements are placed to the left of the pivot?",
      options: [
        "[2, 1] (elements <= pivot)",
        "[7, 9] (elements > pivot)",
        "[9, 1]",
        "[7, 2]"
      ],
      correctIndex: 0,
      explanation: "In ascending Lomuto partitioning, elements satisfying arr[j] <= pivot (2 and 1) are swapped to the left side of the partition."
    },
    {
      question: "To sort an array in ASCENDING order in-place using Heap Sort, what type of binary heap must be constructed in Phase 1?",
      options: [
        "Min-Heap",
        "Max-Heap (so the largest element at root can be swapped to the end)",
        "Binomial Heap",
        "Ternary Heap"
      ],
      correctIndex: 1,
      explanation: "In ascending Heap Sort, a Max-Heap is built first. The maximum element sits at root arr[0] and is repeatedly swapped to the end of the array, growing the sorted suffix."
    },
    {
      question: "For input array [25, 10, 50, 5], which array represents the correct final ASCENDING sorted output?",
      options: [
        "[5, 10, 25, 50]",
        "[50, 25, 10, 5]",
        "[5, 25, 10, 50]",
        "[10, 5, 25, 50]"
      ],
      correctIndex: 0,
      explanation: "Ascending order requires array[i] <= array[i+1] for all valid adjacent pairs: 5 <= 10 <= 25 <= 50."
    }
  ],

  descendingPracticeQuestions: [
    {
      question: "In DESCENDING Bubble Sort on array [8, 3, 12, 1, 5], what occurs during the first adjacent comparison between index 0 (8) and index 1 (3)?",
      options: [
        "8 and 3 are swapped because 8 is larger",
        "8 and 3 are NOT swapped because 8 >= 3 satisfies descending order; next 3 and 12 are compared and swapped (3 < 12)",
        "12 is immediately placed at index 0",
        "The entire array is reversed"
      ],
      correctIndex: 1,
      explanation: "In descending mode, adjacent elements are swapped when arr[j] < arr[j+1]. Since 8 >= 3, they are already in descending order! The next comparison between 3 and 12 (3 < 12) triggers the first swap."
    },
    {
      question: "In DESCENDING Selection Sort on array [14, 33, 27, 10, 35], which value is placed at index 0 after Pass 1 completes?",
      options: [
        "10 (the minimum element)",
        "35 (the global maximum element)",
        "14 (the original first element)",
        "33 (the second element)"
      ],
      correctIndex: 1,
      explanation: "Descending Selection Sort scans the unsorted segment for the maximum value (35) and swaps it into the beginning at index 0, producing [35, 33, 27, 10, 14]."
    },
    {
      question: "When partitioning array [7, 2, 9, 1, 5] with pivot = 5 using Lomuto partitioning in DESCENDING order, which elements are placed to the left of the pivot?",
      options: [
        "[2, 1] (elements <= pivot)",
        "[7, 9] (elements >= pivot)",
        "[9, 1]",
        "[7, 2]"
      ],
      correctIndex: 1,
      explanation: "In descending Lomuto partitioning, elements satisfying arr[j] >= pivot (7 and 9) are grouped to the left side of the partition."
    },
    {
      question: "To sort an array in DESCENDING order in-place using Heap Sort, what type of binary heap must be constructed in Phase 1?",
      options: [
        "Max-Heap",
        "Min-Heap (so the smallest element at root can be swapped to the end)",
        "Fibonacci Heap",
        "Leftist Heap"
      ],
      correctIndex: 1,
      explanation: "In descending Heap Sort, a Min-Heap is built first. The minimum element sits at root arr[0] and is repeatedly swapped to the end of the array, ensuring smaller values settle at the end."
    },
    {
      question: "For input array [25, 10, 50, 5], which array represents the correct final DESCENDING sorted output?",
      options: [
        "[5, 10, 25, 50]",
        "[50, 25, 10, 5]",
        "[50, 10, 25, 5]",
        "[25, 50, 10, 5]"
      ],
      correctIndex: 1,
      explanation: "Descending order requires array[i] >= array[i+1] for all valid adjacent pairs: 50 >= 25 >= 10 >= 5."
    }
  ],

  init() {
    this.bindOrderEvents();
    this.updateOrderUI();
    this.renderQuestion();
  },

  setOrder(order, syncGlobal = true) {
    const normalized = (order || 'ASCENDING').toUpperCase();
    this.currentOrder = normalized;
    this.currentQuestionIndex = 0;
    this.score = 0;
    this.selectedAnswer = null;
    this.updateOrderUI();
    this.renderQuestion();

    if (syncGlobal && window.App && typeof window.App.setOrder === 'function') {
      window.App.setOrder(this.currentOrder, false);
    }
  },

  updateOrderUI() {
    const ascBtn = document.getElementById('quiz-order-btn-asc');
    const descBtn = document.getElementById('quiz-order-btn-desc');
    const badge = document.getElementById('quiz-order-badge');

    const isAsc = this.currentOrder === 'ASCENDING';
    if (ascBtn) {
      ascBtn.classList.toggle('active', isAsc);
      ascBtn.setAttribute('aria-checked', isAsc ? 'true' : 'false');
      ascBtn.setAttribute('tabindex', isAsc ? '0' : '-1');
    }
    if (descBtn) {
      descBtn.classList.toggle('active', !isAsc);
      descBtn.setAttribute('aria-checked', !isAsc ? 'true' : 'false');
      descBtn.setAttribute('tabindex', !isAsc ? '0' : '-1');
    }
    if (badge) {
      badge.textContent = isAsc
        ? 'Ascending: Smallest to Largest'
        : 'Descending: Largest to Smallest';
    }
  },

  bindOrderEvents() {
    const ascBtn = document.getElementById('quiz-order-btn-asc');
    const descBtn = document.getElementById('quiz-order-btn-desc');
    const orderControl = document.getElementById('quiz-order-selector');

    if (ascBtn) {
      ascBtn.addEventListener('click', () => this.setOrder('ASCENDING', true));
    }
    if (descBtn) {
      descBtn.addEventListener('click', () => this.setOrder('DESCENDING', true));
    }

    if (orderControl) {
      orderControl.addEventListener('keydown', (e) => {
        if (e.key === 'ArrowLeft' || e.key === 'ArrowUp' || e.key === 'Home') {
          e.preventDefault();
          this.setOrder('ASCENDING', true);
          if (ascBtn) ascBtn.focus();
        } else if (e.key === 'ArrowRight' || e.key === 'ArrowDown' || e.key === 'End') {
          e.preventDefault();
          this.setOrder('DESCENDING', true);
          if (descBtn) descBtn.focus();
        }
      });
    }
  },

  getActiveQuestions() {
    const practice = this.currentOrder === 'DESCENDING'
      ? this.descendingPracticeQuestions
      : this.ascendingPracticeQuestions;
    return [...this.conceptualQuestions, ...practice];
  },

  renderQuestion() {
    const card = document.getElementById('quiz-card');
    if (!card) return;

    const questions = this.getActiveQuestions();
    if (this.currentQuestionIndex >= questions.length) {
      this.renderResults();
      return;
    }

    const q = questions[this.currentQuestionIndex];
    this.selectedAnswer = null;

    const orderBadgeHtml = `<span class="order-status-badge" style="margin-left: 0.5rem;">${this.currentOrder === 'DESCENDING' ? 'Descending' : 'Ascending'}</span>`;

    card.innerHTML = `
      <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 1rem; color: var(--text-muted); font-size: 0.85rem; font-weight: 600; flex-wrap: wrap; gap: 0.5rem;">
        <div>
          <span>Question ${this.currentQuestionIndex + 1} of ${questions.length}</span>
          ${orderBadgeHtml}
        </div>
        <span>Score: ${this.score}</span>
      </div>
      <h3 class="question-text">${q.question}</h3>
      <div class="options-grid" id="options-grid">
        ${q.options.map((opt, i) => `
          <button class="option-btn" data-index="${i}">
            <span class="option-marker">${String.fromCharCode(65 + i)}</span>
            <span>${opt}</span>
          </button>
        `).join('')}
      </div>
      <div class="quiz-feedback" id="quiz-feedback"></div>
      <div class="quiz-nav-footer">
        <button class="btn-card" id="quiz-skip-btn">Skip Question</button>
        <button class="btn-card primary" id="quiz-next-btn" style="display: none;">
          Next Question
          <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round"><line x1="5" y1="12" x2="19" y2="12"></line><polyline points="12 5 19 12 12 19"></polyline></svg>
        </button>
      </div>
    `;

    const options = card.querySelectorAll('.option-btn');
    options.forEach(btn => {
      btn.addEventListener('click', () => {
        const idx = parseInt(btn.dataset.index, 10);
        this.handleAnswer(idx);
      });
    });

    const skipBtn = document.getElementById('quiz-skip-btn');
    if (skipBtn) {
      skipBtn.addEventListener('click', () => {
        this.currentQuestionIndex++;
        this.renderQuestion();
      });
    }

    const nextBtn = document.getElementById('quiz-next-btn');
    if (nextBtn) {
      nextBtn.addEventListener('click', () => {
        this.currentQuestionIndex++;
        this.renderQuestion();
      });
    }
  },

  handleAnswer(chosenIndex) {
    if (this.selectedAnswer !== null) return;
    this.selectedAnswer = chosenIndex;

    const questions = this.getActiveQuestions();
    const q = questions[this.currentQuestionIndex];
    const isCorrect = chosenIndex === q.correctIndex;
    if (isCorrect) this.score++;

    const buttons = document.querySelectorAll('.option-btn');
    buttons.forEach((btn, idx) => {
      btn.disabled = true;
      if (idx === q.correctIndex) {
        btn.classList.add('correct');
      } else if (idx === chosenIndex) {
        btn.classList.add('wrong');
      }
    });

    const feedback = document.getElementById('quiz-feedback');
    if (feedback) {
      feedback.className = `quiz-feedback show ${isCorrect ? 'correct' : 'wrong'}`;
      feedback.innerHTML = `
        <div style="display: flex; align-items: center; gap: 0.5rem; font-weight: 700;">
          <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round">
            ${isCorrect
              ? '<path d="M22 11.08V12a10 10 0 1 1-5.93-9.14"></path><polyline points="22 4 12 14.01 9 11.01"></polyline>'
              : '<circle cx="12" cy="12" r="10"></circle><line x1="15" y1="9" x2="9" y2="15"></line><line x1="9" y1="9" x2="15" y2="15"></line>'}
          </svg>
          <span>${isCorrect ? 'Correct Analysis' : 'Incorrect Selection'}</span>
        </div>
        <p style="margin-top: 0.45rem; font-size: 0.9rem; line-height: 1.5;">${q.explanation}</p>
      `;
    }

    const skipBtn = document.getElementById('quiz-skip-btn');
    const nextBtn = document.getElementById('quiz-next-btn');
    if (skipBtn) skipBtn.style.display = 'none';
    if (nextBtn) nextBtn.style.display = 'inline-flex';
  },

  renderResults() {
    const card = document.getElementById('quiz-card');
    if (!card) return;

    const questions = this.getActiveQuestions();
    const total = questions.length;
    const percentage = Math.round((this.score / total) * 100);

    card.innerHTML = `
      <div style="text-align: center; padding: 2rem 1rem;">
        <div style="width: 64px; height: 64px; margin: 0 auto 1.5rem auto; border-radius: 50%; background: rgba(56, 189, 248, 0.15); border: 1px solid var(--primary); display: flex; align-items: center; justify-content: center; color: var(--primary);">
          <svg width="32" height="32" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
            <path d="M12 2l3.09 6.26L22 9.27l-5 4.87 1.18 6.88L12 17.77l-6.18 3.25L7 14.14 2 9.27l6.91-1.01L12 2z"></path>
          </svg>
        </div>
        <h3 class="question-text" style="margin-bottom: 0.5rem;">Assessment Completed</h3>
        <p style="font-size: 1.15rem; color: var(--text-secondary); margin-bottom: 1.5rem;">
          You scored <strong>${this.score} out of ${total}</strong> (${percentage}%) in <strong>${this.currentOrder.toLowerCase()} order</strong>
        </p>
        <p style="color: var(--text-muted); max-width: 480px; margin: 0 auto 2rem auto; font-size: 0.95rem; line-height: 1.6;">
          ${percentage >= 80
            ? `Outstanding work! You have mastered sorting mechanics, directional boundary invariants, and complexities for ${this.currentOrder.toLowerCase()} order.`
            : `Good effort! Switch modes, inspect the Visualizer with directional stepping, and retry to strengthen your mastery.`}
        </p>
        <button class="btn-card primary" id="quiz-restart-btn" style="padding: 0.75rem 1.75rem; margin: 0 auto; display: inline-flex;">
          <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
            <polyline points="23 4 23 10 17 10"></polyline>
            <path d="M20.49 15a9 9 0 1 1-2.12-9.36L23 10"></path>
          </svg>
          Retake Assessment
        </button>
      </div>
    `;

    const restartBtn = document.getElementById('quiz-restart-btn');
    if (restartBtn) {
      restartBtn.addEventListener('click', () => {
        this.currentQuestionIndex = 0;
        this.score = 0;
        this.renderQuestion();
      });
    }
  }
};

window.QuizEngine = QuizEngine;
