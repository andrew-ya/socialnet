package com.andrew_ya.soc_net.db

import cats.effect.{Blocker, ContextShift, IO, Resource}
import com.andrew_ya.soc_net.config.DatabaseConfig
import doobie.hikari.HikariTransactor

import scala.concurrent.ExecutionContext

object Database {
  def transactor(config: DatabaseConfig, executionContext: ExecutionContext)(implicit contextShift: ContextShift[IO]): Resource[IO, HikariTransactor[IO]] = {
    HikariTransactor.newHikariTransactor[IO](config.driver, config.url, config.user, config.password, executionContext, Blocker.liftExecutionContext(executionContext))
  }
}
