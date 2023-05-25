import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.WebDriverRunner;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import sqlHomeworkOne.Sql;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;


public class DDTTest {


    @BeforeTest
    public void setUp() {
        try (Connection conn = Sql.getConnection()) {
            System.out.println(String.format("Connected to database %s "
                    + "successfully.", conn.getCatalog()));
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    @Test
    public void dbTest() throws SQLException {
        open("https://demoqa.com/automation-practice-form");
        WebDriverRunner.getWebDriver().manage().window().maximize();
        SelenideElement firstName = $("#firstName");
        SelenideElement lastName = $("#lastName");
        SelenideElement phoneNumber = $("#userNumber");

        String table = "SELECT * FROM dbo.[students]";

        try (Connection conn = Sql.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(table)) {

            while (rs.next()) {
                firstName.setValue(rs.getString("firstname"));
                lastName.setValue(rs.getString("lastname"));
                phoneNumber.setValue(rs.getString("phone"));

                firstName.clear();
                lastName.clear();
                phoneNumber.clear();
            }
        }

    }
}
