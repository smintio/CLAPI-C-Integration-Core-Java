package io.smint.clapi.consumer.integration.core.configuration.models.impl;

import java.time.LocalTime;

public final class LocalTimeDeserializationModel {
	public byte hour;
    public byte minute;
    public byte second;
    public int nano;
    
	public LocalTime getLocalTime() {
		return LocalTime.of(hour,  minute, second, nano);
	}
}