package com.institutosermelhor.perfomaceTest.userPerfomaceTest;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// import io.gatling.core.*;
import io.gatling.core.scenario.Simulation;

// import static io.gatling.core.CoreDsl.*;
// import static io.gatling.javaapi.http.HttpDsl.*;


public class ApiGetUserLoadTest extends Simulation { 

  
        // @Override
        // public Seq<Assertion> io$gatling$core$scenario$Simulation$$_assertions() {
        //     // TODO Auto-generated method stub
        //     return super.io$gatling$core$scenario$Simulation$$_assertions();
        // }

        // @Override
        // public SimulationParams params(GatlingConfiguration configuration) {
        //     // TODO Auto-generated method stub
        //     return super.params(configuration);
        // }

        // @Override
        // public SetUp setUp(Seq<PopulationBuilder> populationBuilders) {
        //     // TODO Auto-generated method stub
        //     return super.setUp(populationBuilders);
        // }

        HttpProtocolBuild httpProtocol = http.baseUrl("https://gatling.io");

        ScenarioBuilder scn = scenario("Scenario"); 

        { 
        setUp(scn.injectOpen(atOnceUsers(1)).protocols(httpProtocol));
        }
}
