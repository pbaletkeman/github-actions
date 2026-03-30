# Quiz Engine -- Node.js / TypeScript

> Part of the [Quiz Engine multi-language collection](../README.md)

- [Quiz Engine -- Node.js / TypeScript](#quiz-engine----nodejs--typescript)
  - [Get Started in 5 Minutes](#get-started-in-5-minutes)
    - [Prerequisites](#prerequisites)
    - [1. Install \& Build](#1-install--build)
    - [2. Import Questions](#2-import-questions)
    - [3. Take a Quiz](#3-take-a-quiz)
    - [4. View History](#4-view-history)
    - [5. Docker](#5-docker)
  
A CLI quiz engine for GitHub Actions (GH-200) certification prep. Built with TypeScript, TypeORM, and SQLite.

## Get Started in 5 Minutes

### Prerequisites
- [Node.js 18+](https://nodejs.org/) and npm

### 1. Install & Build
```bash
npm install
npm run build
```

### 2. Import Questions
```bash
npm run dev -- import --file questions.md
npm run dev -- import --dir ./questions/
```

### 3. Take a Quiz
```bash
npm run dev -- quiz --questions 10
```

### 4. View History
```bash
npm run dev -- history
```

### 5. Docker
```bash
docker build -t quiz-engine-node:latest .
docker-compose up quiz-engine
```

---

Full docs: [docs/README.md](docs/README.md) | Architecture: [docs/architecture.md](docs/architecture.md)
