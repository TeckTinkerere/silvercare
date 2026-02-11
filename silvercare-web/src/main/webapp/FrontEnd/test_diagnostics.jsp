<%@ page import="java.sql.*" %>
<%@ page import="com.silvercare.util.DBConnection" %>
<%@ page contentType="text/html; charset=UTF-8" %>

<!DOCTYPE html>
<html>
<head>
    <title>System Diagnostics</title>
    <style>
        body { font-family: monospace; padding: 20px; }
        .success { color: green; }
        .error { color: red; }
        .info { color: blue; }
    </style>
</head>

<body>
<h1>SilverCare System Diagnostics</h1>

<h2>1. Database Connection Check</h2>

<%
Connection conn = null;

try {
    conn = DBConnection.getConnection();

    if (conn != null) {
        out.println("<p class='success'>[PASS] Database connection established successfully.</p>");
        out.println("<p>Product: " + conn.getMetaData().getDatabaseProductName() + "</p>");
        out.println("<p>URL: " + conn.getMetaData().getURL() + "</p>");
    } else {
        out.println("<p class='error'>[FAIL] Connection is null.</p>");
    }

} catch (Exception e) {
    out.println("<p class='error'>[FAIL] Exception connecting to DB: " + e.getMessage() + "</p>");
    e.printStackTrace(new java.io.PrintWriter(out));
}
%>

<h2>2. User Check (anonymousdiamond6328@gmail.com)</h2>

<%
if (conn != null) {

    String targetEmail = "anonymousdiamond6328@gmail.com";

    // ---------- Check Admin ----------
    String adminSql = "SELECT admin_id, username, password_hash FROM silvercare.admin_user WHERE username = ?";

    try (PreparedStatement pstmt = conn.prepareStatement(adminSql)) {

        pstmt.setString(1, targetEmail);

        try (ResultSet rs = pstmt.executeQuery()) {

            if (rs.next()) {
                out.println("<p class='success'>[FOUND] Found in ADMIN_USER table.</p>");
                out.println("<p>ID: " + rs.getInt("admin_id") + "</p>");

                String hash = rs.getString("password_hash");
                String hashPreview = (hash != null && hash.length() > 5)
                        ? hash.substring(0, 5) + "..."
                        : "null";

                out.println("<p>Hash prefix: " + hashPreview + "</p>");

            } else {
                out.println("<p class='info'>[INFO] Not found in ADMIN_USER table.</p>");
            }
        }

    } catch (Exception e) {
        out.println("<p class='error'>[ERROR] Error querying admin: " + e.getMessage() + "</p>");
    }


    // ---------- Check Customer ----------
    String customerSql = "SELECT customer_id, email, password_hash FROM silvercare.customer WHERE email = ?";

    try (PreparedStatement pstmt = conn.prepareStatement(customerSql)) {

        pstmt.setString(1, targetEmail);

        try (ResultSet rs = pstmt.executeQuery()) {

            if (rs.next()) {
                out.println("<p class='success'>[FOUND] Found in CUSTOMER table.</p>");
                out.println("<p>ID: " + rs.getInt("customer_id") + "</p>");

                String hash = rs.getString("password_hash");
                String hashPreview = (hash != null && hash.length() > 5)
                        ? hash.substring(0, 5) + "..."
                        : "null";

                out.println("<p>Hash prefix: " + hashPreview + "</p>");

            } else {
                out.println("<p class='info'>[INFO] Not found in CUSTOMER table.</p>");
            }
        }

    } catch (Exception e) {
        out.println("<p class='error'>[ERROR] Error querying customer: " + e.getMessage() + "</p>");
    }
}
%>

<h2>3. Environment Variables</h2>

<p>DB_URL: <%= System.getenv("DB_URL") != null ? "Set" : "Not Set" %></p>
<p>DB_USER: <%= System.getenv("DB_USER") %></p>

<%
if (conn != null) {
    try {
        conn.close();
    } catch (Exception ignored) {}
}
%>

</body>
</html>
