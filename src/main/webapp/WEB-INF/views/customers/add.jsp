<%@ include file="../common/header.jspf" %>
<section class="container">
    <h3 class="mb-3">Add Customer</h3>

    <form method="post" action="${pageContext.request.contextPath}/customers/add" class="card card-body">

        <div class="mb-3">
            <label class="form-label">Full Name *</label>
            <input type="text" name="fullName" class="form-control" required />
        </div>

        <div class="mb-3">
            <label class="form-label">Phone *</label>
            <input type="text" name="phone" class="form-control" required />
        </div>

        <div class="mb-3">
            <label class="form-label">Email</label>
            <input type="email" name="email" class="form-control" />
        </div>

        <div class="mb-3">
            <label class="form-label">NIC</label>
            <input type="text" name="nic" class="form-control" />
        </div>

        <div class="d-flex gap-2">
            <button class="btn btn-success" type="submit">Save</button>
            <a class="btn btn-secondary" href="${pageContext.request.contextPath}/customers">Back</a>
        </div>
    </form>
</section>


<%@ include file="../common/footer.jspf" %>
