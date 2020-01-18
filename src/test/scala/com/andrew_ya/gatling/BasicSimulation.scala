package com.andrew_ya.gatling

import io.gatling.core.Predef._
import io.gatling.http.Predef._

import scala.concurrent.duration._

class BasicSimulation extends Simulation {

  val httpProtocol = http
    .baseUrl("http://127.0.0.1:8080").shareConnections



  setUp(
    Scenarios.registration.inject( rampUsers(1000) during (50 seconds), constantUsersPerSec(100) during (90 seconds)),
    Scenarios.viewPages.inject(nothingFor(140 seconds), rampUsers(1000)  during (50 seconds), constantUsersPerSec(100) during (90 seconds)),
    Scenarios.registrationAndView.inject(nothingFor(280 seconds), rampUsers(1000)  during (50 seconds), constantUsersPerSec(100) during (90 seconds))
  ).protocols(httpProtocol)


}
