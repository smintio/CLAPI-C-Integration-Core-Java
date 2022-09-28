package io.smint.clapi.consumer.integration.core.configuration.models.impl;

import java.time.ZoneOffset;

public final class ZoneOffsetDeserializationModel {
	public int totalSeconds;

	public ZoneOffset getZoneOffset() {
		return ZoneOffset.ofTotalSeconds(totalSeconds);
	}
}
