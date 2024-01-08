package com.institutosermelhor.perfomaceTest;

import io.gatling.javaapi.core.*;
import io.gatling.javaapi.http.HttpProtocolBuilder;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

public class ComputerDatabaseSimulation extends Simulation{ 
  
        HttpProtocolBuilder httpProtocol = http.baseUrl("http://localhost:8080");


        ChainBuilder adminGet = exec(
                http("GetUsers").get("/users").header(
                        "Authorization", "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJtYW5hZ2VyX2lzbSIsInN1YiI6InN0cmluZ0BnbWFpbCIsImV4cCI6MTcwNDgyNTA3Mn0.TgHTi40zDpaVq98rPF5v-jWwQogDNJah4YtWIZ2D2-g"
                )
        ).pause(5)
        .exec(http("GetProducts").get("/projects").header(
                        "Authorization", "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJtYW5hZ2VyX2lzbSIsInN1YiI6InN0cmluZ0BnbWFpbCIsImV4cCI6MTcwNDgyNTA3Mn0.TgHTi40zDpaVq98rPF5v-jWwQogDNJah4YtWIZ2D2-g"
                )
        ).pause(5);

        ChainBuilder browse = null;

        ChainBuilder edit = null;

        ScenarioBuilder scn = scenario("SimlationGetRequest").exec(adminGet)
        ;


        { 
        setUp(scn.injectOpen(rampUsers(1000).during(10)).protocols(httpProtocol));
        }
}