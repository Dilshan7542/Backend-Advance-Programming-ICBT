<%@ include file="../common/header.jspf" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<div class="d-flex justify-content-between align-items-center mb-3">
    <h3 class="mb-0">Rooms</h3>
    <c:choose>
        <c:when test="${mode eq 'AVAILABLE'}">
            <span class="badge bg-success">Showing Available Rooms</span>
        </c:when>
        <c:otherwise>
            <span class="badge bg-secondary">Showing All Rooms</span>
        </c:otherwise>
    </c:choose>
</div>

<form class="card card-body mb-3" method="get" action="${pageContext.request.contextPath}/rooms">
    <div class="row g-3 align-items-end">
        <div class="col-md-4">
            <label class="form-label">Check-in</label>
            <input type="date" class="form-control" name="checkIn" value="${checkIn}" />
        </div>
        <div class="col-md-4">
            <label class="form-label">Check-out</label>
            <input type="date" class="form-control" name="checkOut" value="${checkOut}" />
        </div>
        <div class="col-md-4 d-flex gap-2">
            <button class="btn btn-primary" type="submit">Show Available Rooms</button>
            <a class="btn btn-outline-secondary" href="${pageContext.request.contextPath}/rooms">Clear</a>
        </div>
    </div>
    <div class="form-text mt-2">
        Tip: Add dates to see availability. When you create a reservation, the available room count for that date range will reduce automatically.
    </div>
</form>

<div class="mb-3">
    <span class="text-muted">Total listed: <b>${rooms.size()}</b></span>
    <c:if test="${not empty counts}">
        <span class="text-muted ms-3">SINGLE: <b>${counts['SINGLE']}</b></span>
        <span class="text-muted ms-3">DOUBLE: <b>${counts['DOUBLE']}</b></span>
        <span class="text-muted ms-3">SUITE: <b>${counts['SUITE']}</b></span>
    </c:if>
</div>

<table class="table table-striped table-bordered align-middle">
    <thead class="table-dark">
    <tr>
        <th>Room No</th>
        <th>Type</th>
        <th>Category</th>
        <th class="text-end">Price / Night</th>
        <th>Status (Room)</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach var="rm" items="${rooms}">
        <tr>
            <td>${rm.roomNo}</td>
            <td>${rm.roomType}</td>
            <td>
                <span class="badge ${rm.acType eq 'AC' ? 'bg-info text-dark' : 'bg-secondary'}">
                    ${rm.acType eq 'AC' ? 'A/C' : 'Non A/C'}
                </span>
            </td>
            <td class="text-end">Rs.${rm.pricePerNight}</td>
            <td>
                <span class="badge ${rm.status eq 'AVAILABLE' ? 'bg-success' : 'bg-warning text-dark'}">${rm.status}</span>
            </td>
        </tr>
    </c:forEach>
    </tbody>
</table>

<%@ include file="../common/footer.jspf" %>
