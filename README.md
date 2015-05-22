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

You will be asked to specify an initialisation file.
You can use any initialisation file in the `save_files` directory.
Not that you can also import a save file like this:

```
java -jar system.jar save_files/demo.json
```

Don't forget to enter the `help` command if you're unsure about what you can do.

## Running the demo
### DEMO1: Delegation
```
java -jar system.jar save_files/delegation_demo.json
```

We log in into the first branch office.

``` java
Welcome to TaskMan.
Enter "h" for help.

LOGIN:
SELECT BRANCH OFFICES
########
# 1) Location1
# 2) Location2
# ----------------------------------
Please pick a number [0-2] (0 to quit): 1
SELECT USERS
########
# 1) Ann
# 2) Bob
# 3) Charlie
# 4) Johan
# ----------------------------------
Please pick a number [0-4] (0 to quit): 4
```

(Johan is the manager.)

We're now going to create a new task to delegate.

``` java
> task
SELECT PROJECTS
########
# 1) Project 1 - Location1
# ----------------------------------
Please pick a number [0-1] (0 to quit): 1
CREATING TASK
########
# Description: Give the demo
# Estimated Duration (minutes): 60
# Acceptable Deviation (%): 0
SELECT RESOURCE TYPE
########
# 1) Car
# 2) Developer
# ----------------------------------
Please pick a number [0-2] (0 to quit): 1
Quantity required: 1
SELECT RESOURCE TYPE
########
# 1) Developer
# ----------------------------------
Please pick a number [0-1] (0 to quit): 1
Quantity required: 1
No resource type to select.
```

(One requirement of each resource type.)
Now we're going to start a simulation:

``` java
> simulation
```

We're going to delegate the task.

``` java
> delegate
SELECT TASK
########
# Project 1
    # 1) Give the demo

# ----------------------------------
Please pick a number [0-1] (0 to quit): 1
SELECT BRANCH OFFICES
########
# 1) Location1
# 2) Location2
# ----------------------------------
Please pick a number [0-2] (0 to quit): 2
Continue the simulation? ("continue", "realize" or "cancel")
continue
```

Continue the simulation because I want to show that the delegated task has not arrived at the other office yet.

``` java
> list
PROJECTS
########
# Delegated tasks
# Desc:    Tasks that have been delegated to this office.
# Office:  Location2
# Created: Friday, 22 May 2015 - 17:06
# Due:     Friday, 31 December +999999999 - 23:59
# ETA:     Monday, 1 January +1000000000 - 00:00
# ----------------------------------
# Delegated tasks
# Desc:    Tasks that have been delegated to this office.
# Office:  Location1
# Created: Friday, 22 May 2015 - 17:06
# Due:     Friday, 31 December +999999999 - 23:59
# ETA:     Monday, 1 January +1000000000 - 00:00
# ----------------------------------
# Project 1
# Desc:    This is Project 1
# Office:  Location1
# Created: Monday, 1 June 2015 - 09:00
# Due:     Friday, 5 June 2015 - 18:00
# ETA:     Monday, 1 June 2015 - 10:00
# ----------------------------------
# Project 2
# Desc:    This is Project 2
# Office:  Location2
# Created: Monday, 1 June 2015 - 09:00
# Due:     Monday, 22 June 2015 - 18:00
# ETA:     Monday, 1 June 2015 - 10:00
# ----------------------------------
SELECT PROJECTS
########
# 1) Delegated tasks - Location1
# 2) Delegated tasks - Location2
# 3) Project 1 - Location1
# 4) Project 2 - Location2
# ----------------------------------
Please pick a number [0-4] (0 to quit): 2
PROJECT
########
# Delegated tasks
# Desc:    Tasks that have been delegated to this office.
# Office:  Location2
# Created: Friday, 22 May 2015 - 17:06
# Due:     Friday, 31 December +999999999 - 23:59
# ETA:     Monday, 1 January +1000000000 - 00:00
# ----------------------------------
No tasks to select.
Continue the simulation? ("continue", "realize" or "cancel")
continue
```

As you can see: `No tasks to select.`
The task has been delegated though.

