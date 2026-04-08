import enum


class State(str, enum.Enum): 
    ACQUIRED = "A"
    RETIRED = "R"
    TO_YARD = "TY"
    SWITCHING = "SW"
    AT_YARD = "AY"
    TO_STORAGE = "TYS"
    IN_STORAGE = "IYS"
    FROM_STORAGE = "FYS"
    UNDER_INSPECTION = "UI"
    BAD_ORDER = "BO"
    UNDER_REPAIR = "UR"
    TO_INDUSTRY = "TI"
    AT_INDUSTRY = "AI"
    TO_SPUR = "TIS"
    AT_SPUR = "AIS"
    FROM_SPUR = "FIS"
    LOADING = "L"
    UNLOADING = "U"