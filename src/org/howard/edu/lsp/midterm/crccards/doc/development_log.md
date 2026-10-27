# Development Log – Question 1 (CRC Cards)

## AI Tools

**Tool:** Claude (Anthropic), used through claude.ai on October 26, 2026.

**Full conversation transcript (shareable link):** https://claude.ai/share/2cb29963-411b-490f-8624-23de7c96c798

The link above opens the complete conversation, including every prompt I wrote and every response Claude gave. The same conversation was used for Questions 1, 2 and 3. Claude was **not** used for Question 4.


**What Claude did for this question and how it helped:**

Claude read the CRC cards and requirements and wrote `Book.java`, `Library.java` and `crccards.md`. It chose a `HashMap<String, Book>` keyed by ISBN for the Library (fast lookup and easy duplicate detection), added Javadoc to all classes and public methods, and compiled and ran the provided Driver, which reported **44 PASSED, 0 FAILED** (25.00 / 25).

**How I checked the work:** I compiled the code and ran the instructor-provided Driver(s) without modifying them, and I read through the generated code and documentation to make sure I understand it.

## Websites

None.

## Lecture notes / books

None beyond general course knowledge.
