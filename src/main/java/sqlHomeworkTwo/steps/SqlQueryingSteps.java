package sqlHomeworkTwo.steps;

import io.qameta.allure.Step;
import org.testng.Assert;
import sqlHomeworkOne.Sql;

import java.sql.*;

public class SqlQueryingSteps {

    @Step
    public SqlQueryingSteps connection() {
        try (Connection conn = Sql.getConnection()) {
            System.out.println(String.format("Connected to database %s "
                    + "successfully.", conn.getCatalog()));
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return this;
    }


    @Step
    public SqlQueryingSteps insertingNewRow(int id, String firstName, String lastName,
                                            String phone) {
        try {
            String table = "INSERT INTO students(id, firstname, lastname, phone)" + "VALUES(?, '?', '?', '?')";
            Sql.getConnection().setAutoCommit(false);
            ResultSet rs = null;
            try (Connection conn = Sql.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(table, Statement.RETURN_GENERATED_KEYS);) {

                pstmt.setInt(1, id);
                pstmt.setString(2, firstName);
                pstmt.setString(3, lastName);
                pstmt.setString(4, phone);

                int rowAffected = pstmt.executeUpdate();
                if (rowAffected == id) {
                    rs = pstmt.getGeneratedKeys();
                    if (rs.next())
                        rs.getInt(id);
                }

                String validationSql = "SELECT * FROM students WHERE id = '" + id + "' AND firstname = '" + firstName +
                        "' AND lastname = '" + lastName + "' AND phone = '" + phone + "'";
                ResultSet resultSet = pstmt.executeQuery(validationSql);
                Assert.assertFalse(resultSet.next(), "New row was created, but it shouldn't have been.");
            }

            assert rs != null;
            String actualId = rs.getString("id");
            String actualFirstName = rs.getString("firstname");
            String actualLastName = rs.getString("lastname");
            String actualPhone = rs.getString("phone");
            Assert.assertEquals(actualId, firstName, "id doesn't match the expected value.");
            Assert.assertEquals(actualFirstName, lastName, "first name doesn't match the expected value.");
            Assert.assertEquals(actualLastName, lastName, "Last name doesn't match the expected value.");
            Assert.assertEquals(actualPhone, lastName, "phone doesn't match the expected value.");

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

        return this;
    }


   @Step
   public SqlQueryingSteps commitChanges() throws SQLException {
        Sql.getConnection().commit();
        return this;
   }

    @Step
    public SqlQueryingSteps updatingFname() throws SQLException {

        String sqlUpdate = "UPDATE students "
                + "SET firstname = ? "
                + "WHERE id = ?";

        try (Connection conn = Sql.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sqlUpdate)) {
            String firstname = "Mirian";
            int id = 1006;
            pstmt.setString(1, firstname);
            pstmt.setInt(2, id);

            int rowAffected = pstmt.executeUpdate();
            System.out.println(String.format("Row affected %d", rowAffected));

            ResultSet resultSet = pstmt.executeQuery(sqlUpdate);
            String actualFirstName = resultSet.getString("first_name");
            Assert.assertEquals(actualFirstName, firstname, "First name doesn't match the expected value.");
            return this;
        }
    }






}
