package com.andrew_ya.gatling

import io.gatling.core.Predef._
import io.gatling.http.Predef._

import scala.concurrent.duration._

object Scenarios {



  val registrationOnly =
    exec(http("Signup") // let's give proper names, as they are displayed in the reports
      .get("/signup"))
      .pause(3.second)
      .feed(Datagen.userFeeder)
      .exec(http("Registration") // let's give proper names, as they are displayed in the reports
        .post("/register")
        .formParam("first_name", "${first_name}")
        .formParam("last_name", "${last_name}")
        .formParam("age", "${age}")
        .formParam("sex", "${sex}")
        .formParam("interests", "${interests}")
        .formParam("city", "${city}")
      )




  val viewOnly =
    feed(Datagen.idFeeder)
    .exec(http("ViewPage") // let's give proper names, as they are displayed in the reports
    .get("/user/${id}"))


  val mixed = exec(registrationOnly).exec(viewOnly)



  val registration = scenario("user's registration").exec(registrationOnly)
  val viewPages = scenario("user's page view").exec(viewOnly)
  val registrationAndView = scenario("registration and view").exec(mixed)

  def main(args: Array[String]): Unit = {


  }

}
