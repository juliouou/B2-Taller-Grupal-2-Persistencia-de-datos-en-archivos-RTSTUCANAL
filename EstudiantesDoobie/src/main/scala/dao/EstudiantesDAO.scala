package dao

import doobie._
import doobie.implicits._
import cats.effect.IO
import cats.implicits._

import models.Estudiantes
import config.Database

object EstudiantesDAO {
  def insert(estudiante: Estudiantes): ConnectionIO[Int] = {
    sql"""
     INSERT INTO estudiantes (nombre, edad, calificacion, genero)
     VALUES (
       ${estudiante.nombre},
       ${estudiante.edad},
       ${estudiante.calificacion},
       ${estudiante.genero}
     )
   """.update.run
  }

  def insertAll(estudiantes: List[Estudiantes]): IO[List[Int]] = {
    Database.transactor.use { xa =>
      estudiantes.traverse(t => insert(t).transact(xa))
    }
  }

  def obtenerTodos: ConnectionIO[List[(String, Int, Int, String)]] =
    sql"SELECT nombre, edad, calificacion, genero FROM estudiantes".query[(String, Int, Int, String)].to[List]
}
