package com.example.jatcool.zno_on_math.util;

import org.junit.Test;

import static org.junit.Assert.*;

public class ValidationTests {

    @Test
    public void isValidEmail() {
        boolean actualResult1 = Validation.isValidEmail("pronumber19@gmail.com");
        boolean actualResult2 = Validation.isValidEmail("pмчвчвмчм19@gmail.com");
        boolean actualResult3 = Validation.isValidEmail("фвцвф");
        boolean actualResult4 = Validation.isValidEmail("pronumber19@gmail.");
        boolean actualResult5 = Validation.isValidEmail("pronumber19@gmail.cm");
        boolean actualResult6 = Validation.isValidEmail("pronumber19@com");
        boolean actualResult7 = Validation.isValidEmail("pronumbe_r19@gmail.com");
        boolean actualResult8 = Validation.isValidEmail("pronumbe-r19@gmail.com");
        boolean actualResult9 = Validation.isValidEmail("pronum@ber19@gmail.com");

        assertTrue(actualResult1);
        assertFalse(actualResult2);
        assertFalse(actualResult3);
        assertFalse(actualResult4);
        assertFalse(actualResult5);
        assertFalse(actualResult6);
        assertTrue(actualResult7);
        assertTrue(actualResult8);
        assertTrue(actualResult9);
    }

    @Test
    public void isValidName() {
        boolean actualResult1 = Validation.isValidName("wad");
        boolean actualResult2 = Validation.isValidName("Bohdan");
        boolean actualResult3 = Validation.isValidName("Bohdan_Kasumov");
        boolean actualResult4 = Validation.isValidName("_!_2_2");
        boolean actualResult5 = Validation.isValidName("ццццц");
        boolean actualResult6 = Validation.isValidName("      ");
        boolean actualResult7 = Validation.isValidName("ФЫЫЫЫ_;;");
        boolean actualResult8 = Validation.isValidName("pe4enika");
        boolean actualResult9 = Validation.isValidName("@..$?");

        assertFalse(actualResult1);
        assertTrue(actualResult2);
        assertTrue(actualResult3);
        assertTrue(actualResult4);
        assertTrue(actualResult5);
        assertFalse(actualResult6);
        assertFalse(actualResult7);
        assertTrue(actualResult8);
        assertTrue(actualResult9);
    }

    @Test
    public void isValidPassword() {
        boolean actualResult1 = Validation.isValidPassword("wad");
        boolean actualResult2 = Validation.isValidPassword("Bohdan123");
        boolean actualResult3 = Validation.isValidPassword("Bohdan_Kasumov");
        boolean actualResult4 = Validation.isValidPassword("_!_2_2____");
        boolean actualResult5 = Validation.isValidPassword("ццц     цц");
        boolean actualResult6 = Validation.isValidPassword("            ");
        boolean actualResult7 = Validation.isValidPassword("ФЫЫЫЫ_;;");
        boolean actualResult8 = Validation.isValidPassword("pe4enika");
        boolean actualResult9 = Validation.isValidPassword("@..$,,,,33?");

        assertFalse(actualResult1);
        assertTrue(actualResult2);
        assertTrue(actualResult3);
        assertTrue(actualResult4);
        assertFalse(actualResult5);
        assertFalse(actualResult6);
        assertFalse(actualResult7);
        assertTrue(actualResult8);
        assertTrue(actualResult9);
    }

    @Test
    public void isEqualsPassword() {
        String password1 = "Abcdefgh";
        String password2 = "Abcdefgh";
        String password3 = "abcdefgh";

        boolean actualResult1 = Validation.isEqualsPassword(password1, password2);
        boolean actualResult2 = Validation.isEqualsPassword(password2, password3);

        assertTrue(actualResult1);
        assertFalse(actualResult2);
    }
}