package com.github.robindevilliers.cascade

import com.github.robindevilliers.cascade.annotations.Scan
import com.github.robindevilliers.cascade.annotations.Step
import com.github.robindevilliers.cascade.conditions.ConditionalLogic
import com.github.robindevilliers.cascade.model.Journey
import com.github.robindevilliers.cascade.modules.*
import com.github.robindevilliers.cascade.modules.reporter.RenderingSystem
import com.github.robindevilliers.cascade.utils.Reference
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.Description
import org.junit.runner.notification.RunNotifier

import static org.mockito.Matchers.any
import static org.mockito.Matchers.eq
import static org.mockito.Mockito.*

class CascadeTest {

    Cascade cascade;

    Scanner classpathScannerMock = mock(Scanner)

    JourneyGenerator journeyGeneratorMock = mock(JourneyGenerator)

    ConstructionStrategy constructionStrategyMock = mock(ConstructionStrategy)

    TestExecutor testExecutorMock = mock(TestExecutor)

    FilterStrategy filterStrategyMock = mock(FilterStrategy)

    CompletenessStrategy completenessStrategy = mock(CompletenessStrategy)

    RunNotifier runNotifierMock = mock(RunNotifier)

    Reporter reporterMock = mock(Reporter)

    TestReport testReport = mock(TestReport)

    RenderingSystem renderingSystemMock = mock(RenderingSystem)

    @Before
    public void "initialisation"() {
        when(reporterMock.createTestReport()).thenReturn(testReport)
        cascade = new Cascade(classpathScannerMock, journeyGeneratorMock, constructionStrategyMock, testExecutorMock, filterStrategyMock, completenessStrategy, reporterMock, renderingSystemMock);
    }

    @After
    public void "cleanup"(){
        verifyNoMoreInteractions(classpathScannerMock, journeyGeneratorMock, constructionStrategyMock, testExecutorMock, filterStrategyMock)
    }

    @Test
    public void "given start of test, the cascade class should find scenarios and delegate to journey generator to generate journeys"() {
        //given
        List<Scenario> scenarios = []
        when(classpathScannerMock.findScenarios(any(String[]))).thenReturn(scenarios)

        List<Journey> journeysMock = mock(List)
        when(journeyGeneratorMock.generateJourneys(any(List), any(Class), eq(filterStrategyMock), eq([:]))).thenReturn(journeysMock)
        when(completenessStrategy.filter(journeysMock)).thenReturn(journeysMock)

        //When
        cascade.init(TestClass);

        //then
        verify(journeyGeneratorMock).init(any(ConditionalLogic.class))
        verify(classpathScannerMock).findScenarios(["uk.co.this", "uk.co.that"] as String[])
        verify(filterStrategyMock).init(TestClass, [:]);
        verify(testExecutorMock).init(TestClass, [:]);
        verify(journeyGeneratorMock).generateJourneys(scenarios, TestClass, filterStrategyMock, [:])
        verify(completenessStrategy).filter(journeysMock)

        assert journeysMock == cascade.journeys
        assert TestClass == cascade.controlClass
    }

    @Test
    public void "given a generated cascade test suit, a call to getDescription should generate a description for each journey"() {
        //given
        cascade.journeys = [new Journey([new Scenario(Her.class, Her), new Scenario(Him.class, Him)], TestClass), new Journey([new Scenario(Him.class, Him), new Scenario(Her.class, Her)], TestClass)]

        int i = 1;
        for (Journey journey : cascade.journeys){
            journey.init(i++)
        }

        //when
        Description description = cascade.getDescription()

        //then
        assert description.displayName == 'Cascade Tests'
        List<Description> children = description.getChildren()
        assert children.size() == 2
        assert children[0].displayName == 'Test[1]  CascadeTest$Her  CascadeTest$Him (com.github.robindevilliers.cascade.CascadeTest$TestClass)'
        assert children[1].displayName == 'Test[2]  CascadeTest$Him  CascadeTest$Her (com.github.robindevilliers.cascade.CascadeTest$TestClass)'
    }

    @Test
    public void "given a request to run, the cascade class should setup, execute and then teardown all journeys "() {
        //given
        Map<String, Scope> scope = [:]
        List<Journey> journeys = [new Journey([new Scenario(Her.class, Her), new Scenario(Him.class, Him)], TestClass), new Journey([new Scenario(Him.class, Him), new Scenario(Her.class, Her)], TestClass)]
        cascade.journeys = journeys
        int i = 1;
        for (Journey journey : cascade.journeys){
            journey.init(i++)
        }
        cascade.controlClass = TestClass

        //when
        cascade.run(runNotifierMock);

        //then
        verify(constructionStrategyMock).setup(eq(TestClass), eq(journeys[0]), any(Reference), any(Reference), any(Map));
        verify(testExecutorMock).executeTest(eq(runNotifierMock), eq(journeys[0].getDescription()), eq(null), eq(journeys[0]), eq(testReport), any(Map));
        verify(constructionStrategyMock).tearDown(any(Reference), eq(journeys[0]), any(Reference));

        verify(constructionStrategyMock).setup(eq(TestClass), eq(journeys[1]), any(Reference), any(Reference), any(Map));
        verify(testExecutorMock).executeTest(eq(runNotifierMock), eq(journeys[1].getDescription()), eq(null), eq(journeys[1]), eq(testReport), any(Map));
        verify(constructionStrategyMock).tearDown(any(Reference), eq(journeys[1]), any(Reference));
    }

    @Step
    public static class Her {

    }

    @Step
    public static class Him {

    }

    @Scan(["uk.co.this", "uk.co.that"])
    public static class TestClass {

    }
}
