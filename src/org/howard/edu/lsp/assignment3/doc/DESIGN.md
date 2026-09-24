How was your Assignment #2 solution organized?
My Assignment #2 solution was a single ETLPipeline class made of static methods that controlled everything from one central place. It resolved file paths, read and validated the CSV, calculated pay, applied the pay rules, and wrote the output file. EmployeeRecord was a nested class that only held data, and ETLPipeline read its private fields directly. This made ETLPipeline a behavioral God class, which goes against Riel's heuristic "Do not create God classes/objects in your system.   

What design changes did you make for Assignment #3?
I replaced centralized control with a decentralized design where each class handles one step of the pipeline. ETLPipeline now only creates the components and runs extract, transform, and load in order. Pay calculations moved into the Employee class, and the pay rule constants moved into PayPolicy. All output and console results stayed exactly the same as in Assignment #2.

What classes or abstractions did you introduce and why?
I introduced EmployeeCsvReader to read and split rows, EmployeeTransformer to validate rows and turn them into employees, and EmployeeCsvWriter to write the output file. Employee is an immutable object that calculates its own gross pay, pay level, and employment status, and PayPolicy keeps the payroll rules in one named place. RunSummary replaced ExtractionResult and tracks the row counts through record methods instead of exposed fields. Each class captures one key abstraction, and I avoided making separate classes for small operations like pay level to follow Riel's heuristic "Do not turn an operation into a class."

How did you divide responsibilities differently?
Instead of one class making every decision, responsibilities are now distributed across classes, following Riel's heuristic "Distribute system intelligence horizontally as uniformly as possible." The reader only handles file input, the transformer only handles validation, and the writer only handles output formatting. Employee keeps its data and pay behavior together, following "Keep related data and behavior in one place." Fields are private, helper methods are private, and each class exposes only the methods other classes need.

Why do you believe your Assignment #3 design is an improvement?
The new design is easier to read, change, and test because each class has a single responsibility. If a pay rule changes, I only edit PayPolicy or Employee, and if the file format changes, I only edit the reader or writer. The God class is gone, data is better encapsulated, and the classes model real payroll concepts without adding unnecessary classes. The program is better organized and still produces the same output as Assignment #2.

https://claude.ai/share/4d85cf2c-592b-47af-aaeb-8f8af814f32f 

