package com.sundbybergsit.calculation;

public interface Calculator<T> {

    Number calculate(T calculee, Number number);
}
