# ⚡ SortMaster — DSA Sorting Academy

**SortMaster** is a modern, interactive educational web platform built to help university students understand, visualize, analyze, and master 16 sorting algorithms through real Java-backed animations, line-by-line code tracing, Big-O complexity guides, multi-algorithm benchmarks, and conceptual quizzes.

Rather than relying on simulated front-end animations, SortMaster executes **actual Java 21 algorithms** on a **Spring Boot 3** backend, recording immutable array snapshots and telemetry events for every comparison, swap, pivot, write, and range partition.

---

## 🌟 Key Highlights

- **16 Complete Sorting Algorithms:**
  1. Bubble Sort *(with early-exit optimization)*
  2. Selection Sort *(minimum-write demonstration)*
  3. Insertion Sort *(adaptive sublist shifts)*
  4. Merge Sort *(divide-and-conquer stable merge)*
  5. Quick Sort *(Lomuto partition & pivot visualization)*
  6. Heap Sort *(in-place binary heapify & root extractions)*
  7. Shell Sort *(diminishing gap sequences)*
  8. Counting Sort *(non-comparison frequency distribution with negative number offsets)*
  9. Radix Sort *(LSD base-10 digit sorting)*
  10. Bucket Sort *(uniform scatter-gather intervals)*
  11. Cocktail Shaker Sort *(bidirectional turtle elimination)*
  12. Comb Sort *(shrink-factor 1.3 gap exchange)*
  13. Gnome Sort *(single-pointer pot traversal)*
  14. Cycle Sort *(optimal theoretical write minimization)*
  15. Pigeonhole Sort *(Dirichlet drawer principle)*
  16. Tim Sort *(genuine hybrid with natural run detection, minRun extension, and hierarchical merging)*

- **Interactive Animation Engine:**
  - Dynamic vertical bars with heights scaled proportionally to values.
  - Value labels displayed automatically for manageable array sizes.
  - Color-coded operations:
    - 🟨 **Amber**: Element comparisons
    - 🟥 **Crimson**: Element swaps and writes
    - 🟪 **Violet**: Active pivots and partitions
    - 🟩 **Emerald**: Confirmed sorted positions
  - Playback controls: Play, Pause, Next Step, Previous Step, Reset to Start, Jump to End, Random Array, Preset Arrays (Reversed, Nearly Sorted, Few Unique), and Custom Array modal input.
  - Live speed slider (1x to 20x) and array size slider (6 to 60 elements).

- **Java & Pseudocode Learning Panel:**
  - Real-time conceptual line highlighting synchronized with visualization events.
  - Contextual explanations explaining *why* each operation is performed.
  - Toggle between Java 21 implementation and academic pseudocode.

- **Deep-Dive Study Academy:**
  - Comprehensive university-level guide for all 16 algorithms.
  - Detailed intuition, step-by-step examples, Big-O complexity tables (Best, Average, Worst, Auxiliary Space).
  - Stability and in-place properties.
  - Practical advantages & disadvantages.
  - Common student exam traps and mistakes.
  - Frequently asked exam questions with expandable answers.

- **Quiz & Practice Lab:**
  - 12+ challenging multiple-choice questions testing Big-O intuition, stability, partition traps, and cache locality.
  - Instant answer validation and detailed explanations.
  - Score tracking and retake options.

- **Multi-Algorithm Comparison Lab:**
  - Select two or more algorithms to run side-by-side against an **identical initial dataset**.
  - Evaluates theoretical complexity alongside empirical telemetry:
    - Number of comparisons
    - Number of swaps
    - Number of writes/shifts
    - Elapsed server execution time in milliseconds

- **Modern Glassmorphic UI:**
  - Built with Vanilla CSS using CSS variables and modern glassmorphism.
  - Dark Theme (default) and Light Theme toggle with `localStorage` persistence.
  - Fully responsive across desktop, tablet, and mobile screens.

---

## 🛠️ Technology Stack

- **Backend:** Java 21, Spring Boot 3.3.4
- **Build & Dependency Management:** Apache Maven 3.9+
- **Testing:** JUnit 5, Spring Boot Test, MockMvc (157 unit & parameterized tests)
- **Frontend:** HTML5, CSS3 (Vanilla CSS with custom properties & glassmorphism), Vanilla JavaScript (ES6+ modules)
- **Architecture:** Decoupled REST API emitting immutable `SortingEvent` state snapshots

