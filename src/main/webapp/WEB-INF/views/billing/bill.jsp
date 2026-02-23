<%@ include file="../common/header.jspf" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<h3 class="mb-3">Bill</h3>

<div class="card">
    <div class="card-body">
        <p class="text-muted mb-4">Reservation ID: <b>${bill.reservationId}</b></p>

        <div class="mb-3">
            <span class="badge ${bill.status eq 'PAID' ? 'bg-success' : 'bg-warning text-dark'}">${bill.status}</span>
            <c:if test="${bill.status eq 'PAID' and bill.paidAt ne null}">
                <span class="text-muted ms-2">Paid at: ${bill.paidAt}</span>
            </c:if>
        </div>

        <div class="row">
            <div class="col-md-6">
                <p><b>Customer:</b> ${bill.customerName}</p>
                <p><b>Room:</b> ${bill.roomNo} (${bill.roomType})</p>
            </div>
            <div class="col-md-6">
                <p><b>Nights:</b> ${bill.nights}</p>
                <p><b>Rate (per night):</b> Rs.${bill.pricePerNight}</p>
            </div>
        </div>

        <hr />

        <table class="table table-bordered">
            <tbody>
            <tr>
                <th>Sub Total</th>
                <td class="text-end">Rs.${bill.subTotal}</td>
            </tr>
            <tr>
                <th>Service Charge (10%)</th>
                <td class="text-end">Rs.${bill.serviceCharge}</td>
            </tr>
            <tr>
                <th>Tax (2%)</th>
                <td class="text-end">Rs.${bill.tax}</td>
            </tr>
            <tr class="table-success">
                <th>Total</th>
                <td class="text-end"><b>Rs.${bill.total}</b></td>
            </tr>
            </tbody>
        </table>

        <div class="d-flex gap-2">
            <a class="btn btn-outline-primary" target="_blank"
               href="${pageContext.request.contextPath}/billing/print?id=${bill.reservationId}">Print (PDF)</a>

            <form method="post" action="${pageContext.request.contextPath}/billing" class="d-inline">
                <input type="hidden" name="id" value="${bill.reservationId}" />
                <c:choose>
                    <c:when test="${bill.status eq 'PAID'}">
                        <input type="hidden" name="status" value="UNPAID" />
                        <button class="btn btn-outline-warning" type="submit" onclick="return confirm('Mark this bill as UNPAID?')">Mark as Unpaid</button>
                    </c:when>
                    <c:otherwise>
                        <input type="hidden" name="status" value="PAID" />
                        <button class="btn btn-success" type="submit" onclick="return confirm('Mark this bill as PAID?')">Mark as Paid</button>
                    </c:otherwise>
                </c:choose>
            </form>

            <a class="btn btn-secondary" href="${pageContext.request.contextPath}/reservations">Back</a>
        </div>
    </div>
</div>

<%@ include file="../common/footer.jspf" %>
