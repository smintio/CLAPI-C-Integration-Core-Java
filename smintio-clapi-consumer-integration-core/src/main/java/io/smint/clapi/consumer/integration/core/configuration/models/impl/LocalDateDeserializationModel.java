package io.smint.clapi.consumer.integration.core.configuration.models.impl;

import java.time.LocalDate;

public final class LocalDateDeserializationModel {
	public int year;
    public short month;
    public short day;
    
	public LocalDate getLocalDate() {
		return LocalDate.of(year, month, day);
	}
}