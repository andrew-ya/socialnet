package com.andrew_ya.soc_net.repository

import cats.effect.IO
import cats.implicits._
import com.andrew_ya.soc_net.domain._
import doobie._
import doobie.implicits._
import doobie.util.transactor.Transactor

class UserRepo(transactor: Transactor[IO]) {

  val insertSql = "insert into interests (user_id, interest) values (?, ?)"

  def insertUser(user: User): IO[Long] = {
    for {
      id <- sql"insert into users (first_name, last_name, age, sex, city) values (${user.firstName}, ${user.lastName}, ${user.age}, ${user.sex}, ${user.city})"
        .update.withUniqueGeneratedKeys[Long]("id").transact(transactor)
          .recover{
            case ex => println(ex); 0
          }
      interests = user.interests.map((id, _))
      _ <- Update[(Long, String)](insertSql).updateMany(interests).transact(transactor)
    } yield id
  }


  def findUserById(id: Long): IO[Either[UserNotFoundError.type, User]] = for {
       interests <- sql"select interest from interests where user_id = $id".query[String].to[List].transact(transactor)
       userdata <-  sql"select first_name, last_name, age, sex, city from users where id = $id".query[(String, String, Int, String, String)].option.transact(transactor)
         .map {
           case Some(user) => Right(user)
           case None => Left(UserNotFoundError)
         }
    } yield userdata.map(user => User(user._1, user._2, user._3,  user._4, interests, user._5))

}

