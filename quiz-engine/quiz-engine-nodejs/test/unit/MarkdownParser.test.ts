import * as fs from 'fs';
import * as path from 'path';
import * as os from 'os';
import { parseMarkdownFile, parseMarkdownContent } from '../../src/service/MarkdownParser';

const SIMPLE_MD = `
## Q1
> What does CI stand for?
- A) Continuous Integration
- B) Code Import
- C) Compiler Install
- D) Content Index
**Answer: A**
`;

const STRUCTURED_MD = `
### Question 1 — DevOps

**Difficulty**: Easy
**Answer Type**: one
**Topic**: CI/CD

**Question**:
What does CI stand for?

- A) Continuous Integration
- B) Code Import
- C) Compiler Install
- D) Content Index

**Answer: A**
`;

const SCENARIO_MD = `
### Question 1 — Context

**Difficulty**: Medium
**Answer Type**: one
**Topic**: workflow

**Scenario**:
A developer wants to run tests on push.

**Question**:
Which trigger should they use?

- A) on: push
- B) on: schedule
- C) on: workflow_dispatch
- D) on: pull_request

**Answer: A**
`;

const WITH_EXPLANATION_MD = `
## Q1
> What does CI stand for?
- A) Continuous Integration
- B) Code Import
- C) Compiler Install
- D) Content Index
**Answer: A**
CI stands for Continuous Integration.
`;

const WITH_OPTION_E_MD = `
## Q1
> Five option question?
- A) Alpha
- B) Beta
- C) Gamma
- D) Delta
- E) Epsilon
**Answer: E**
`;

const MISSING_ANSWER_MD = `
## Q1
> No answer provided.
- A) Option A
- B) Option B
- C) Option C
- D) Option D
`;

const MISSING_OPTIONS_MD = `
## Q1
> Missing options
- A) Option A
**Answer: A**
`;

const MULTI_QUESTION_MD = `
## Q1
> First question?
- A) Option A
- B) Option B
- C) Option C
- D) Option D
**Answer: B**

## Q2
> Second question?
- A) Option A
- B) Option B
- C) Option C
- D) Option D
**Answer: C**
`;

const SOURCE_FORMAT_MD = `
## Questions

### Question 1 — DevOps

**Difficulty**: Easy
**Answer Type**: one
**Topic**: CI/CD

**Question**:
What does CI stand for?

- A) Continuous Integration
- B) Code Import
- C) Compiler Install
- D) Content Index

---

### Question 2 — DevOps

**Difficulty**: Medium
**Answer Type**: many
**Topic**: CI/CD

**Question**:
Which of these are CI tools?

- A) Jenkins
- B) Word
- C) Travis CI
- D) Paint

---

## Answer Key

| Q# | Answer(s) | Explanation | Source | Difficulty |
|----|-----------|-------------|--------|------------|
| 1 | A | CI stands for Continuous Integration. | source.md | Easy |
| 2 | A, C | Jenkins and Travis CI are CI tools. | source.md | Medium |
`;

function writeTempFile(content: string): string {
  const filePath = path.join(os.tmpdir(), `quiz-test-${Date.now()}-${Math.random()}.md`);
  fs.writeFileSync(filePath, content, 'utf-8');
  return filePath;
}

describe('MarkdownParser', () => {
  describe('parseMarkdownContent', () => {
    it('parses a simple markdown question', () => {
      const questions = parseMarkdownContent(SIMPLE_MD);
      expect(questions).toHaveLength(1);
      expect(questions[0].questionText).toBe('What does CI stand for?');
      expect(questions[0].optionA).toBe('Continuous Integration');
      expect(questions[0].correctAnswer).toBe('A');
    });

    it('parses a structured (GH-200 format) question', () => {
      const questions = parseMarkdownContent(STRUCTURED_MD);
      expect(questions).toHaveLength(1);
      expect(questions[0].questionText).toContain('What does CI stand for?');
      expect(questions[0].correctAnswer).toBe('A');
      expect(questions[0].difficulty).toBe('Easy');
      expect(questions[0].section).toBe('DevOps');
    });

    it('parses a scenario-style question', () => {
      const questions = parseMarkdownContent(SCENARIO_MD);
      expect(questions).toHaveLength(1);
      expect(questions[0].questionText).toContain('Which trigger should they use?');
      expect(questions[0].correctAnswer).toBe('A');
    });

    it('parses explanation text after answer', () => {
      const questions = parseMarkdownContent(WITH_EXPLANATION_MD);
      expect(questions).toHaveLength(1);
      expect(questions[0].explanation).toContain('CI stands for Continuous Integration');
    });

    it('parses 5-option question', () => {
      const questions = parseMarkdownContent(WITH_OPTION_E_MD);
      expect(questions).toHaveLength(1);
      expect(questions[0].optionE).toBe('Epsilon');
      expect(questions[0].correctAnswer).toBe('E');
    });

    it('parses multiple questions from one file', () => {
      const questions = parseMarkdownContent(MULTI_QUESTION_MD);
      expect(questions).toHaveLength(2);
      expect(questions[0].correctAnswer).toBe('B');
      expect(questions[1].correctAnswer).toBe('C');
    });

    it('throws on missing answer line', () => {
      expect(() => parseMarkdownContent(MISSING_ANSWER_MD)).toThrow();
    });

    it('throws on missing required options', () => {
      expect(() => parseMarkdownContent(MISSING_OPTIONS_MD)).toThrow();
    });

    it('returns empty array for empty content', () => {
      const questions = parseMarkdownContent('');
      expect(questions).toHaveLength(0);
    });

    it('returns empty array for content with no question headers', () => {
      const questions = parseMarkdownContent('# Just a heading\nSome text here.');
      expect(questions).toHaveLength(0);
    });
  });

  describe('parseMarkdownFile', () => {
    it('parses a valid markdown file', () => {
      const filePath = writeTempFile(SIMPLE_MD);
      try {
        const questions = parseMarkdownFile(filePath);
        expect(questions).toHaveLength(1);
        expect(questions[0].correctAnswer).toBe('A');
      } finally {
        fs.unlinkSync(filePath);
      }
    });

    it('throws on missing file', () => {
      expect(() => parseMarkdownFile('/nonexistent/path/file.md')).toThrow();
    });

    it('parses file with multiple questions', () => {
      const filePath = writeTempFile(MULTI_QUESTION_MD);
      try {
        const questions = parseMarkdownFile(filePath);
        expect(questions).toHaveLength(2);
      } finally {
        fs.unlinkSync(filePath);
      }
    });
  });

  describe('source format with answer key table', () => {
    it('parses single-answer questions from answer key', () => {
      const questions = parseMarkdownContent(SOURCE_FORMAT_MD);
      expect(questions).toHaveLength(1);
      expect(questions[0].correctAnswer).toBe('A');
    });

    it('extracts explanation from answer key', () => {
      const questions = parseMarkdownContent(SOURCE_FORMAT_MD);
      expect(questions[0].explanation).toBe('CI stands for Continuous Integration.');
    });

    it('extracts section from source format header', () => {
      const questions = parseMarkdownContent(SOURCE_FORMAT_MD);
      expect(questions[0].section).toBe('DevOps');
    });

    it('skips many-type questions in source format', () => {
      const questions = parseMarkdownContent(SOURCE_FORMAT_MD);
      const texts = questions.map((q) => q.questionText);
      expect(texts.some((t) => t.includes('CI tools'))).toBe(false);
    });
  });
});
