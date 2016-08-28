package com.innocept.taximastercustomer;

import com.innocept.taximastercustomer.model.foundation.TaxiType;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ExampleUnitTest {

    @Test
    public void addition_isCorrect() throws Exception {
        System.out.println(TaxiType.getEnum(3));
    }
}