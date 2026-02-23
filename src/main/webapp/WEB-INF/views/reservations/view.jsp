<%@ include file="../common/header.jspf" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<h3 class="mb-3">Reservation Details</h3>

<div class="card">
    <div class="card-body">
        <div class="row">
            <div class="col-md-6">
                <p><b>Reservation ID:</b> ${reservation.reservationId}</p>
                <p><b>Customer:</b> ${reservation.customerName}</p>
                <p><b>Room:</b> ${reservation.roomNo} (${reservation.roomType})</p>
            </div>
            <div class="col-md-6">
                <p><b>Check-in:</b> ${reservation.checkIn}</p>
                <p><b>Check-out:</b> ${reservation.checkOut}</p>
                <p><b>Nights:</b> ${reservation.nights}</p>
                <p><b>Status:</b> ${reservation.status}</p>
                <p><b>Bill Status:</b>
                    <span class="badge ${reservation.billStatus eq 'PAID' ? 'bg-success' : 'bg-warning text-dark'}">${reservation.billStatus}</span>
                </p>
            </div>
        </div>

        <c:if test="${reservation.status eq 'CONFIRMED'}">
            <hr/>
            <c:choose>
                <c:when test="${reservation.billStatus eq 'PAID'}">
                    <c:choose>
                        <c:when test="${sessionScope.AUTH_USER.role eq 'MANAGER' or sessionScope.AUTH_USER.role eq 'ADMIN'}">
                            <div class="alert alert-warning">
                                <b>Paid reservation:</b> Cancellation is restricted. As a manager, you can cancel only with a reason.
                            </div>
                            <form method="post" action="${pageContext.request.contextPath}/reservations/cancel" class="mt-2">
                                <input type="hidden" name="id" value="${reservation.reservationId}" />
                                <div class="mb-2">
                                    <label class="form-label">Reason (required)</label>
                                    <textarea class="form-control" name="reason" rows="3" required placeholder="Example: customer requested refund, duplicate booking, system error..."></textarea>
                                </div>
                                <button class="btn btn-danger" onclick="return confirm('Cancel this PAID reservation? This action will be logged.')">Cancel (Manager Override)</button>
                            </form>
                        </c:when>
                        <c:otherwise>
                            <div class="alert alert-danger">
                                This reservation is <b>PAID</b>. Only a <b>MANAGER</b> can cancel it.
                            </div>
                        </c:otherwise>
                    </c:choose>
                </c:when>
                <c:otherwise>
                    <form method="post" action="${pageContext.request.contextPath}/reservations/cancel" class="mt-2">
                        <input type="hidden" name="id" value="${reservation.reservationId}" />
                        <button class="btn btn-danger" onclick="return confirm('Cancel this reservation?')">Cancel Reservation</button>
                    </form>
                </c:otherwise>
            </c:choose>
        </c:if>

        <div class="d-flex gap-2 mt-3">
            <a class="btn btn-warning" href="${pageContext.request.contextPath}/billing?id=${reservation.reservationId}">Display Bill</a>
            <a class="btn btn-secondary" href="${pageContext.request.contextPath}/reservations">Back</a>
        </div>
    </div>
</div>

<%@ include file="../common/footer.jspf" %>
