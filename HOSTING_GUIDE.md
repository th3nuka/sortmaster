# SortMaster — 100% Static Web Hosting Guide

SortMaster is a completely static, browser-executable web application. All 16 sorting algorithms, step-by-step animations, code academy, and quizzes run directly in the browser via JavaScript.

**Zero backend servers, zero Docker, zero configuration, and 100% free hosting.**

---

## Option 1: Free Static Hosting on Render (No Docker)

1. Go to **[https://render.com](https://render.com)** and log in with GitHub.
2. Click **New +** ➔ **Static Site**.
3. Select your repository: **`th3nuka/sortmaster`**.
4. Configure settings:
   - **Name:** `sortmaster`
   - **Branch:** `main`
   - **Build Command:** *(leave empty)*
   - **Publish Directory:** `.` *(a single dot for the root folder)*
5. Click **Create Static Site**.
   - Your site will be live immediately on a free `https://sortmaster.onrender.com` domain with free SSL/HTTPS!

---

## Option 2: Free Hosting on GitHub Pages

1. Open your repository on GitHub: **[https://github.com/th3nuka/sortmaster](https://github.com/th3nuka/sortmaster)**.
2. Go to **Settings** ➔ **Pages** (in the left sidebar).
3. Under **Branch**, select **`main`** and folder **`/ (root)`**.
4. Click **Save**.
   - Your website will be live in ~60 seconds at:
     **`https://th3nuka.github.io/sortmaster/`**

---

## Option 3: Local Offline Use

Just double-click `index.html` in your browser!
The application runs 100% offline with zero server requirements.
