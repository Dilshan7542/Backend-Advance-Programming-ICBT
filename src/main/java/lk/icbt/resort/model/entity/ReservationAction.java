package lk.icbt.resort.model.entity;

import java.time.LocalDateTime;

public class ReservationAction {
    private int actionId;
    private int reservationId;
    private int userId;
    private String username;
    private String role;
    private String action; // e.g., CANCEL, CANCEL_PAID_OVERRIDE
    private String reason;
    private LocalDateTime createdAt;

    public int getActionId() { return actionId; }
    public void setActionId(int actionId) { this.actionId = actionId; }

    public int getReservationId() { return reservationId; }
    public void setReservationId(int reservationId) { this.reservationId = reservationId; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
