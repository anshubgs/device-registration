package com.anshu.device_registration.exception;

public class DeviceAlreadyRegisteredException extends RuntimeException {
    public DeviceAlreadyRegisteredException(String message) {
        super(message);
    }

}
