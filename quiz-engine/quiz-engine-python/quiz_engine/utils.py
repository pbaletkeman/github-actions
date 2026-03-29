from dataclasses import dataclass
from typing import List, Optional
import random
import re
from .models import Question


@dataclass
class ShuffleResult:
    options: List[str]  # shuffled option texts
    labels: List[str]   # ['A', 'B', 'C', 'D'] or ['A','B','C','D','E']
    correct_shuffled_position: int  # index in options that is correct
    answer_mapping: dict  # maps new label -> original label


def shuffle_answers(options: List[str], correct_answer: str) -> ShuffleResult:
    """Randomize answer order, track correct position."""
    # Filter out None values and build (original_label, text) pairs
    labels_all = ['A', 'B', 'C', 'D', 'E']
    pairs = [(labels_all[i], text) for i, text in enumerate(options) if text is not None]

    correct_original = correct_answer.upper()
    random.shuffle(pairs)

    new_labels = ['A', 'B', 'C', 'D', 'E'][:len(pairs)]
    shuffled_texts = [p[1] for p in pairs]
    original_labels = [p[0] for p in pairs]

    answer_mapping = {new_labels[i]: original_labels[i] for i in range(len(pairs))}

    correct_pos = next(i for i, (orig, _) in enumerate(pairs) if orig == correct_original)

    return ShuffleResult(
        options=shuffled_texts,
        labels=new_labels,
        correct_shuffled_position=correct_pos,
        answer_mapping=answer_mapping,
    )


def calculate_score(num_correct: int, num_total: int) -> float:
    if num_total == 0:
        return 0.0
    return round((num_correct / num_total) * 100, 1)


def format_time(seconds: int) -> str:
    """Format seconds as MM:SS"""
    minutes = seconds // 60
    secs = seconds % 60
    return f"{minutes:02d}:{secs:02d}"


def _parse_answer_key(content: str) -> dict:
    """Parse the ## Answer Key table. Returns {q_num: {'answer': str, 'explanation': str|None}}."""
    key_match = re.search(r'^## Answer Key([\s\S]*)$', content, re.MULTILINE)
    if not key_match:
        return {}
    result = {}
    row_pattern = re.compile(r'^\|\s*(\d+)\s*\|\s*([^|]+)\s*\|\s*([^|]*)\s*\|', re.MULTILINE)
    for m in row_pattern.finditer(key_match.group(1)):
        q_num = int(m.group(1))
        raw_answer = m.group(2).strip()
        explanation = m.group(3).strip() or None
        # Only single-letter answers — skip multi-answer "many" type entries like "A, B, D"
        if re.match(r'^[A-E]$', raw_answer):
            result[q_num] = {'answer': raw_answer, 'explanation': explanation}
    return result


def _extract_structured_question(block: str) -> str:
    """Extract question text from a structured **Scenario**: / **Question**: block."""
    scenario_match = re.search(r'\*\*Scenario\*\*:\s*([\s\S]+?)(?=\*\*Question\*\*:|$)', block)
    question_match = re.search(r'\*\*Question\*\*:\s*([\s\S]+?)(?=\n-\s[A-E]\)|$)', block)
    text = ''
    if scenario_match:
        text += scenario_match.group(1).strip() + '\n\n'
    if question_match:
        text += question_match.group(1).strip()
    return text.strip()


def parse_markdown_file(file_path: str) -> List[Question]:
    """Parse gh-200-iteration-*.md files. Answers are read from the ## Answer Key table
    when present; otherwise the inline **Answer: X** marker is used as a fallback."""
    with open(file_path, 'r', encoding='utf-8') as f:
        content = f.read()

    answer_key = _parse_answer_key(content)
    has_answer_key = bool(answer_key)

    # Strip the answer key section so it is not mistaken for a question block
    questions_content = re.sub(r'^## Answer Key[\s\S]*$', '', content, flags=re.MULTILINE) if has_answer_key else content

    questions = []
    # Split at ## Q<n>, ## Question <n>, or ### Question <n> headers
    blocks = re.split(r'(?=^#{2,3}\s+(?:Q\d+|Question\s+\d+))', questions_content, flags=re.MULTILINE)

    for block in blocks:
        block = block.strip()
        if not block:
            continue

        # Parse header: ## Q1 / ## Question 1 / ### Question 1 — Section
        header_match = re.match(
            r'^#{2,3}\s+(?:Q(\d+)|Question\s+(\d+))(?:\s+[-\u2014\u2013]+\s+(.+))?',
            block,
        )
        if not header_match:
            continue

        q_num_str = header_match.group(1) or header_match.group(2)
        question_num = int(q_num_str) if q_num_str else 0
        section = header_match.group(3).strip() if header_match.group(3) else None

        # Extract difficulty
        diff_match = re.search(r'^\*\*Difficulty\*\*:\s*(.+)$', block, re.MULTILINE)
        difficulty = diff_match.group(1).strip() if diff_match else None

        # Skip many/none answer types — only import single-answer questions
        type_match = re.search(r'^\*\*Answer\s+Type\*\*:\s*(.+?)$', block, re.MULTILINE)
        if type_match and type_match.group(1).strip().lower() != 'one':
            continue

        # Extract question text
        if '**Question**:' in block or '**Scenario**:' in block:
            question_text = _extract_structured_question(block)
        else:
            # Simple format: text is everything between header and first option
            quote_match = re.search(r'^>\s*(.+?)$', block, re.MULTILINE)
            if quote_match:
                question_text = quote_match.group(1).strip()
            else:
                body = block[header_match.end():].strip()
                opt_start = re.search(r'^-\s+[A-E]\)', body, re.MULTILINE)
                question_text = body[:opt_start.start()].strip() if opt_start else ''
                question_text = re.sub(r'\*{1,3}', '', question_text).strip()

        if not question_text:
            continue

        # Extract options A–E
        options: dict = {}
        for m in re.finditer(r'^-\s+([A-E])\)\s+(.+)$', block, re.MULTILINE):
            options[m.group(1)] = m.group(2).strip()

        if not all(k in options for k in ('A', 'B', 'C', 'D')):
            continue

        # Resolve answer: answer key takes priority, then inline **Answer: X**
        correct_answer = None
        explanation = None

        key_entry = answer_key.get(question_num)
        if key_entry:
            correct_answer = key_entry['answer']
            explanation = key_entry['explanation']
        else:
            ans_match = re.search(r'\*\*Answer:\s*([A-E])\*\*', block, re.IGNORECASE)
            if ans_match:
                correct_answer = ans_match.group(1).upper()
                after = block[ans_match.end():].strip()
                if after:
                    explanation = re.sub(r'^>\s*', '', after, flags=re.MULTILINE).strip()[:500] or None
            elif has_answer_key:
                continue  # question number not in key (many/none type already skipped above)
            else:
                continue  # no answer found at all

        if not correct_answer:
            continue

        questions.append(Question(
            question_text=question_text,
            option_a=options.get('A', ''),
            option_b=options.get('B', ''),
            option_c=options.get('C', ''),
            option_d=options.get('D', ''),
            option_e=options.get('E'),
            correct_answer=correct_answer,
            explanation=explanation,
            section=section,
            difficulty=difficulty,
        ))

    return questions
