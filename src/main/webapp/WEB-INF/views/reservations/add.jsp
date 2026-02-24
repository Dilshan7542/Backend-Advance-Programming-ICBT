<%@ include file="../common/header.jspf" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<section class="container">
    <h3 class="mb-3">Add Reservation</h3>

    <c:if test="${not empty error}">
        <div class="alert alert-danger">${error}</div>
    </c:if>

    <form method="get" action="${pageContext.request.contextPath}/reservations/add" class="card card-body mb-3">
        <div class="row g-3 align-items-end">
            <div class="col-md-4">
                <label class="form-label">Check-in</label>
                <input type="date" name="checkIn" class="form-control" value="${checkIn}" required />
            </div>
            <div class="col-md-4">
                <label class="form-label">Check-out</label>
                <input type="date" name="checkOut" class="form-control" value="${checkOut}" required />
            </div>
            <div class="col-md-4 d-flex gap-2">
                <button class="btn btn-primary" type="submit">Find Available Rooms</button>
                <a class="btn btn-outline-secondary" href="${pageContext.request.contextPath}/reservations/add">Clear</a>
            </div>
        </div>
        <div class="form-text mt-2">
            Step 1: Select dates and click <b>Find Available Rooms</b>. Then you can create the reservation.
        </div>
    </form>

    <form method="post" action="${pageContext.request.contextPath}/reservations/add" class="card card-body">

        <div class="row g-3">
            <div class="col-md-6">
                <label class="form-label">Customer *</label>
                <select name="customerId" class="form-select" required>
                    <option value="">-- Select Customer --</option>
                    <c:forEach var="c" items="${customers}">
                        <option value="${c.customerId}">${c.customerId} - ${c.fullName}</option>
                    </c:forEach>
                </select>
                <div class="form-text">If customer is not available, add customer first.</div>
            </div>

            <div class="col-md-6">
                <label class="form-label">Room *</label>

                <c:choose>
                    <c:when test="${hasDates}">
                        <select name="roomId" class="form-select" required>
                            <option value="">-- Select Room --</option>
                            <c:forEach var="rm" items="${rooms}">
                                <option value="${rm.roomId}">${rm.roomNo} - ${rm.roomType} - ${rm.acType eq 'AC' ? 'A/C' : 'Non A/C'} (Rs.${rm.pricePerNight}/night)</option>
                            </c:forEach>
                        </select>
                        <div class="form-text">Available rooms for selected dates: <b>${rooms.size()}</b></div>
                    </c:when>
                    <c:otherwise>
                        <select class="form-select" disabled>
                            <option>-- Select dates first --</option>
                        </select>
                        <div class="form-text text-danger">Please select dates and click “Find Available Rooms”.</div>
                    </c:otherwise>
                </c:choose>
            </div>

            <div class="col-md-6">
                <label class="form-label">Check-in *</label>
                <input type="date" name="checkIn" class="form-control" value="${checkIn}" required />
            </div>

            <div class="col-md-6">
                <label class="form-label">Check-out *</label>
                <input type="date" name="checkOut" class="form-control" value="${checkOut}" required />
            </div>
        </div>

        <hr />

        <div class="d-flex gap-2">
            <button class="btn btn-success" type="submit" <c:if test="${not hasDates}">disabled</c:if>>Create Reservation</button>
            <a class="btn btn-secondary" href="${pageContext.request.contextPath}/reservations">Back</a>
        </div>
    </form>
</section>


<%@ include file="../common/footer.jspf" %>
