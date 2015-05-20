
!ifndef FAILEDSTATUS_CL
!define FAILEDSTATUS_CL

!include completedstatus.cl
class FailedStatus << (V,orange) >>
CompletedStatus <|-- FailedStatus

!endif

