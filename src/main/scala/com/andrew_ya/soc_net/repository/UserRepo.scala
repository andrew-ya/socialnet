package com.andrew_ya.soc_net.repository

import cats.effect.IO
import cats.implicits._
import com.andrew_ya.soc_net.domain._
import doobie._
import doobie.implicits._
import doobie.util.transactor.Transactor

class UserRepo(transactor: Transactor[IO]) {

  val insertSql = "insert into users_interests (user_id, interest_id) values (?, ?)"
  implicit val han = LogHandler.jdkLogHandler

  def insertUser(user: User): IO[Long] = {
    for {
      id <- sql"insert into users (first_name, last_name, age, sex, city) values (${user.firstName}, ${user.lastName}, ${user.age}, ${user.sex}, ${user.city})"
        .update.withUniqueGeneratedKeys[Long]("id").transact(transactor)

      interests <- getInterestIds(user.interests)

      _ <- Update[(Long, Long)](insertSql).updateMany(interests.map((id, _))).transact(transactor)
    } yield id
  }


  def findUserById(id: Long): IO[Either[UserNotFoundError.type, User]] = for {
       interests <- sql"select i.interest from interests i inner join users_interests ui on i.id = ui.interest_id where ui.user_id = $id".query[String].to[List].transact(transactor)
       userdata <-  sql"select first_name, last_name, age, sex, city from users where id = $id".query[(String, String, Int, String, String)].option.transact(transactor)
         .map {
           case Some(user) => Right(user)
           case None => Left(UserNotFoundError)
         }
    } yield userdata.map(user => User(user._1, user._2, user._3,  user._4, interests, user._5))


  private def getInterestIds(interests: List[String]): IO[List[Long]] =
    interests.map { interest =>
          sql"select id from interests where interest = $interest".query[Long].option.transact(transactor).flatMap{
            case Some(id) => IO(id)
            case None => sql"insert into interests (interest) values ($interest)".update.withUniqueGeneratedKeys[Long]("id").transact(transactor)
          }
    }.sequence



}

