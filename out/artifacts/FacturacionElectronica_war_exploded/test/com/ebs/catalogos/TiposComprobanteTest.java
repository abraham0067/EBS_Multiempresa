package com.ebs.catalogos;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by eflores on 24/11/2017.
 */
@DisplayName("TiposComprobanteTest")
class TiposComprobanteTest {
    @BeforeAll
    static void beforeAll() {
        System.out.println("Before all test methods");
    }

    @BeforeEach
    void beforeEach() {
        System.out.println("Before each test method");
    }

    @AfterEach
    void afterEach() {
        System.out.println("After each test method");
    }

    @AfterAll
    static void afterAll() {
        System.out.println("After all test methods");
    }

    @Test
    void myFirstTest() {
        assertEquals(2, 1 + 1);
    }

    @Test
    void groupedAssertions() {
        // In a grouped assertion all assertions are executed, and any
        // failures will be reported together.
        assertAll("TiposComprobante",
                () -> assertEquals(0, TiposComprobante.INGRESO),
                () -> assertEquals(1, TiposComprobante.EGRESO),
                () -> assertEquals(2, TiposComprobante.TRASLADO),
                () -> assertEquals(3, TiposComprobante.NOMINA),
                () -> assertEquals(4, TiposComprobante.PAGO)

        );
    }

}