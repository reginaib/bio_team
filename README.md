# bio_team
The aim of this Java project is to provide the opportunity for the bioinformatics team to work together on a nucleotide multiple sequence alignment. Significant updates have been made to the code, with a focus on various object-oriented concepts covered in the course. 
1. The project structure is as follows:
•	BioTeam Package: A core package containing the Main class as well as the three following subpackages.
•	Alignment Package: Consists of the Alignment abstract superclass (with two subclasses StandardAlignment and SNPAlignment), related interfaces (Editable, Set, and IO), and a Genome subpackage. The latter includes the Genome class and Editable and Sequence interfaces.
•	Repository Package: Houses Repository class for data management and the UserNotFound custom exception class for handling invalid users. 
•	Users Package: Manages users’ interactions through the User abstract superclass and its subclasses (Bioinformatician, TeamLead, and TechnicalSupport) as well as RepoAccessible and UserInfo interfaces.
The project structure has a wide range of advantages, most of which are related to efficient organization and maintainability. This modular approach streamlines collaboration between team members who are individually working on distinct components while facilitating focused development. This design decision makes it possible to incorporate additions or improvements in the future without having to completely revise the system. 
2. Interfaces:  
•	Genome Package: Sequence and Editable interfaces standardize genomic data handling.
•	Alignment Package: Set, Editable, and IO interfaces enhance alignment management and file operations.
•	Users Package: UserInfo and RepoAccessible interfaces ensure consistent user data handling and controlled repository access.
The implementation of interfaces significantly improves communication and interaction among various components of the framework. It eliminates code duplication and ensures a consistent set of methods for each interface. This consistency simplifies development and maintenance, making it easier to implement changes and enhancements across the project. Moreover, interfaces enhance the overall extensibility of the structure, allowing for the smooth integration of new features in the future. 
3.	Inheritance: 
•	Alignment Hierarchy: Alignment abstract superclass with StandardAlignment and SNPAlignment subclasses to represent different types of alignment.
•	Users Hierarchy: User abstract superclass with Bioinformatician, TeamLead and TechnicalSupport subclasses.
In the above cases, using inheritance improves the modularity, reusability, and organization of the code. It allows for a methodical approach to alignment and user task administration and offers an established structure of responsibilities and access levels. This approach efficiently simulates the complex interactions and roles within the bioinformatics team as well as the many alignment types by utilizing inheritance.
4.	The main method is organized in a way that focuses on demonstrating the functionality specific to each user. 
5.	Try-catch blocks are added when handling input. This improves code robustness by protecting against unexpected errors and ensuring graceful error handling. It is also useful to provide user-friendly error messages as it helps to achieve a better user experience and assists in debugging.

Note: When running the main method, the working directory should be the ‘data’ folder containing the config file.
