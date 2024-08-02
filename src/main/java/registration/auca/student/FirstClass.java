package registration.auca.student;

import java.io.IOException;
import java.sql.*;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FirstClass extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private static final String JDBC_URL = "jdbc:postgresql://localhost:5433/auca_db";
    private static final String JDBC_USER = "dani";
    private static final String JDBC_PASSWORD = "Smooth1.";
    
    private static final Logger logger = LoggerConfig.logger;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        logger.info("Received action: " + action);

        switch (action) {
            case "create":
                createUser(request, response);
                break;
            case "retrieve":
                retrieveUser(request, response);
                break;
            case "update":
                updateUser(request, response);
                break;
            case "delete":
                deleteUser(request, response);
                break;
            default:
                response.getWriter().println("Invalid action!");
                logger.warning("Invalid action received: " + action);
        }
    }

    private void createUser(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String userId = request.getParameter("id");
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");

        logger.info("Creating user: " + userId);

        try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
             PreparedStatement stmt = connection.prepareStatement("INSERT INTO users (user_id, first_name, last_name) VALUES (?, ?, ?)")) {
            stmt.setString(1, userId);
            stmt.setString(2, firstName);
            stmt.setString(3, lastName);
            stmt.executeUpdate();
            displayMessage(response, "User created successfully!", userId, firstName, lastName);
            logger.info("User created successfully: " + userId);
        } catch (SQLException e) {
            response.getWriter().println("Error creating user: " + e.getMessage());
            logger.log(Level.SEVERE, "Error creating user: " + userId, e);
        }
    }

    private void retrieveUser(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String userId = request.getParameter("id");

        logger.info("Retrieving user: " + userId);

        try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
             PreparedStatement stmt = connection.prepareStatement("SELECT * FROM users WHERE user_id = ?")) {
            stmt.setString(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                displayMessage(response, "User retrieved successfully!", rs.getString("user_id"), rs.getString("first_name"), rs.getString("last_name"));
                logger.info("User retrieved successfully: " + userId);
            } else {
                response.getWriter().println("User not found!");
                logger.warning("User not found: " + userId);
            }
        } catch (SQLException e) {
            response.getWriter().println("Error retrieving user: " + e.getMessage());
            logger.log(Level.SEVERE, "Error retrieving user: " + userId, e);
        }
    }

    private void updateUser(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String userId = request.getParameter("id");
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");

        logger.info("Updating user: " + userId);

        try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
             PreparedStatement stmt = connection.prepareStatement("UPDATE users SET first_name = ?, last_name = ? WHERE user_id = ?")) {
            stmt.setString(1, firstName);
            stmt.setString(2, lastName);
            stmt.setString(3, userId);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                displayMessage(response, "User updated successfully!", userId, firstName, lastName);
                logger.info("User updated successfully: " + userId);
            } else {
                response.getWriter().println("User not found!");
                logger.warning("User not found: " + userId);
            }
        } catch (SQLException e) {
            response.getWriter().println("Error updating user: " + e.getMessage());
            logger.log(Level.SEVERE, "Error updating user: " + userId, e);
        }
    }

    private void deleteUser(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String userId = request.getParameter("id");

        logger.info("Deleting user: " + userId);

        try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
             PreparedStatement stmt = connection.prepareStatement("DELETE FROM users WHERE user_id = ?")) {
            stmt.setString(1, userId);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                displayMessage(response, "User deleted successfully!", userId, "", "");
                logger.info("User deleted successfully: " + userId);
            } else {
                response.getWriter().println("User not found!");
                logger.warning("User not found: " + userId);
            }
        } catch (SQLException e) {
            response.getWriter().println("Error deleting user: " + e.getMessage());
            logger.log(Level.SEVERE, "Error deleting user: " + userId, e);
        }
    }

    private void displayMessage(HttpServletResponse response, String message, String userId, String firstName, String lastName) throws IOException {
        response.setContentType("text/html");
        response.getWriter().println("<!DOCTYPE html>");
        response.getWriter().println("<html>");
        response.getWriter().println("<head>");
        response.getWriter().println("<title>User Information</title>");
        response.getWriter().println("<style>");
        response.getWriter().println("body { font-family: 'Montserrat', sans-serif; background: linear-gradient(to right, #e2e2e2, #c9d6ff); display: flex; flex-direction: column; align-items: center; justify-content: space-between; min-height: 100vh; margin: 0; }");
        response.getWriter().println(".container { background-color: #fff; border-radius: 30px; box-shadow: 0 5px 15px rgba(0, 0, 0, 0.35); width: 768px; max-width: 100%; padding: 40px; text-align: center; margin-bottom: 20px; }");
        response.getWriter().println("h1 { margin-bottom: 20px; }");
        response.getWriter().println("p { font-size: 18px; color: #4caf50; margin-top: 20px; }");
        response.getWriter().println("table { width: 100%; margin-top: 20px; border-collapse: collapse; }");
        response.getWriter().println("td { padding: 10px; border: 1px solid #ccc; }");
        response.getWriter().println("button { background-color: #512da8; color: #fff; font-size: 12px; padding: 10px 45px; border: 1px solid transparent; border-radius: 8px; font-weight: 600; letter-spacing: 0.5px; text-transform: uppercase; margin-top: 20px; cursor: pointer; }");
        response.getWriter().println("button:hover { background-color: #311b92; }");
        response.getWriter().println("footer { text-align: center; font-size: 0.9em; color: #555; margin-top: auto; padding: 10px; background-color: #f0f0f0; width: 100%; }");
        response.getWriter().println("</style>");
        response.getWriter().println("</head>");
        response.getWriter().println("<body>");
        response.getWriter().println("<div class='container'>");
        response.getWriter().println("<h1>User Information</h1>");
        response.getWriter().println("<p>" + message + "</p>");
        response.getWriter().println("<table>");
        response.getWriter().println("<tr><td>ID:</td><td>" + userId + "</td></tr>");
        response.getWriter().println("<tr><td>First Name:</td><td>" + firstName + "</td></tr>");
        response.getWriter().println("<tr><td>Last Name:</td><td>" + lastName + "</td></tr>");
        response.getWriter().println("</table>");
        response.getWriter().println("<button onclick='goBack()'>Back</button>");
        response.getWriter().println("</div>");
        response.getWriter().println("<footer>");
        response.getWriter().println("2024 &copy; Daniel Maseya. All Rights Reserved.");
        response.getWriter().println("</footer>");
        response.getWriter().println("<script>");
        response.getWriter().println("function goBack() {");
        response.getWriter().println("  window.history.back();");
        response.getWriter().println("}");
        response.getWriter().println("</script>");
        response.getWriter().println("</body>");
        response.getWriter().println("</html>");
    }

    public void init() throws ServletException {
        try {
            Class.forName("org.postgresql.Driver");
            logger.info("PostgreSQL JDBC Driver registered");
        } catch (ClassNotFoundException e) {
            logger.log(Level.SEVERE, "PostgreSQL JDBC Driver not found", e);
            throw new ServletException(e);
        }
    }
}
