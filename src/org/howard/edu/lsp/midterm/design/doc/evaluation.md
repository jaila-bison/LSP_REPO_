# Design Evaluation – CourseRegistration

`CourseRegistration` works, but it breaks several of Arthur Riel's object-oriented design heuristics. The problems below make the class hard to maintain, hard to extend, and easy to break.

## 1. All data is public (Riel H2.1: all data should be hidden within its class)

`studentId`, `studentName`, `email`, `courseCode` and `creditHours` are all `public`. Any code can change them at any time without validation. For example, `creditHours` can be set to a negative number, which produces negative tuition, or `email` can be set to `null`. Because the data is not encapsulated, the class cannot protect its own invariants. Any change to how the data is stored (renaming a field, changing its type) breaks every client that touches the field directly.

## 2. One class does too many unrelated things (Riel H2.8: a class should capture one and only one key abstraction; H3.2: do not create god classes)

`registerStudent()` does six different jobs in one method:

1. calculates tuition,
2. applies a discount policy,
3. prints a confirmation to the console,
4. writes a record to a file,
5. "sends" an email,
6. logs a timestamp.

These are separate concerns: business rules, presentation, persistence, notification and logging. The class has low cohesion and has become a small god class. A change to any one concern (a new tuition rate, a new file format, a real email service) forces a change to this class, so it has many reasons to change. This also makes it impossible to test the tuition calculation without writing to a real file and printing to the console.

## 3. Student and course data are mixed into one abstraction (Riel H2.8, H2.9: keep related data and behavior in one place)

The class holds student data (`studentId`, `studentName`, `email`) and course data (`courseCode`, `creditHours`) as flat fields. These are two separate key abstractions, a Student and a Course, collapsed into a "registration." A student who registers for five courses would have their name and email duplicated five times, and changing a student's email means finding every registration object. A registration should *relate* a Student to a Course, not copy their fields.

## 4. Business rules are hard-coded (Riel H2.8; poor extensibility)

The tuition rate (`750.0`), the full-load threshold (`12`), and the discount (`0.90`) are magic numbers inside the method. Adding a different rate for graduate students, or a new discount, means editing and re-testing the whole method. A pricing policy should be its own class so it can change without touching registration, storage or notification code.

## 5. Direct dependence on concrete infrastructure (Riel H4.1: minimize the number of classes with which another class collaborates; avoid tight coupling)

The class constructs a `FileWriter` for a hard-coded `"registrations.txt"` and calls `System.out` directly. It is tightly coupled to one storage mechanism and one output device. Switching to a database, or sending a real email, means rewriting this class. There is no abstraction (interface) between "what needs to happen" (save a record, notify the student) and "how it happens."

## 6. Correctness and robustness problems

- **Resource leak:** if `writer.write(...)` throws, `writer.close()` is never called. A try-with-resources block is needed.
- **Swallowed exceptions:** `catch (Exception e) { e.printStackTrace(); }` hides failures. The method still prints a confirmation and "sends" an email even if the record was never saved, so the student is told they are registered when they are not.
- **Wrong order of operations:** confirmation is printed *before* the save succeeds.
- **Money as `double`:** tuition is stored as a `double`, which can produce rounding errors (for example `8100.000000001`). Currency should use `BigDecimal` or integer cents.
- **No validation:** there is no check that the student or course exists, that credit hours are positive, or that the student isn't already registered.

## 7. Misleading method name

The method is called `registerStudent()`, but most of what it does is not registration. The name hides the side effects (file I/O, email), which surprises anyone calling it.

## Summary

The class violates encapsulation (H2.1), combines several key abstractions and concerns into one god class (H2.8, H3.2), and is tightly coupled to concrete infrastructure. As a result, it is hard to maintain (every change touches the same method), hard to extend (new storage or notification channels mean rewriting it), hard to test, and it can report success when a save fails. The redesign in `design.md` splits these responsibilities into focused classes that collaborate through clear interfaces.
