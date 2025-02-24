package com.beamsoftsolution.taxfm.utils;

import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;

@Component
public class DurationUtility {
	public String formatDuration(LocalDateTime start, LocalDateTime end) {
		if (start == null || end == null) return "-";
		Duration duration = Duration.between(start, end);
		return String.format("%dh %02dm", duration.toHours(), duration.toMinutesPart());
	}
}