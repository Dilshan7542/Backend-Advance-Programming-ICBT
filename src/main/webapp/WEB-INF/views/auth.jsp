<%--
  Created by IntelliJ IDEA.
  User: dilshan_k
  Date: 2/22/2026
  Time: 12:07 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<h2>Ocean View Resort - Demo UI</h2>

<button onclick="callApi()">Call API (/api/health)</button>
<pre id="out" style="margin-top:12px;"></pre>

<script>
    function callApi() {
        fetch('<%= request.getContextPath() %>/api/health')
            .then(r => r.json())
            .then(d => document.getElementById('out').textContent = JSON.stringify(d, null, 2))
            .catch(e => document.getElementById('out').textContent = 'ERROR: ' + e);
    }
</script>
</body>
</html>
