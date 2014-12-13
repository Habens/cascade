package uk.co.malbec.onlinebankingexample;


import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import org.junit.runner.RunWith;
import org.slf4j.LoggerFactory;
import uk.co.malbec.cascade.CascadeRunner;
import uk.co.malbec.cascade.annotations.*;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

@RunWith(CascadeRunner.class)
@Scan("uk.co.malbec.onlinebankingexample.steps")
//@StepPostHandler(WaitASecond.class)
public class OnlineBankingTests {

    {
        Logger root = (Logger) LoggerFactory.getLogger(org.slf4j.Logger.ROOT_LOGGER_NAME);
        root.setLevel(Level.INFO);
    }

    @FilterTests
   // Predicate filter = and(withStep(Notice.AcceptOneNotice.class), withStep(Portfolio.AllAccounts.class), withStep(SetupStandingOrder.SetupStandingOrderForNow.class), withStep(EditEmail.class));
    //Predicate filter = and(withStep(Notice.AcceptOneNotice.class), withStep(Portfolio.CurrentAccountOnly.class),  withStep(EditAddress.class), withStep(EditMobile.class));

   // @FilterTests
    /*Predicate filter = and(
            withStep(OpenLandingPage.class),
            withStep(Login.SuccessfulLogin.class),
            or(
                    withStep(Challenge.FailChallenge.class),
                    and(
                            withStep(Notice.AcceptOneNotice.class),
                            withStep(OpenAccountPage.OpenCurrentAccount.class)
                    )
            )
    );*/

    @Demands
    String username;

    @Demands
    String password;

    @Demands
    String challengePhrase;

    @Demands
    List<String> notices;

    @Demands
    List<Map> accounts;

    @Demands
    Map personalDetails;

    @Demands
    List<Map> standingOrders;

    @Demands
    List<Map> recentPayments;

    @Supplies
    List<String[]> expectedStandingOrders = new ArrayList<String[]>();

    @Supplies
    List<String[]> expectedRecentPayments = new ArrayList<String[]>();

    @Setup
    public void setup() {

        Map user = new HashMap<String, Object>() {{
            put("username", username);
            put("password", password);
            put("challengePhrase", challengePhrase);
            put("notices", notices);
            put("accounts", accounts);
            put("personalDetails", personalDetails);
            put("standingOrders", standingOrders);
            put("recentPayments", recentPayments);
        }};

        Client client = ClientBuilder.newBuilder().build();
        WebTarget target = client.target("http://localhost:8080/database/set-user");
        Response response = target.request().post(Entity.entity(user, "application/json"));


        if (response.getStatus() != 200){
            String content = response.readEntity(String.class);
            System.err.print(content);
        }

        assertEquals(200, response.getStatus());
    }

}