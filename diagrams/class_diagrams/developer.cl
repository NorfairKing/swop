
!ifndef DEVELOPER_CL
!define DEVELOPER_CL

 
!include user.cl
!include resource.cl

class Developer << (V,orange) >>
Resource <|-- Developer
User <|-- Developer
 

!endif

