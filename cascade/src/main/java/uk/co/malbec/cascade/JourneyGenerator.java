package uk.co.malbec.cascade;


import uk.co.malbec.cascade.model.Journey;

import java.util.List;

public interface JourneyGenerator {
    public List<Journey> generateJourneys(List<Class> scenarios, Class<?> controlClass);

}