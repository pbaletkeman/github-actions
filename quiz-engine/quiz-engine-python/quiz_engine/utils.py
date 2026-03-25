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


def parse_markdown_file(file_path: str) -> List[Question]:
    """Parse gh-200-iteration-*.md files. Extract questions, options, answers."""
    with open(file_path, 'r', encoding='utf-8') as f:
        content = f.read()

    questions = []

    # Split on question headers: ## Question N or ### Question N
    question_blocks = re.split(r'\n(?=##+ Question \d+)', content)

    for block in question_blocks:
        block = block.strip()
        if not block:
            continue

        # Check if this block starts with a question header
        header_match = re.match(r'^#{1,3}\s+Question\s+\d+[.:)]*\s*\n', block, re.IGNORECASE)
        if not header_match:
            continue

        # Remove the header line
        body = block[header_match.end():].strip()

        # Extract options: lines like "- A) text" or "A) text" or "- **A)** text"
        option_pattern = re.compile(
            r'^[-*]?\s*\*{0,2}([A-E])\*{0,2}[.):\s]+(.+)$',
            re.IGNORECASE | re.MULTILINE
        )
        option_matches = list(option_pattern.finditer(body))

        if len(option_matches) < 4:
            # Try alternative format: A. text
            option_pattern2 = re.compile(
                r'^[-*]?\s*([A-E])[.)\s]+(.+)$',
                re.IGNORECASE | re.MULTILINE
            )
            option_matches = list(option_pattern2.finditer(body))

        if len(option_matches) < 4:
            continue

        # Get question text (everything before first option)
        first_opt_start = option_matches[0].start()
        question_text = body[:first_opt_start].strip()

        # Remove markdown bold/italic and clean up
        question_text = re.sub(r'\*{1,3}', '', question_text).strip()

        if not question_text:
            continue

        # Extract options A-E
        opts = {}
        for m in option_matches:
            letter = m.group(1).upper()
            text = m.group(2).strip()
            text = re.sub(r'\*{1,3}', '', text).strip()
            opts[letter] = text

        if not all(k in opts for k in ('A', 'B', 'C', 'D')):
            continue

        # Extract answer
        answer_match = re.search(
            r'\*{0,2}Answer[:\s]+([A-E])\*{0,2}',
            body,
            re.IGNORECASE
        )
        if not answer_match:
            # Try "Correct Answer: X"
            answer_match = re.search(
                r'Correct\s+Answer[:\s]+([A-E])',
                body,
                re.IGNORECASE
            )

        correct_answer = answer_match.group(1).upper() if answer_match else None

        # Extract explanation (text after the answer line)
        explanation = None
        if answer_match:
            after_answer = body[answer_match.end():].strip()
            # Remove leading explanation markers
            after_answer = re.sub(r'^[*_]*Explanation[*_]*[:\s]*', '', after_answer, flags=re.IGNORECASE).strip()
            after_answer = re.sub(r'\*{1,3}', '', after_answer).strip()
            if after_answer:
                explanation = after_answer[:500]  # cap length

        q = Question(
            question_text=question_text,
            option_a=opts.get('A', ''),
            option_b=opts.get('B', ''),
            option_c=opts.get('C', ''),
            option_d=opts.get('D', ''),
            option_e=opts.get('E'),
            correct_answer=correct_answer,
            explanation=explanation,
        )
        questions.append(q)

    return questions