---

## 🚀 Quickstart Guide

### Prerequisites
- **Java Development Kit (JDK):** Version 21 or higher (`java -version`)
- **Apache Maven:** Version 3.8+ (`mvn -version`)

### 1. Clone & Enter Project Directory
```bash
cd "SortMaster"
```

### 2. Run Automated Test Suite
To verify that all 16 algorithms, immutability guarantees, range validations, and REST endpoints pass:
```bash
mvn clean test
```

### 3. Package and Run the Application
You can run using the Spring Boot Maven plugin:
```bash
mvn spring-boot:run
```
Or package an executable JAR:
```bash
mvn package
java -jar target/sortmaster-academy-1.0.0.jar
```

### 4. Open in Browser
Open your browser and navigate to:
```
http://localhost:8080
```

---

## 📡 REST API Documentation

### 1. Health Check
- **Endpoint:** `GET /api/health`
- **Description:** Verifies server availability and reports the count and list of supported algorithms.
- **Example Response:**
```json
{
  "status": "UP",
  "application": "SortMaster — DSA Sorting Academy",
  "version": "1.0.0",
  "algorithmCount": 16,
  "supportedAlgorithms": [
    "bubble-sort",
    "selection-sort",
    "insertion-sort",
    "merge-sort",
    "quick-sort",
    "heap-sort",
    "shell-sort",
    "counting-sort",
    "radix-sort",
    "bucket-sort",
    "cocktail-shaker-sort",
    "comb-sort",
    "gnome-sort",
    "cycle-sort",
    "pigeonhole-sort",
    "tim-sort"
  ]
}
```

---

### 2. Retrieve All Algorithms
- **Endpoint:** `GET /api/algorithms`
- **Description:** Returns the metadata, complexity specifications, stability, advantages, disadvantages, and Java source code for all 16 algorithms.

---

### 3. Retrieve Specific Algorithm
- **Endpoint:** `GET /api/algorithms/{id}`
- **Parameters:** `id` (e.g. `quick-sort`, `tim-sort`, `merge-sort`)
- **Description:** Returns detailed educational content for the requested algorithm.

---

### 4. Execute Sorting Algorithm
- **Endpoint:** `POST /api/sort`
- **Request Body:**
```json
{
  "algorithmId": "quick-sort",
  "array": [25, 10, 50, 5, 30],
  "detailLevel": "NORMAL"
}
```
- **Example Response:**
```json
{
  "algorithmId": "quick-sort",
  "algorithmName": "Quick Sort",
  "initialArray": [25, 10, 50, 5, 30],
  "sortedArray": [5, 10, 25, 30, 50],
  "events": [
    {
      "stepIndex": 1,
      "eventType": "START",
      "array": [25, 10, 50, 5, 30],
      "indices": [],
      "message": "Beginning Quick Sort on 5 elements.",
      "conceptualStep": "start"
    },
    {
      "stepIndex": 2,
      "eventType": "PIVOT",
      "array": [25, 10, 50, 5, 30],
      "indices": [4],
      "message": "Selected pivot element 30 at index 4.",
      "conceptualStep": "choose_pivot"
    }
  ],
  "statistics": {
    "comparisons": 7,
    "swaps": 4,
    "writes": 8,
    "executionTimeNanos": 611288,
    "totalSteps": 24,
    "executionTimeMillis": 0.61
  },
  "truncated": false,
  "message": "Sorting completed successfully"
}
```

---

### 5. Multi-Algorithm Comparison
- **Endpoint:** `POST /api/compare`
- **Request Body:**
```json
{
  "algorithmIds": ["quick-sort", "merge-sort", "bubble-sort"],
  "array": [64, 34, 25, 12, 22, 11, 90]
}
```
- **Example Response:**
```json
{
  "initialArray": [64, 34, 25, 12, 22, 11, 90],
  "results": [
    {
      "algorithmId": "quick-sort",
      "algorithmName": "Quick Sort",
      "bestTimeComplexity": "O(n log n)",
      "averageTimeComplexity": "O(n log n)",
      "worstTimeComplexity": "O(n²)",
      "spaceComplexity": "O(log n)",
      "stable": false,
      "inPlace": true,
      "statistics": {
        "comparisons": 14,
        "swaps": 7,
        "writes": 14,
        "executionTimeMillis": 0.42
      }
    }
  ]
}
```

