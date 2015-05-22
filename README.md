# SWOP Iteratie 3

## Contents
- diagrams
  - class diagrams
    - `entire_domain_uml.eps`: class diagram of the entire domain
    - `*_uml.eps`: class diagrams of a specific part of the domain
    - `*_asso.eps`: class diagrams of single classes and their associations
    - `*_class.eps`: class diagrams of single classes
  - domain model
  - fsms
    - `project_fsm.eps`: state diagram for a project
    - `task_fsm.eps`: state diagram for a task
  - sequence diagrams
- doc
- save files
- src
- system.jar
- README.txt

## Execution
To start the system, run the following command:

```
java -jar system.jar
```

You will be asked to specifiy an initialisation file.
You can use any initialisation file in the `save_files` directory.
Not that you can also import a save file like this:

```
java -jar system.jar save_files/demo.json
```

Don't forget to enter the `help` command if you're unsure about what you can do.

## Running the demo
TODO
