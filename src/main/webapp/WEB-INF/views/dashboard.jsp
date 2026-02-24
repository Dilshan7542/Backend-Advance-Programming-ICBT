<%@ include file="common/header.jspf" %>
<section>
    <section class="container">
        <h3 class="mb-3">Dashboard</h3>
        <p class="text-muted">Use the menu to manage customers, reservations, and billing.</p>
        <section>
            <div class="row g-3">
                <div class="col-md-4">
                    <div class="card h-100">
                        <div class="card-body">
                            <h5 class="card-title">Rooms</h5>
                            <p class="card-text">View rooms and check availability by date range.</p>
                            <a class="btn btn-primary" href="${pageContext.request.contextPath}/rooms">Open</a>
                        </div>
                    </div>
                </div>
                <div class="col-md-4">
                    <div class="card h-100">
                        <div class="card-body">
                            <h5 class="card-title">Customers</h5>
                            <p class="card-text">Add, manage, and search customer details.</p>
                            <a class="btn btn-primary" href="${pageContext.request.contextPath}/customers">Open</a>
                        </div>
                    </div>
                </div>
                <div class="col-md-4">
                    <div class="card h-100">
                        <div class="card-body">
                            <h5 class="card-title">Reservations</h5>
                            <p class="card-text">Create reservations (prevents double-booking automatically).</p>
                            <a class="btn btn-primary" href="${pageContext.request.contextPath}/reservations">Open</a>
                        </div>
                    </div>
                </div>
            </div>

            <div class="row g-3 mt-1">
                <div class="col-md-4">
                    <div class="card h-100">
                        <div class="card-body">
                            <h5 class="card-title">Billing</h5>
                            <p class="card-text">Generate bills and mark payment status (PAID/UNPAID).</p>
                            <a class="btn btn-outline-primary" href="${pageContext.request.contextPath}/reservations">Open</a>
                        </div>
                    </div>
                </div>
                <%--   <div class="col-md-4">
                       <div class="card h-100">
                           <div class="card-body">
                               <h5 class="card-title">Help</h5>
                               <p class="card-text">How to use the system.</p>
                               <a class="btn btn-outline-primary" href="${pageContext.request.contextPath}/help">Open</a>
                           </div>
                       </div>
                   </div>--%>
            </div>
        </section>
    </section>
    <section class="w-100 pt-4">
        <img src="${pageContext.request.contextPath}/assets/img/backgound.jpg" class="w-100" style="height: 250px">
    </section>
</section>
<%@ include file="common/footer.jspf" %>
