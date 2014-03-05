package uk.co.malbec.cascade;


import uk.co.malbec.cascade.annotations.Step;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ScenarioFinder {
    
    List<Class> findScenarios(String[] paths, ClasspathScanner classpathScanner){
        List<Class> scenarios = new ArrayList<Class>();

        for (String path : paths) {
            classpathScanner.initialise(path);
            Set<Class<?>> steps = classpathScanner.getTypesAnnotatedWith(Step.class);

            for (Class<?> step : steps) {
                findScenarios(scenarios, step, classpathScanner);
            }
        }
        return scenarios;
    }

    private void findScenarios(List<Class> scenarios, Class<?> clazz, ClasspathScanner classpathScanner) {
        if (clazz.isInterface()) {
            Set<Class> subtypes = classpathScanner.getSubTypesOf(clazz);
            for (Class subType : subtypes) {
                findScenarios(scenarios, subType, classpathScanner);
            }
        } else {
            scenarios.add(clazz);
        }
    }
}