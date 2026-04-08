import math
import pydantic_settings
import yaml

from .state import State


class App(pydantic_settings.BaseSettings): 
    CASSANDRA_KEYSPACE: str
    CASSANDRA_KEYSPACE_REPLICATION: str
    CASSANDRA_TABLE: str
    DATA_GEN_RAILCARS: int
    DATA_GEN_TRANSITIONS_PER_RAILCAR: int

    model_config = pydantic_settings.SettingsConfigDict(
        env_file = "config/app.env", 
        strict = True
    )

class StateTransitionProbabilities(): 
    def __init__(self): 
        raw = self._load()
        self.data = self._validate(raw)

    def _load(self) -> dict: 
        with open(file = "config/state-transition-probabilities.yaml", mode = "r") as file: 
            return yaml.safe_load(file)
        
    def _validate(self, raw: dict) -> dict[State, dict[State, float]]: 
        validated = {}

        for state, transitions in raw.items(): 
            try: assert (state in (State._value2member_map_))
            except: raise RuntimeError(state)

            if state == State.RETIRED: 
                assert (transitions == None)

            else: 
                transition_probability_sum = 0

                for transition_state, transition_probability in transitions.items(): 
                    try: assert (transition_state in (State._value2member_map_))
                    except: raise RuntimeError(state)

                    transition_probability_sum += transition_probability

                try: assert (math.ceil(transition_probability_sum) == 1)
                except: raise RuntimeError(state, math.ceil(transition_probability_sum))
        
            validated[state] = transitions

        return validated
    
    def get(self, state: State) -> dict[State, float]:
        return self.data[state]
    
class Config: 
    app: App = App() # type: ignore
    state_transition_probabilities: StateTransitionProbabilities = StateTransitionProbabilities()