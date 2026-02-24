<%@ include file="../common/header.jspf" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<%-- tab param default --%>
<c:set var="tab" value="${param.tab}" />
<c:if test="${empty tab}">
    <c:set var="tab" value="available" />
</c:if>

<%-- if controller sets split lists --%>
<c:set var="hasSplit" value="false" />
<c:if test="${not empty availableRooms or not empty unavailableRooms}">
    <c:set var="hasSplit" value="true" />
</c:if>

<%-- counts without using rooms.size() or fn:length --%>
<c:set var="availableCount" value="0" />
<c:set var="unavailableCount" value="0" />

<c:choose>
    <c:when test="${hasSplit}">
        <c:forEach var="x" items="${availableRooms}">
            <c:set var="availableCount" value="${availableCount + 1}" />
        </c:forEach>
        <c:forEach var="x" items="${unavailableRooms}">
            <c:set var="unavailableCount" value="${unavailableCount + 1}" />
        </c:forEach>
    </c:when>
    <c:otherwise>
        <c:forEach var="x" items="${rooms}">
            <c:set var="availableCount" value="${availableCount + 1}" />
        </c:forEach>
    </c:otherwise>
</c:choose>

<section class="container">
    <div class="d-flex justify-content-between align-items-center mb-3">
        <h3 class="mb-0">Rooms</h3>

        <c:choose>
            <c:when test="${not empty checkIn and not empty checkOut}">
                <span class="badge bg-primary">Availability for ${checkIn} to ${checkOut}</span>
            </c:when>
            <c:otherwise>
                <span class="badge bg-secondary">All Rooms</span>
            </c:otherwise>
        </c:choose>
    </div>

    <form class="card card-body mb-3" method="get" action="${pageContext.request.contextPath}/rooms">
        <div class="row g-3 align-items-end">
            <div class="col-md-4">
                <label class="form-label">Check-in</label>
                <input type="date" class="form-control" name="checkIn" value="${checkIn}"/>
            </div>
            <div class="col-md-4">
                <label class="form-label">Check-out</label>
                <input type="date" class="form-control" name="checkOut" value="${checkOut}"/>
            </div>
            <div class="col-md-4 d-flex gap-2">
                <button class="btn btn-primary" type="submit">Search</button>
                <a class="btn btn-outline-secondary" href="${pageContext.request.contextPath}/rooms">Clear</a>
            </div>
        </div>
    </form>

    <%-- Tabs --%>
    <ul class="nav nav-tabs mb-3">
        <li class="nav-item">
            <c:choose>
                <c:when test="${tab eq 'available'}">
                    <a class="nav-link active"
                       href="${pageContext.request.contextPath}/rooms?checkIn=${checkIn}&checkOut=${checkOut}&tab=available">
                        Available <span class="badge bg-success ms-1">${availableCount}</span>
                    </a>
                </c:when>
                <c:otherwise>
                    <a class="nav-link"
                       href="${pageContext.request.contextPath}/rooms?checkIn=${checkIn}&checkOut=${checkOut}&tab=available">
                        Available <span class="badge bg-success ms-1">${availableCount}</span>
                    </a>
                </c:otherwise>
            </c:choose>
        </li>

        <li class="nav-item">
            <c:choose>
                <c:when test="${not hasSplit}">
                    <a class="nav-link disabled" href="#" tabindex="-1" aria-disabled="true">
                        Unavailable <span class="badge bg-danger ms-1">0</span>
                    </a>
                </c:when>
                <c:when test="${tab eq 'unavailable'}">
                    <a class="nav-link active"
                       href="${pageContext.request.contextPath}/rooms?checkIn=${checkIn}&checkOut=${checkOut}&tab=unavailable">
                        Unavailable <span class="badge bg-danger ms-1">${unavailableCount}</span>
                    </a>
                </c:when>
                <c:otherwise>
                    <a class="nav-link"
                       href="${pageContext.request.contextPath}/rooms?checkIn=${checkIn}&checkOut=${checkOut}&tab=unavailable">
                        Unavailable <span class="badge bg-danger ms-1">${unavailableCount}</span>
                    </a>
                </c:otherwise>
            </c:choose>
        </li>
    </ul>

    <%-- Decide which list to render --%>
    <c:choose>
        <c:when test="${hasSplit and tab eq 'unavailable'}">
            <c:set var="displayRooms" value="${unavailableRooms}" />
            <c:set var="listTitle" value="Unavailable Rooms" />
        </c:when>
        <c:otherwise>
            <c:choose>
                <c:when test="${hasSplit}">
                    <c:set var="displayRooms" value="${availableRooms}" />
                </c:when>
                <c:otherwise>
                    <c:set var="displayRooms" value="${rooms}" />
                </c:otherwise>
            </c:choose>
            <c:set var="listTitle" value="Available Rooms" />
        </c:otherwise>
    </c:choose>

    <div class="mb-3">
        <span class="text-muted">${listTitle}</span>
        <c:if test="${not empty counts}">
            <span class="text-muted ms-3">SINGLE: <b>${counts['SINGLE']}</b></span>
            <span class="text-muted ms-3">DOUBLE: <b>${counts['DOUBLE']}</b></span>
            <span class="text-muted ms-3">SUITE: <b>${counts['SUITE']}</b></span>
        </c:if>
    </div>

    <c:if test="${empty displayRooms}">
        <div class="alert alert-info">No rooms found.</div>
    </c:if>

    <c:if test="${not empty displayRooms}">
        <table class="table table-striped table-bordered align-middle">
            <thead class="table-dark">
            <tr>
                <th>Room No</th>
                <th>Type</th>
                <th>Category</th>
                <th class="text-end">Price / Night</th>
                <th>Status</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="rm" items="${displayRooms}">
                <tr>
                    <td>${rm.roomNo}</td>
                    <td>${rm.roomType}</td>
                    <td>
                        <c:choose>
                            <c:when test="${rm.acType eq 'AC'}">
                                <span class="badge bg-info text-dark">A/C</span>
                            </c:when>
                            <c:otherwise>
                                <span class="badge bg-secondary">Non A/C</span>
                            </c:otherwise>
                        </c:choose>
                    </td>
                    <td class="text-end">Rs.${rm.pricePerNight}</td>
                    <td>
                        <c:choose>
                            <c:when test="${hasSplit and tab eq 'unavailable' and rm.status eq 'AVAILABLE'}">
                                <span class="badge bg-danger">BOOKED</span>
                            </c:when>
                            <c:when test="${rm.status eq 'AVAILABLE'}">
                                <span class="badge bg-success">AVAILABLE</span>
                            </c:when>
                            <c:otherwise>
                                <span class="badge bg-warning text-dark">${rm.status}</span>
                            </c:otherwise>
                        </c:choose>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </c:if>
</section>

<%@ include file="../common/footer.jspf" %>