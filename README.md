# Players
Given a Player class - an instance of which can communicate with other Players.

# The requirements are as follows:

1. create 2 Player instances
2. one of the players should send a message to second player (let's call this player "initiator")
3. when a player receives a message, it should reply with a message that contains the received message concatenated with the value of a counter holding the number of messages this player already sent.
4. finalize the program (gracefully) after the initiator sent 10 messages and received back 10 messages (stop condition)
5. both players should run in the same java process (strong requirement)
6. document for every class the responsibilities it has.
7. additional challenge (nice to have) opposite to 5: have every player in a separate JAVA process.

Please use core Java as much as possible without additional frameworks like Spring etc; focus on design and not on the technology.
Please include a maven project with the source code to build the jar and a shell script to start the program.
Everything not specified is to be decided by you; everything specified is a hard requirement.

# Requirements
- Maven 3 (using mvn 3.8.1 version)
- Java 17 (using jdk-17.0.1)

# Build
Compile the Application using build.bat or execute.

mvn clean install

# Run
To run the application in the same process please use the file run.bat file or execute 

java -jar .\eventbus\target\eventbus-0.0.2-jar-with-dependencies.jar


To run every player running in a different JAVA process please run the file receiver.bat then initiator.bat or execute

java -jar .\eventbus\target\eventbus-0.0.2-jar-with-dependencies.jar [khaled]

java -jar .\eventbus\target\eventbus-0.0.2-jar-with-dependencies.jar initiator

NB: the initiator name should be initiator