<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <title>Login - Ocean View Resort</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="bg-light">
<div class="container" style="max-width: 520px;">

    <div class="text-center my-5">
        <h2>Ocean View Resort</h2>
        <p class="text-muted mb-0">Online Room Reservation System</p>
    </div>

    <c:if test="${not empty error}">
        <div class="alert alert-danger">${error}</div>
    </c:if>

    <div class="card shadow-sm">
        <div class="card-body">
            <h5 class="card-title mb-3">Login</h5>

            <form method="post" action="${pageContext.request.contextPath}/login">
                <div class="mb-3">
                    <label class="form-label">Username</label>
                    <input type="text" name="username" class="form-control" required />
                </div>
                <div class="mb-3">
                    <label class="form-label">Password</label>
                    <input type="password" name="password" class="form-control" required />
                </div>

                <button type="submit" class="btn btn-primary w-100">Sign in</button>

                <div class="mt-3 small text-muted">
                    Demo user (from SQL seed): <b>admin</b> / <b>admin123</b>
                </div>
            </form>
        </div>
    </div>

    <div class="text-center mt-4">
        <button class="btn btn-outline-secondary btn-sm" onclick="callApi()">Test API (/api/health)</button>
        <pre id="out" class="text-start small mt-2"></pre>
    </div>

</div>

<script>
    function callApi() {
        fetch('${pageContext.request.contextPath}/api/health')
            .then(r => r.json())
            .then(d => document.getElementById('out').textContent = JSON.stringify(d, null, 2))
            .catch(e => document.getElementById('out').textContent = 'ERROR: ' + e);
    }
</script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
