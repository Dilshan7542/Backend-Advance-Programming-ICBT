package lk.icbt.resort.model.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class BillView {
    private int reservationId;
    private String customerName;
    private String roomNo;
    private String roomType;
    private int nights;
    private BigDecimal pricePerNight;
    private BigDecimal subTotal;
    private BigDecimal serviceCharge; // 10%
    private BigDecimal tax;           // 2%
    private BigDecimal total;

    private String status; // UNPAID/PAID
    private LocalDateTime paidAt;

    public int getReservationId() { return reservationId; }
    public void setReservationId(int reservationId) { this.reservationId = reservationId; }

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public String getRoomNo() { return roomNo; }
    public void setRoomNo(String roomNo) { this.roomNo = roomNo; }

    public String getRoomType() { return roomType; }
    public void setRoomType(String roomType) { this.roomType = roomType; }

    public int getNights() { return nights; }
    public void setNights(int nights) { this.nights = nights; }

    public BigDecimal getPricePerNight() { return pricePerNight; }
    public void setPricePerNight(BigDecimal pricePerNight) { this.pricePerNight = pricePerNight; }

    public BigDecimal getSubTotal() { return subTotal; }
    public void setSubTotal(BigDecimal subTotal) { this.subTotal = subTotal; }

    public BigDecimal getServiceCharge() { return serviceCharge; }
    public void setServiceCharge(BigDecimal serviceCharge) { this.serviceCharge = serviceCharge; }

    public BigDecimal getTax() { return tax; }
    public void setTax(BigDecimal tax) { this.tax = tax; }

    public BigDecimal getTotal() { return total; }
    public void setTotal(BigDecimal total) { this.total = total; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getPaidAt() { return paidAt; }
    public void setPaidAt(LocalDateTime paidAt) { this.paidAt = paidAt; }
}
