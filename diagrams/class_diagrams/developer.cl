
!ifndef DEVELOPER_CL
!define DEVELOPER_CL

 
!include user.cl
!include reservable.cl

class Developer
Reservable <|-- Developer
User <|-- Developer
 

!endif

