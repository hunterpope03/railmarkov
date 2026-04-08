import datetime
import pydantic
import uuid

from .state import State


class Event(pydantic.BaseModel): 
    railcar_id: uuid.UUID
    state: State
    timestamp: datetime.datetime