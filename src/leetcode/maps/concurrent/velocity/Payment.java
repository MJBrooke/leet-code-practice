package leetcode.maps.concurrent.velocity;

import java.time.Instant;

public record Payment(String id, Instant timestamp, String hashedCardNumber) {}
