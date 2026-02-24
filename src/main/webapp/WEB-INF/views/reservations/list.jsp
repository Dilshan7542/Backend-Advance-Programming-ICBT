<%@ include file="../common/header.jspf" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<section class="container">
    <div class="d-flex justify-content-between align-items-center mb-3">
        <div>
            <h3 class="mb-1">Reservations</h3>
            <div class="btn-group" role="group" aria-label="Reservation tabs">
                <a class="btn btn-sm ${tab eq 'all' ? 'btn-primary' : 'btn-outline-primary'}"
                   href="${pageContext.request.contextPath}/reservations?tab=all">All</a>
                <a class="btn btn-sm ${tab eq 'active' ? 'btn-primary' : 'btn-outline-primary'}"
                   href="${pageContext.request.contextPath}/reservations?tab=active">Active</a>
                <a class="btn btn-sm ${tab eq 'history' ? 'btn-primary' : 'btn-outline-primary'}"
                   href="${pageContext.request.contextPath}/reservations?tab=history">History</a>
            </div>
        </div>
        <a class="btn btn-success" href="${pageContext.request.contextPath}/reservations/add">+ Add Reservation</a>
    </div>

    <c:if test="${not empty error}">
        <div class="alert alert-danger">${error}</div>
    </c:if>

    <table class="table table-striped table-bordered align-middle">
        <thead class="table-dark">
        <tr>
            <th>ID</th>
            <th>Customer</th>
            <th>Room</th>
            <th>Type</th>
            <th>Check-in</th>
            <th>Check-out</th>
            <th>Nights</th>
            <th>Status</th>
            <th>Bill</th>
            <th style="width: 240px;">Actions</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="r" items="${reservations}">
            <tr>
                <td>${r.reservationId}</td>
                <td>${r.customerName}</td>
                <td>${r.roomNo}</td>
                <td>${r.roomType}</td>
                <td>${r.checkIn}</td>
                <td>${r.checkOut}</td>
                <td>${r.nights}</td>
                <td>
                    <span class="badge ${r.status eq 'CONFIRMED' ? 'text-bg-success' : 'text-bg-secondary'}">${r.status}</span>
                </td>
                <td>
                    <span class="badge ${r.billStatus eq 'PAID' ? 'bg-success' : 'bg-warning text-dark'}">${r.billStatus}</span>
                </td>
                <td>
                    <a class="btn btn-sm btn-primary"
                       href="${pageContext.request.contextPath}/reservations/view?id=${r.reservationId}">View</a>
                    <a class="btn btn-sm btn-warning"
                       href="${pageContext.request.contextPath}/billing?id=${r.reservationId}">Bill</a>

                    <c:if test="${r.status eq 'CONFIRMED'}">
                        <c:choose>
                            <c:when test="${r.billStatus eq 'PAID'}">
                                <button class="btn btn-sm btn-danger" disabled
                                        title="Paid reservations can be cancelled only by MANAGER">
                                    Cancel
                                </button>
                            </c:when>
                            <c:otherwise>
                                <form method="post" action="${pageContext.request.contextPath}/reservations/cancel"
                                      style="display:inline;">
                                    <input type="hidden" name="id" value="${r.reservationId}"/>
                                    <button class="btn btn-sm btn-danger"
                                            onclick="return confirm('Cancel this reservation?')">Cancel
                                    </button>
                                </form>
                            </c:otherwise>
                        </c:choose>
                    </c:if>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</section>

<%@ include file="../common/footer.jspf" %>