``` java
> list
PROJECTS
########
# Delegated tasks
# Desc:    Tasks that have been delegated to this office.
# Office:  Location2
# Created: Friday, 22 May 2015 - 17:06
# Due:     Friday, 31 December +999999999 - 23:59
# ETA:     Monday, 1 January +1000000000 - 00:00
# ----------------------------------
# Delegated tasks
# Desc:    Tasks that have been delegated to this office.
# Office:  Location1
# Created: Friday, 22 May 2015 - 17:06
# Due:     Friday, 31 December +999999999 - 23:59
# ETA:     Monday, 1 January +1000000000 - 00:00
# ----------------------------------
# Project 1
# Desc:    This is Project 1
# Office:  Location1
# Created: Monday, 1 June 2015 - 09:00
# Due:     Friday, 5 June 2015 - 18:00
# ETA:     Monday, 1 June 2015 - 10:00
# ----------------------------------
# Project 2
# Desc:    This is Project 2
# Office:  Location2
# Created: Monday, 1 June 2015 - 09:00
# Due:     Monday, 22 June 2015 - 18:00
# ETA:     Monday, 1 June 2015 - 10:00
# ----------------------------------
SELECT PROJECTS
########
# 1) Delegated tasks - Location1
# 2) Delegated tasks - Location2
# 3) Project 1 - Location1
# 4) Project 2 - Location2
# ----------------------------------
Please pick a number [0-4] (0 to quit): 3
PROJECT
########
# Project 1
# Desc:    This is Project 1
# Office:  Location1
# Created: Monday, 1 June 2015 - 09:00
# Due:     Friday, 5 June 2015 - 18:00
# ETA:     Monday, 1 June 2015 - 10:00
# ----------------------------------
SELECT TASKS
########
# 1) Give the demo, it is unavailable
# ----------------------------------
Please pick a number [0-1] (0 to quit): 1
TASK
########
# Give the demo
#   Dependencies: 0
#   Estimated Duration: 1 hour
#   Estimated finsih date: Monday, 1 June 2015 - 10:00
#   Acceptable Deviation: 0%
#   Still needs work
#   Cannot be executed by you at this point
#   Has been delegated to: Location2
# ----------------------------------
Continue the simulation? ("continue", "realize" or "cancel")
realize
```

Now we're going to finish up the simulation: `realize`.
At this point, the delegated task has arrived at the other office.

``` java
> list
PROJECTS
########
# Delegated tasks
# Desc:    Tasks that have been delegated to this office.
# Office:  Location2
# Created: Friday, 22 May 2015 - 17:06
# Due:     Friday, 31 December +999999999 - 23:59
# ETA:     Monday, 1 June 2015 - 10:00
# ----------------------------------
# Delegated tasks
# Desc:    Tasks that have been delegated to this office.
# Office:  Location1
# Created: Friday, 22 May 2015 - 17:06
# Due:     Friday, 31 December +999999999 - 23:59
# ETA:     Monday, 1 January +1000000000 - 00:00
# ----------------------------------
# Project 1
# Desc:    This is Project 1
# Office:  Location1
# Created: Monday, 1 June 2015 - 09:00
# Due:     Friday, 5 June 2015 - 18:00
# ETA:     Monday, 1 June 2015 - 10:00
# ----------------------------------
# Project 2
# Desc:    This is Project 2
# Office:  Location2
# Created: Monday, 1 June 2015 - 09:00
# Due:     Monday, 22 June 2015 - 18:00
# ETA:     Monday, 1 June 2015 - 10:00
# ----------------------------------
SELECT PROJECTS
########
# 1) Delegated tasks - Location1
# 2) Delegated tasks - Location2
# 3) Project 1 - Location1
# 4) Project 2 - Location2
# ----------------------------------
Please pick a number [0-4] (0 to quit): 2
PROJECT
########
# Delegated tasks
# Desc:    Tasks that have been delegated to this office.
# Office:  Location2
# Created: Friday, 22 May 2015 - 17:06
# Due:     Friday, 31 December +999999999 - 23:59
# ETA:     Monday, 1 June 2015 - 10:00
# ----------------------------------
SELECT TASKS
########
# 1) Give the demo, it is unavailable
# ----------------------------------
Please pick a number [0-1] (0 to quit): 1
TASK
########
# Give the demo
#   Dependencies: 0
#   Estimated Duration: 1 hour
#   Estimated finsih date: Monday, 1 June 2015 - 10:00
#   Acceptable Deviation: 0%
#   Still needs work
#   Cannot be executed by you at this point
# ----------------------------------
```

There is already a task planned in the second office:

