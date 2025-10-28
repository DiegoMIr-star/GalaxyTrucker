# Software Engineering Final Examination 2024/2025 -- Galaxy Trucker
---

Disclaimer:
This project is a non-commercial academic work based on a board game owned by Cranio Creations S.r.l.
All rights to the original game, its rules and related materials are the exclusive property of Cranio Creations S.r.l.

---

- Diego Mirabella
- Giulio Modolo
- Sara Patano
- Federica Sofia Paris

---
## Implemented features
We have designed the digital version of the board game "Galaxy Trucker" by Cranio Creations, developing the following features:

- complete rules
- two different connections, Socket and RMI (Socket on port 12345 and RMI on port 1099)
- two different user interfaces, TUI (text user interface) and GUI (graphical user interface)
- two different AF (advanced functionalities): persistence and resilience to disconnections

The software is designed according to the MVC pattern and respecting the object oriented paradigm, for example using the Visitor pattern to handle object dispatching in a
flexible and scalable way based on their runtime type. The repository doesn't contain the images of cards and components, because of copyright issues.

---
## Tests and documentation
The program is supported by a rich documentation written using javadoc and by numerous class tests for an effective coverage of Model and Controller:

|                   | Line Coverage | Branch Coverage|
|-------------------|---------------|----------------|
|   Package Model   |      86%      |       76%      |
| Controller Class  |      92%      |       74%      |

For the implementation of the tests, Mockito was used, especially to effectively test the Controller. It was configured following these steps:
```Run → Edit Configurations Template → JUnit```, then in the ```VM options``` field, next to ```-ea```, you need to add: 
```
-Dnet.bytebuddy.experimental=true
```
Then just click ```apply```.

---
## Diagrams
For a better explanation of the software some diagrams are attached in the "deliverables" folder, there are:

- class diagram for the [Model Package](deliverables/Final/UML/ClassDiagrams/Model_Class_Diagram.drawio.drawio.pdf) (better to be downloaded for a better visualization)
- class diagram for the [RMI package](deliverables/Final/UML/ClassDiagrams/RMI_Diagram.drawio.pdf) (better to be downloaded for a better visualization)
- class diagram for the [Socket package](deliverables/Final/UML/ClassDiagrams/Socket_Diagram.drawio.pdf) (better to be downloaded for a better visualization)
- [3 different sequence diagrams](deliverables/Final/UML/SequenceDiagrams) with explanation for a better understanding of the connections (better to be downloaded for a better visualization)

---
## JAR
The JAR executables for both client and server are provided. To launch the client, use the command:
```
java -jar path_to_the_client_jar
```
While the server requires:
```
java -jar path_to_the_server_jar -rmi -socket
```
Thanks to the persistence feature, any unfinished game automatically saves a ```.txt``` file in the directory from which the terminal is launched on the user's machine running the server

Due to copyright reasons, the repository does not include the original images or the JAR files containing the final application. The history of the repository is not available for the same reason.

---
