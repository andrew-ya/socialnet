package com.andrew_ya.gatling

import org.scalacheck.Gen

object Datagen {

  val interestGen = Gen.oneOf("sports", "music", "arts", "beer", "dating",
    "films", "watching TV",  "books", "martial arts", "yoga", "painting", "astronomy", "science")


  val firstNameGen = Gen.alphaStr
  val lastNameGen = Gen.alphaStr
  val ageGen = Gen.choose(18, 100)
  val sexGen = Gen.oneOf("M", "F")
  val interestsGen = for {
    first <- interestGen
    second <- interestGen //suchThat (_ != first)
    third <- interestGen //suchThat (x => x != first && x != second)
  } yield List(first, second, third)
  val cityGen = Gen.alphaStr


  def getUser = for {
    firstName <- firstNameGen
    lastName <- lastNameGen
    age <- ageGen
    sex <- sexGen
    interests <- interestsGen
    city <- cityGen
  } yield Map("first_name" -> firstName, "last_name" -> lastName, "age" -> age, "sex" -> sex, "interests" ->  interests.mkString(","), "city" -> city)

  val userFeeder = Iterator.continually(getUser.sample.get)

  val idFeeder = Iterator.continually(Map("id" -> (for {id <- Gen.choose(0, 1000)} yield id).sample.get))


}

case class User(firstName: String, lastName: String, age: Int, sex: String, interests: List[String], city: String)