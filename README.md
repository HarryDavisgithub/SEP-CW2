# Tinee - a third year university project based around Software Engineering Practices, Refactoring and Redesign

This project involved taking a provided code base and redesigning it. This meant:
- Refactoring code based on design pattern
- Implimenting MVC
- Internationalisation-  the generalisation of the Tinee project for use with other
languages
- Resolving bugs
- Use of JUnits to preform systimatic tests after changes
- Determining test code coverage (Using JaCoCo)
- Static code analysis (Using FindBugs and EasyPMD) - Used to improve the quality of code and fix possible future issues.

# Application introduction
Welcome to Tinee, a simple text-based Ticket System! It allows users to create tickets to log various issues.

It can be run from command line using:
C:\...\tinee>  java -cp build\classes Client userid localhost 8888 

Where userid is replaced with your user name.
C:\...\tinee> represents the location of the files. This can be changed by using "cd <path to tinee>" in the console.

It also needs a server to run in command line using:
C:\...\tinee> java -cp build\classes sep.tinee.server.Server 8888
