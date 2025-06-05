package hexlet.code.model;

import java.time.Instant;

public final class LastCheck {
    private Long urlId;
    private Long status;
    private Instant checkTime;

    public LastCheck(Long userId, Long status, Instant checkTime) {
        this.urlId = userId;
        this.status = status;
        this.checkTime = checkTime;
    }

    public Long getUserId() {
        return urlId;
    }
    public Long getStatus() {
        return status;
    }
    public Instant getCheckTime() {
        return checkTime;
    }
}
