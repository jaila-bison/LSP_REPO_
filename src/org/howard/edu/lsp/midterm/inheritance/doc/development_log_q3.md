# Development Log – Question 3 (Inheritance)

## AI Tools

**Tool:** Claude (Anthropic), used through claude.ai on October 26, 2026.

**Full conversation transcript (shareable link):** PASTE_SHARE_LINK_HERE

The link above opens the complete conversation, including every prompt I wrote and every response Claude gave. The same conversation was used for Questions 1, 2 and 3. Claude was **not** used for Question 4.

**Prompts I gave:**

1. I uploaded the midterm exam document (no text).
2. "whats needed to just do questions that are ai able"

**What Claude did for this question and how it helped:**

Claude wrote an abstract `Employee` class with an abstract `calculatePay()` method and three subclasses that override it: `FullTimeEmployee` (1.5x overtime after 40 hours), `PartTimeEmployee` (straight hourly pay) and `Contractor` (15% premium). This replaces the original string conditional with polymorphism and no design pattern. Claude added Javadoc and ran the provided Driver, which reported **12 PASSED, 0 FAILED**.

**How I checked the work:** I compiled the code and ran the instructor-provided Driver(s) without modifying them, and I read through the generated code and documentation to make sure I understand it.

## Websites

None.

## Lecture notes / books

None beyond general course knowledge.
