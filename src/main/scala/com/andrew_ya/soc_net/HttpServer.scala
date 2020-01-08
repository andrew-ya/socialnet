package com.andrew_ya.soc_net

import cats.effect._
import com.andrew_ya.soc_net.config.Config
import com.andrew_ya.soc_net.db.Database
import com.andrew_ya.soc_net.repository.UserRepo
import com.andrew_ya.soc_net.service.UserService
import doobie.hikari.HikariTransactor
import doobie.util.ExecutionContexts
import org.http4s.server.blaze.BlazeServerBuilder
import org.http4s.implicits._

object HttpServer {
  def create(implicit contextShift: ContextShift[IO], concurrentEffect: ConcurrentEffect[IO], timer: Timer[IO]): IO[ExitCode] = {
    resources.use(create)
  }

  private def resources(implicit contextShift: ContextShift[IO]): Resource[IO, Resources] = {
    for {
      config <- Resource.liftF(Config.load())
      ec <- ExecutionContexts.fixedThreadPool[IO](config.database.threadPoolSize)
      transactor <- Database.transactor(config.database, ec)
    } yield Resources(transactor, config)
  }

  private def create(resources: Resources)(implicit concurrentEffect: ConcurrentEffect[IO], timer: Timer[IO], cs: ContextShift[IO]): IO[ExitCode] = {
    val repository = new UserRepo(resources.transactor)
    for {
//      _ <- Database.initialize(resources.transactor)

      exitCode <- BlazeServerBuilder[IO]
        .bindHttp(resources.config.server.port, resources.config.server.host)
        .withHttpApp(new UserService(repository).routes.orNotFound).serve.compile.lastOrError
    } yield exitCode
  }

  case class Resources(transactor: HikariTransactor[IO], config: Config)
}
