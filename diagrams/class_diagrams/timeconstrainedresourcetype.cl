!include resourcetype.cl

class TimeConstrainedResourceType {
    TimePeriod: dailyAvailability
}
ResourceType <|-- TimeConstrainedResourceType
