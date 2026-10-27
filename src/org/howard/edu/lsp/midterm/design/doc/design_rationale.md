# Design Rationale – Evolving Requirements

## New requirements

1. Registration records must be stored either in a **local file** or a **database**, chosen by deployment environment.
2. Confirmations must be sent by either **email** or **text message**.

## How the Part 2 design accommodates them

The Part 2 design already isolates storage in `RegistrationRepository` and notification in `ConfirmationSender`, and `RegistrationService` only depends on what those classes do, not how they do it. So both changes are handled by turning those two classes into abstractions with interchangeable implementations. `RegistrationService`, `TuitionCalculator`, `Registration`, `Student` and `Course` do not change at all.

## Changes to the CRC cards

**RegistrationRepository** becomes an interface (abstract role). Its responsibilities stay the same ("save registration records; report success or failure"), and two new cards are added:

**Class:** FileRegistrationRepository
**Responsibilities:** Save registration records by appending them to a local file; report success or failure
**Collaborators:** Registration

**Class:** DatabaseRegistrationRepository
**Responsibilities:** Save registration records as rows in a database; report success or failure
**Collaborators:** Registration

**ConfirmationSender** becomes an interface (abstract role) with the responsibility "send a registration confirmation to the student." Two new cards are added:

**Class:** EmailConfirmationSender
**Responsibilities:** Send the confirmation to the student's email address
**Collaborators:** Registration, Student

**Class:** TextConfirmationSender
**Responsibilities:** Send the confirmation as a text message to the student's phone number
**Collaborators:** Registration, Student

**Student** keeps its card but now explicitly stores a phone number (already listed in Part 2) and a preferred contact method, so the right sender can be chosen per student.

One more card is added to decide which implementations are used:

**Class:** RegistrationConfiguration
**Responsibilities:** Read the deployment environment settings; create the correct RegistrationRepository (file or database) and provide the correct ConfirmationSender for a student
**Collaborators:** RegistrationRepository, ConfirmationSender, Student

`RegistrationService` is given its repository and senders by `RegistrationConfiguration`, so it never names a concrete storage or messaging class.

## Key responsibility decision: who decides file vs. database?

The choice of storage belongs to **RegistrationConfiguration**, not to `RegistrationService` and not to the repository classes.

- If `RegistrationService` chose the storage (for example, `if (env.equals("PROD")) ... else ...`), it would be back to the type-checking conditional the original design suffered from, and it would have to change every time a new storage option is added. That would give it a second reason to change, on top of coordinating registration.
- The repository classes should not decide either; each one should only know how to do its own kind of storage.
- Choosing the storage is a deployment concern. Putting it in one configuration class means that adding a third option (say, a cloud service) only requires writing a new `RegistrationRepository` implementation and updating the configuration. `RegistrationService` and the rest of the business logic stay closed to modification.

The same reasoning applies to choosing email or text: the configuration supplies the right `ConfirmationSender`, and `RegistrationService` simply asks it to "send a confirmation."
