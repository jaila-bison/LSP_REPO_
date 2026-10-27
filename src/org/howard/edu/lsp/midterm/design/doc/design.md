# Proposed Design – CRC Cards

The redesign splits the original `CourseRegistration` class into focused classes, one per key abstraction or concern. `RegistrationService` coordinates the workflow; each other class does one job.

---

**Class:** Student
**Responsibilities:**
- Store student information (ID, name, email, phone number)
- Keep its data private and validate it (for example, a non-empty ID and a well-formed email)
- Provide student details to other classes

**Collaborators:** None

---

**Class:** Course
**Responsibilities:**
- Store course information (course code, credit hours)
- Validate that credit hours are positive
- Provide course details to other classes

**Collaborators:** None

---

**Class:** Registration
**Responsibilities:**
- Represent one student enrolled in one course
- Store the student, the course, the tuition charged, and the registration timestamp
- Provide registration details (for example, a formatted record for storage or confirmation)

**Collaborators:** Student, Course

---

**Class:** TuitionCalculator
**Responsibilities:**
- Hold the tuition rules (rate per credit hour, full-load threshold, discount)
- Calculate tuition for a given number of credit hours, applying the discount when the load is 12 or more hours

**Collaborators:** None

---

**Class:** RegistrationRepository
**Responsibilities:**
- Save registration records
- Report whether a save succeeded or failed (instead of hiding errors)

**Collaborators:** Registration

---

**Class:** ConfirmationSender
**Responsibilities:**
- Send a registration confirmation to the student

**Collaborators:** Registration, Student

---

**Class:** RegistrationLogger
**Responsibilities:**
- Record that a registration was processed, with its timestamp

**Collaborators:** Registration

---

**Class:** RegistrationService
**Responsibilities:**
- Coordinate the registration workflow:
  1. validate the student and course,
  2. ask TuitionCalculator for the tuition,
  3. create a Registration,
  4. save it through RegistrationRepository,
  5. only after a successful save, send a confirmation and log the event
- Report failure to the caller if any step fails, so the student is never told they are registered when they are not

**Collaborators:** Student, Course, Registration, TuitionCalculator, RegistrationRepository, ConfirmationSender, RegistrationLogger

---

## How this fixes the original problems

- **Encapsulation:** every class keeps its data private; Student and Course validate their own fields.
- **Single responsibility:** pricing, persistence, notification and logging each live in their own class, so a change to one doesn't touch the others.
- **Correct abstractions:** Student and Course are separate; a Registration relates them instead of copying their fields.
- **Correctness:** RegistrationService saves first and confirms only on success, and errors are reported instead of swallowed.
- **Testability:** each class can be tested on its own, and TuitionCalculator can be tested without any file or console I/O.
