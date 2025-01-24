package dao

import doobie._
import doobie.implicits._
import cats.effect.IO
import cats.implicits._

import models.Temperatura
import config.Database

object TemperaturaDAO {
  def insert(temperatura: Temperatura): ConnectionIO[Int] = {
    sql"""
     INSERT INTO temperaturas (dia, temperatura_manana, temperatura_tarde)
     VALUES (
       ${temperatura.dia},
       ${temperatura.temp_dia},
       ${temperatura.temp_tarde}
     )
   """.update.run
  }

  def insertAll(temperaturas: List[Temperatura]): IO[List[Int]] = {
    Database.transactor.use { xa =>
      temperaturas.traverse(t => insert(t).transact(xa))
    }
  }
}
