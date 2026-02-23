package lk.icbt.resort.model.entity;

import java.math.BigDecimal;

public class Room {
    private int roomId;
    private String roomNo;
    private String roomType;
    private String acType; // AC / NON_AC
    private BigDecimal pricePerNight;
    private String status;

    public Room() {}

    public Room(int roomId, String roomNo, String roomType, String acType, BigDecimal pricePerNight, String status) {
        this.roomId = roomId;
        this.roomNo = roomNo;
        this.roomType = roomType;
        this.acType = acType;
        this.pricePerNight = pricePerNight;
        this.status = status;
    }

    public int getRoomId() { return roomId; }
    public void setRoomId(int roomId) { this.roomId = roomId; }

    public String getRoomNo() { return roomNo; }
    public void setRoomNo(String roomNo) { this.roomNo = roomNo; }

    public String getRoomType() { return roomType; }
    public void setRoomType(String roomType) { this.roomType = roomType; }

    public String getAcType() { return acType; }
    public void setAcType(String acType) { this.acType = acType; }

    public BigDecimal getPricePerNight() { return pricePerNight; }
    public void setPricePerNight(BigDecimal pricePerNight) { this.pricePerNight = pricePerNight; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
