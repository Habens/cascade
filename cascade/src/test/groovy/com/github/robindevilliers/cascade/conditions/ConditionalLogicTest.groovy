package com.github.robindevilliers.cascade.conditions

import org.junit.Test
import com.github.robindevilliers.cascade.Scenario
import com.github.robindevilliers.cascade.annotations.Step
import com.github.robindevilliers.cascade.annotations.Terminator

import static Predicates.*

class ConditionalLogicTest {

    List<Scenario> steps = [new Scenario(OpenLoginPage.class, OpenLoginPage), new Scenario(Successful.class, Successful),
                            new Scenario(PostLoginAlert.UserAccountLockedAlert.class, PostLoginAlert.UserAccountLockedAlert)] as List<Scenario>

    @Test
    def void "the withStep predicate should return true"() {
        assert new ConditionalLogic().matches(withStep(Successful), steps)
    }

    @Test
    def void "the stepAt predicate should return true"() {
        assert new ConditionalLogic().matches(stepAt(1, Successful), steps)
    }

    @Test
    def void "the stepAt predicate should return false"() {
        assert !new ConditionalLogic().matches(stepAt(0, Successful), steps)
    }

    @Test
    def void "the not predicate enclosing the withStep predicate should return false"() {
        assert !new ConditionalLogic().matches(not(withStep(Successful)), steps)
    }

    @Test
    def void "the withStep predicate should return false"() {
        assert !new ConditionalLogic().matches(withStep(BadPassword), steps)
    }

    @Test
    def void "the and predicate should return true"() {
        assert new ConditionalLogic().matches(and(withStep(OpenLoginPage), withStep(Successful)), steps)
    }

    @Test
    def void "the and predicate should return false"() {
        assert !new ConditionalLogic().matches(and(withStep(OpenLoginPage), withStep(BadPassword)), steps)
    }

    @Test
    def void "the or predicate should return true"() {
        assert new ConditionalLogic().matches(or(withStep(OpenLoginPage), withStep(BadPassword)), steps)
    }

    @Test
    def void "the or predicate should return true 2"() {
        assert new ConditionalLogic().matches(or(withStep(BadPassword), withStep(Successful)), steps)
    }

    @Test
    def void "the or predicate should return false"() {
        assert !new ConditionalLogic().matches(or(withStep(BadPassword), withStep(PostLoginAlert.InformationAlert)), steps)
    }

    @Test
    def void "a and composing or should return true"() {
        assert new ConditionalLogic().matches(and(withStep(OpenLoginPage), or(withStep(BadPassword), withStep(Successful))), steps)
    }

    @Test
    def void "a and composing or should return false because of and"() {
        assert !new ConditionalLogic().matches(and(withStep(PostLoginAlert.InformationAlert), or(withStep(BadPassword), withStep(Successful))), steps)
    }

    @Test
    def void "a and composing or should return false because of or"() {
        assert !new ConditionalLogic().matches(and(withStep(OpenLoginPage), or(withStep(BadPassword), withStep(PostLoginAlert.InformationAlert))), steps)
    }

    @Test
    def void "a or composing and should return true"() {
        assert new ConditionalLogic().matches(or(withStep(BadPassword), and(withStep(OpenLoginPage), withStep(Successful))), steps)
    }

    @Test
    def void "a or composing and should return false because of and"() {
        assert !new ConditionalLogic().matches(or(withStep(BadPassword), and(withStep(OpenLoginPage), withStep(PostLoginAlert.InformationAlert))), steps)
    }

    @Test
    def void "a or composing and should return true because of or"() {
        assert new ConditionalLogic().matches(or(withStep(PostLoginAlert.InformationAlert), and(withStep(OpenLoginPage), withStep(Successful))), steps)
    }

    @Test
    def void "a 'not' composing 'or' composing 'and' should return true"() {
        assert !new ConditionalLogic().matches(not(or(withStep(PostLoginAlert.InformationAlert), and(withStep(OpenLoginPage), withStep(Successful)))), steps)
    }

    @Step
    static class OpenLoginPage {
    }

    @Step([OpenLoginPage])
    static class BadPassword {
    }

    @Step([OpenLoginPage, BadPassword])
    static class Successful {
    }

    @Step([Successful])
    static interface PostLoginAlert {

        @Terminator
        static class UserAccountLockedAlert implements PostLoginAlert {
        }

        static class InformationAlert implements PostLoginAlert {
        }
    }

}
