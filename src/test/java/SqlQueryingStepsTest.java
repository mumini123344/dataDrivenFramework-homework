import com.codeborne.selenide.WebDriverRunner;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import sqlHomeworkTwo.steps.SqlQueryingSteps;

import java.sql.SQLException;


public class SqlQueryingStepsTest {

    SqlQueryingSteps sqlQueryingSteps = new SqlQueryingSteps();

    @BeforeTest
    public void setUp() {
        sqlQueryingSteps.connection();
    }

    @Test
    public void queryingTest() throws SQLException {
        sqlQueryingSteps.insertingNewRow(1006, "Giorgi", "Davitiani", "444444444")
                .commitChanges().updatingFname();


    }
}



