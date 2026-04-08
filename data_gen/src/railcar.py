import datetime
import uuid

from .state import State


class Railcar: 
    def __init__(self, id: uuid.UUID): 
        self.id: uuid.UUID = id
        self.state: State
        self.timestamp: datetime.datetime