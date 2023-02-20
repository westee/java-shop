package com.wester.shop.service;

import com.wester.shop.controller.AuthController;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CheckTelServiceTest {
    public static AuthController.TelAndCode validParam = new AuthController.TelAndCode("13112345678", "000000");
    public static AuthController.TelAndCode invalidParam = new AuthController.TelAndCode("23112345678", "000000");
    @Test
    void returnTrueIfValid() {
        Assertions.assertTrue(new CheckTelService().verifyTelParams(validParam));
    }

    @Test
    void returnFalseIfInvalid() {
        Assertions.assertFalse(new CheckTelService().verifyTelParams(invalidParam));
    }
}