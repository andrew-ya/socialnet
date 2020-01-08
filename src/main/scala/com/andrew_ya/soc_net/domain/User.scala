package com.andrew_ya.soc_net.domain

sealed trait Sex
case object Female extends Sex
case object Male extends Sex


case class User(firstName: String, lastName: String, age: Int, sex: String, interests: List[String], city: String)

case object UserNotFoundError

