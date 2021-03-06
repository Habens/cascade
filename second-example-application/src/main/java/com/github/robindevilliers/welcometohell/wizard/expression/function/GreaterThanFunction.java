package com.github.robindevilliers.welcometohell.wizard.expression.function;

import com.github.robindevilliers.welcometohell.wizard.expression.Function;

import java.time.LocalDate;
import java.util.Map;

public class GreaterThanFunction implements Function<Boolean> {
    private Function<?> lhs;
    private Function<?> rhs;

    public GreaterThanFunction(Function<?> lhs, Function<?> rhs){
        this.lhs = lhs;
        this.rhs = rhs;
    }

    @Override
    public Boolean apply(Map<String, Object> scope) {
        Object lhs = this.lhs.apply(scope);
        Object rhs = this.rhs.apply(scope);

        if (lhs instanceof LocalDate && rhs instanceof LocalDate){
            LocalDate leftDate = (LocalDate) lhs;
            LocalDate rightDate = (LocalDate) rhs;

            return leftDate.isAfter(rightDate);
        } else if (lhs instanceof Integer && rhs instanceof Integer) {
            Integer leftInt = (Integer) lhs;
            Integer rightInt = (Integer) rhs;

            return leftInt > rightInt;
        } else {
            throw new RuntimeException("Invalid expression.  Unsupported types.");
        }
    }
}