---

## 📁 Project Architecture

```
.
├── pom.xml
├── README.md
└── src
    ├── main
    │   ├── java/com/sortmaster
    │   │   ├── SortMasterApplication.java
    │   │   ├── algorithm/
    │   │   │   ├── SortingAlgorithm.java          # Common sorting interface
    │   │   │   ├── AbstractSortingAlgorithm.java  # Snapshot generator & metrics base
    │   │   │   ├── BubbleSort.java
    │   │   │   ├── SelectionSort.java
    │   │   │   ├── InsertionSort.java
    │   │   │   ├── MergeSort.java
    │   │   │   ├── QuickSort.java
    │   │   │   ├── HeapSort.java
    │   │   │   ├── ShellSort.java
    │   │   │   ├── CountingSort.java
    │   │   │   ├── RadixSort.java
    │   │   │   ├── BucketSort.java
    │   │   │   ├── CocktailShakerSort.java
    │   │   │   ├── CombSort.java
    │   │   │   ├── GnomeSort.java
    │   │   │   ├── CycleSort.java
    │   │   │   ├── PigeonholeSort.java
    │   │   │   └── TimSort.java
    │   │   ├── controller/
    │   │   │   └── SortingApiController.java      # REST endpoints
    │   │   ├── dto/                               # Request & response payloads
    │   │   ├── exception/                         # Global error handling
    │   │   ├── model/                             # Event, metadata, & metrics models
    │   │   └── service/
    │   │       ├── AlgorithmRegistry.java         # Dynamic algorithm provider
    │   │       └── SortingService.java            # Execution & comparison logic
    │   └── resources
    │       ├── application.properties
    │       └── static/
    │           ├── css/style.css                  # Modern glassmorphism & dual-theme CSS
    │           ├── js/
    │           │   ├── api.js                     # REST communication client
    │           │   ├── visualizer.js              # Bar renderer & animation controller
    │           │   ├── code-panel.js              # Synchronized Java/Pseudocode tracing
    │           │   ├── quiz.js                    # University DSA quiz engine
    │           │   ├── comparison.js              # Empirical comparison benchmark
    │           │   └── app.js                     # Navigation, search, & state
    │           └── index.html                     # SPA entry point
    └── test
        └── java/com/sortmaster
            ├── algorithm/
            │   ├── AllAlgorithmsTest.java         # Parameterized tests across all 16 algorithms
            │   └── BubbleSortTest.java
            └── controller/
                └── SortingApiControllerTest.java  # MockMvc integration tests
```

---

## 🧪 Testing Verification

SortMaster features automated testing covering:
- Empty and single-element arrays
- Pre-sorted arrays (testing adaptive O(n) shortcuts)
- Reverse-sorted arrays (testing worst-case partitions)
- Large sets with negative integers and duplicates
- Range-boundary protections (e.g. Counting/Pigeonhole sort memory guards)
- Snapshot immutability (mutating the output array never alters prior historical frames)
- Verification against standard `Arrays.sort()`

Execute all tests with:
```bash
mvn test
```
Result: **157 Tests Run — 0 Failures, 0 Errors, 0 Skipped.**

---

## 🔒 Security & Reliability Guarantees

1. **Memory Protection:** Maximum array size for animation is restricted to 200 elements, and visualization events are capped at 5,000 to prevent browser DOM freezes and out-of-memory errors.
2. **Range Guards:** Distribution algorithms (Counting Sort and Pigeonhole Sort) validate that `max - min <= 100,000`, refusing inputs with massive ranges that would otherwise trigger server OOM exceptions.
3. **No Code Execution (`eval`):** All sorting is implemented as statically compiled Java code. The system never executes user-provided arbitrary code.
4. **Defensive Snapshots:** Every animation frame stores a cloned array copy, preventing future iterations from altering past event states.

---

## 📜 License
This project is open-source educational software distributed under the MIT License.
