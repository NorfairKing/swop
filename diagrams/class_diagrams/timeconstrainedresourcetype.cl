
!ifndef TIMECONSTRAINEDRESOURCETYPE_CL
!define TIMECONSTRAINEDRESOURCETYPE_CL

!include resourcetype.cl

class TimeConstrainedResourceType {
    TimePeriod: dailyAvailability
}
ResourceType <|-- TimeConstrainedResourceType

!endif

