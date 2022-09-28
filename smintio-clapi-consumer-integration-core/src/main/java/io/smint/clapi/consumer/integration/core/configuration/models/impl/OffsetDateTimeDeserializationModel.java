package io.smint.clapi.consumer.integration.core.configuration.models.impl;

import java.time.OffsetDateTime;

public final class OffsetDateTimeDeserializationModel {
	public LocalDateTimeDeserializationModel dateTime;
    public ZoneOffsetDeserializationModel offset;
    
	public OffsetDateTime getOffsetDateTime() {
		return OffsetDateTime.of(dateTime.GetLocalDateTime(), offset.getZoneOffset());
	}
}
