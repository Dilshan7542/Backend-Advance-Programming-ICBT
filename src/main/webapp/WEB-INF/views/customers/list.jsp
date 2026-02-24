<%@ include file="../common/header.jspf" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<section class="container">
    <div class="d-flex justify-content-between align-items-center mb-3">
        <h3 class="mb-0">Customers</h3>
        <div class="d-flex gap-2">
            <form class="d-flex" method="get" action="${pageContext.request.contextPath}/customers">
                <input class="form-control me-2" type="search" name="q" placeholder="Search name / phone / email / NIC" value="${q}" />
                <button class="btn btn-outline-primary" type="submit">Search</button>
            </form>
            <a class="btn btn-success" href="${pageContext.request.contextPath}/customers/add">+ Add Customer</a>
        </div>
    </div>

    <c:if test="${not empty q}">
        <div class="mb-3">
            <span class="text-muted">Search results for: <b>${q}</b></span>
            <a class="ms-2" href="${pageContext.request.contextPath}/customers">Clear</a>
        </div>
    </c:if>

    <table class="table table-striped table-bordered align-middle">
        <thead class="table-dark">
        <tr>
            <th>ID</th>
            <th>Full Name</th>
            <th>Phone</th>
            <th>Email</th>
            <th>NIC</th>
            <th style="width: 180px;">Actions</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="c" items="${customers}">
            <tr>
                <td>${c.customerId}</td>
                <td>${c.fullName}</td>
                <td>${c.phone}</td>
                <td>${c.email}</td>
                <td>${c.nic}</td>
                <td>
                    <c:choose>
                        <c:when test="${isManager}">
                            <a class="btn btn-sm btn-primary" href="${pageContext.request.contextPath}/customers/edit?id=${c.customerId}">Edit</a>
                        </c:when>
                        <c:otherwise>
                            <button class="btn btn-sm btn-secondary" disabled title="Only MANAGER can edit">Edit</button>
                        </c:otherwise>
                    </c:choose>

                    <c:set var="hasRes" value="${hasReservationMap[c.customerId]}" />
                    <c:choose>
                        <c:when test="${isManager && !hasRes}">
                            <form method="post" action="${pageContext.request.contextPath}/customers/delete" style="display:inline;">
                                <input type="hidden" name="id" value="${c.customerId}" />
                                <button class="btn btn-sm btn-danger" onclick="return confirm('Delete this customer?')">Delete</button>
                            </form>
                        </c:when>
                        <c:otherwise>
                            <button class="btn btn-sm btn-secondary" disabled
                                    title="${!isManager ? 'Only MANAGER can delete' : 'Cannot delete (customer has reservations)'}">Delete</button>
                        </c:otherwise>
                    </c:choose>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</section>

<%@ include file="../common/footer.jspf" %>
