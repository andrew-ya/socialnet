package com.andrew_ya.soc_net.service

import java.util.concurrent.Executors

import cats.effect.{ContextShift, IO}
import com.andrew_ya.soc_net.domain.{User, UserNotFoundError}
import com.andrew_ya.soc_net.repository.UserRepo
import io.circe.{Decoder, Encoder}
import org.http4s.circe._
import io.circe.syntax._
import org.http4s.{HttpRoutes, StaticFile, Uri, UrlForm}
import org.http4s.dsl.Http4sDsl
import io.circe.generic.semiauto._
import org.http4s.headers.Location
import org.http4s.twirl._

import scala.concurrent.ExecutionContext

class UserService(userRepo: UserRepo)(implicit cs: ContextShift[IO]) extends Http4sDsl[IO] {

  implicit val userDecoder: Decoder[User] = deriveDecoder[User]
  implicit val userEncoder: Encoder[User] =  deriveEncoder[User]

  val blockingEc = ExecutionContext.fromExecutorService(Executors.newFixedThreadPool(4))

  val routes = HttpRoutes.of[IO] {
    case request@GET -> Root / "signup" =>
      StaticFile.fromResource("/html/registration_form.html", blockingEc, Some(request))
        .getOrElseF(NotFound())


    case GET -> Root / "user" / LongVar(id) =>
      userRepo.findUserById(id)
        .flatMap {
          case Left(UserNotFoundError) => NotFound()
          case Right(u) => Ok(soc_net.html.user(u))
        }


    case request @ POST -> Root / "register" =>
      request.decode[UrlForm] {
        form =>
          val user = User(
            form.values.get("last_name").flatMap(_.headOption).getOrElse(""),
            form.values.get("first_name").flatMap(_.headOption).getOrElse(""),
            form.values.get("age").flatMap(_.headOption).getOrElse("").toInt,
            form.values.get("sex").flatMap(_.headOption).getOrElse("") ,
            form.values.get("interests").flatMap(_.headOption).getOrElse("").split(",").toList,
            form.values.get("city").flatMap(_.headOption).getOrElse("")
          )
          userRepo.insertUser(user).flatMap(id => Created(user.asJson, Location(Uri.unsafeFromString(s"/user/$id"))))

      }
  }


}
