package io.smint.clapi.consumer.integration.core.configuration.models.impl;

import java.time.LocalDateTime;

public final class LocalDateTimeDeserializationModel {
	public LocalDateDeserializationModel date;
	public LocalTimeDeserializationModel time;
	
	public LocalDateTime GetLocalDateTime() {
		return LocalDateTime.of(date.getLocalDate(), time.getLocalTime());
	}
}