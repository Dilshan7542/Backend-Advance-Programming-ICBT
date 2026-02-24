<%@ include file="../common/header.jspf" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<section class="container">
    <h3 class="mb-3">Edit Customer</h3>

    <c:if test="${!isManager}">
        <div class="alert alert-warning">
            Only <b>MANAGER</b> can update customer details.
        </div>
    </c:if>

    <form method="post" action="${pageContext.request.contextPath}/customers/edit" class="card card-body">

        <input type="hidden" name="customerId" value="${customer.customerId}" />

        <div class="mb-3">
            <label class="form-label">Full Name *</label>
            <input type="text" name="fullName" class="form-control" value="${customer.fullName}" required />
        </div>

        <div class="mb-3">
            <label class="form-label">Phone *</label>
            <input type="text" name="phone" class="form-control" value="${customer.phone}" required />
        </div>

        <div class="mb-3">
            <label class="form-label">Email</label>
            <input type="email" name="email" class="form-control" value="${customer.email}" />
        </div>

        <div class="mb-3">
            <label class="form-label">NIC</label>
            <input type="text" name="nic" class="form-control" value="${customer.nic}" readonly />
            <div class="form-text">NIC cannot be edited once saved.</div>
        </div>

        <div class="d-flex gap-2">
            <button class="btn btn-primary" type="submit" <c:if test="${!isManager}">disabled</c:if>>Update</button>
            <a class="btn btn-secondary" href="${pageContext.request.contextPath}/customers">Back</a>
        </div>
    </form>
</section>


<%@ include file="../common/footer.jspf" %>