``` java
> list
PROJECTS
########
# Delegated tasks
# Desc:    Tasks that have been delegated to this office.
# Office:  Location2
# Created: Friday, 22 May 2015 - 17:06
# Due:     Friday, 31 December +999999999 - 23:59
# ETA:     Monday, 1 June 2015 - 10:00
# ----------------------------------
# Delegated tasks
# Desc:    Tasks that have been delegated to this office.
# Office:  Location1
# Created: Friday, 22 May 2015 - 17:06
# Due:     Friday, 31 December +999999999 - 23:59
# ETA:     Monday, 1 January +1000000000 - 00:00
# ----------------------------------
# Project 1
# Desc:    This is Project 1
# Office:  Location1
# Created: Monday, 1 June 2015 - 09:00
# Due:     Friday, 5 June 2015 - 18:00
# ETA:     Monday, 1 June 2015 - 10:00
# ----------------------------------
# Project 2
# Desc:    This is Project 2
# Office:  Location2
# Created: Monday, 1 June 2015 - 09:00
# Due:     Monday, 22 June 2015 - 18:00
# ETA:     Monday, 1 June 2015 - 10:00
# ----------------------------------
SELECT PROJECTS
########
# 1) Delegated tasks - Location1
# 2) Delegated tasks - Location2
# 3) Project 1 - Location1
# 4) Project 2 - Location2
# ----------------------------------
Please pick a number [0-4] (0 to quit): 4
PROJECT
########
# Project 2
# Desc:    This is Project 2
# Office:  Location2
# Created: Monday, 1 June 2015 - 09:00
# Due:     Monday, 22 June 2015 - 18:00
# ETA:     Monday, 1 June 2015 - 10:00
# ----------------------------------
SELECT TASKS
########
# 1) Prove P==NP, it is unavailable
# ----------------------------------
Please pick a number [0-1] (0 to quit): 1
TASK
########
# Prove P==NP
#   Dependencies: 0
#   Estimated Duration: 1 hour
#   Estimated finsih date: Monday, 1 June 2015 - 10:00
#   Acceptable Deviation: 0%
#   Still needs work
#   Cannot be executed by you at this point
PLANNING: 
#   Planned start time: 2015-06-01T10:00
#   Planned duration: 60
#   Reservations: 
      # Car: Car 3
      # Developer: David
# ----------------------------------
```

Let's log into the second office as the manager (Maria).

``` java
> user
SELECT BRANCH OFFICES
########
# 1) Location1
# 2) Location2
# ----------------------------------
Please pick a number [0-2] (0 to quit): 2
SELECT USERS
########
# 1) David
# 2) Maria
# ----------------------------------
Please pick a number [0-2] (0 to quit): 2
```

We're going to plan the new task.

``` java
> plan
SELECT TASK
########
# Delegated tasks
    # 1) Give the demo

# ----------------------------------
Please pick a number [0-1] (0 to quit): 1
PLANNING:
Give the demo
# ----------------------------------
SELECT TIME
########
# 1) Monday, 1 June 2015 - 11:00
# 2) Monday, 1 June 2015 - 12:00
# 3) Monday, 1 June 2015 - 13:00
# ----------------------------------
Please pick a number [0-3] (0 to quit): 1
SELECT SELECT A TYPE
########
# 1) Car (1 options, 1 required )
# 2) Developer (1 options, 1 required )
# ----------------------------------
Please pick a number [0-2] (0 to quit): 1
SELECT RESOURCE FOR Car
########
# 1) Car 3
# ----------------------------------
Please pick a number [0-1] (0 to quit): 1
SELECT SELECT A TYPE
########
# 1) Car (1 options, 1 required , 1 selected )
# 2) Developer (1 options, 1 required )
# ----------------------------------
Please pick a number [0-2] (0 to quit): 2
SELECT RESOURCE FOR Developer
########
# 1) David
# ----------------------------------
Please pick a number [0-1] (0 to quit): 1
SELECT SELECT A TYPE
########
# 1) Car (1 options, 1 required , 1 selected )
# 2) Developer (1 options, 1 required , 1 selected )
# ----------------------------------
Please pick a number [0-2] (0 to quit): 0
The developer(s) could take a break during this Task.
Would you like for the planning to add a break? (y/n/exit): n
```

No break, because Maria is a horrible manager.
Now let's log in as David.

``` java
> user
SELECT BRANCH OFFICES
########
# 1) Location1
# 2) Location2
# ----------------------------------
Please pick a number [0-2] (0 to quit): 2
SELECT USERS
########
# 1) David
# 2) Maria
# ----------------------------------
Please pick a number [0-2] (0 to quit): 1
```

Fast-forward to 10 am.

``` java
> clock
TIME STAMP
########
# Time: 2015-06-01 10:00
```

David starts executing the task: `Prove P==NP`.

``` java
> update
SELECT TASK
########
# Delegated tasks
    # 1) Give the demo
# Project 2
    # 2) Prove P==NP

# ----------------------------------
Please pick a number [0-2] (0 to quit): 2
# Execute or fail the task (execute/fail): execute
# Use the resources from the planning or allocate new resources? (plan/new/exit): plan
```

Fast forward to 11:30.

``` java
> clock
TIME STAMP
########
# Time: 2015-06-01 11:30
```

Now the task is late, so we'll have a conflict.

``` java
> update
SELECT TASK
########
# Delegated tasks
    # 1) Give the demo
# Project 2
    # 2) Prove P==NP

# ----------------------------------
Please pick a number [0-2] (0 to quit): 2
# Was the task successful (finish/fail): finish
PLANNING:
Give the demo
# ----------------------------------
SELECT TIME
########
# 1) Monday, 1 June 2015 - 12:00
# 2) Monday, 1 June 2015 - 13:00
# 3) Monday, 1 June 2015 - 14:00
# ----------------------------------
Please pick a number [0-3] (0 to quit): 1
```

As you can see, there is a conflict, so we have to re-plan giving the demo.
Fast-forward to 12:00am.

``` java
> clock
TIME STAMP
########
# Time: 2015-06-01 12:00
```

David starts giving the demo:

``` java
> update
SELECT TASK
########
# Delegated tasks
    # 1) Give the demo
# Project 2
    # 2) Prove P==NP

# ----------------------------------
Please pick a number [0-2] (0 to quit): 1
# Execute or fail the task (execute/fail): execute
# Use the resources from the planning or allocate new resources? (plan/new/exit): plan
```

Fast-forward to 12:30.

``` java
> clock
TIME STAMP
########
# Time: 2015-06-01 12:30
```

Dave finished giving the demo.

``` java
update
SELECT TASK
########
# Delegated tasks
    # 1) Give the demo
# ----------------------------------
Please pick a number [0-2] (0 to quit): 1
# Was the task successful (finish/fail): finish
```

Let's see about the status of the demo task now:

``` java
> list
PROJECTS
########
# Delegated tasks
# Desc:    Tasks that have been delegated to this office.
# Office:  Location2
# Created: Friday, 22 May 2015 - 17:06
# Due:     Friday, 31 December +999999999 - 23:59
# ETA:     Monday, 1 June 2015 - 12:30
#   Is finished
# ----------------------------------
# Delegated tasks
# Desc:    Tasks that have been delegated to this office.
# Office:  Location1
# Created: Friday, 22 May 2015 - 17:06
# Due:     Friday, 31 December +999999999 - 23:59
# ETA:     Monday, 1 January +1000000000 - 00:00
# ----------------------------------
# Project 1
# Desc:    This is Project 1
# Office:  Location1
# Created: Monday, 1 June 2015 - 09:00
# Due:     Friday, 5 June 2015 - 18:00
# ETA:     Monday, 1 June 2015 - 12:30
#   Is finished
# ----------------------------------
# Project 2
# Desc:    This is Project 2
# Office:  Location2
# Created: Monday, 1 June 2015 - 09:00
# Due:     Monday, 22 June 2015 - 18:00
# ETA:     Monday, 1 June 2015 - 11:30
#   Is finished
# ----------------------------------
SELECT PROJECTS
########
# 1) Delegated tasks - Location1
# 2) Delegated tasks - Location2
# 3) Project 1 - Location1
# 4) Project 2 - Location2
# ----------------------------------
Please pick a number [0-4] (0 to quit): 3
PROJECT
########
# Project 1
# Desc:    This is Project 1
# Office:  Location1
# Created: Monday, 1 June 2015 - 09:00
# Due:     Friday, 5 June 2015 - 18:00
# ETA:     Monday, 1 June 2015 - 12:30
#   Is finished
# ----------------------------------
SELECT TASKS
########
# 1) Give the demo, it is finished and was finished early
# ----------------------------------
Please pick a number [0-1] (0 to quit): 1
TASK
########
# Give the demo
#   Dependencies: 0
#   Estimated Duration: 1 hour
#   Estimated finsih date: Monday, 1 June 2015 - 12:30
#   Acceptable Deviation: 0%
#   Is Finished
#   Performed During: Mon 01/06/2015 12:00 --> Mon 01/06/2015 12:30
#   Was finished early
#   Cannot be executed by you at this point
#   Has been delegated to: Location2
# ----------------------------------
```

Tadaah! The task is finished and it was delegated too.
