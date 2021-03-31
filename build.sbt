name := "ctor"

version := "0.1.1"

organization := "net.zygfryd"
organizationName := "zygfryd's projects"
organizationHomepage := Some(url("https://zygfryd.net/"))

description := "A tiny macro library for Scala that lets your generic code gain access to constructors."

licenses := List("CC0 1.0" -> new URL("https://creativecommons.org/publicdomain/zero/1.0/legalcode"))

lazy val scala211 = "2.11.12"
lazy val scala212 = "2.12.13"
lazy val scala213 = "2.13.5"
lazy val supportedScalaVersions = List(scala212, scala213, scala211)

scalaVersion := scala212
crossScalaVersions := supportedScalaVersions

libraryDependencies ++= Seq("org.scala-lang" % "scala-reflect" % scalaVersion.value,
                            "org.scalatest" %% "scalatest" % "3.0.9" % "test")

scalacOptions ++= Seq("-sourcepath", (baseDirectory in ThisBuild).value.getAbsolutePath,
                      "-deprecation",
                      "-feature",
                      "-unchecked",
                      "-language:experimental.macros",
                      "-Xfatal-warnings",
                      "-Xlint:inaccessible",
                      "-Xlint:infer-any",
                      "-Xlint:missing-interpolator",
                      "-Xlint:option-implicit",
                      "-Xlint:poly-implicit-overload",
                      "-Xlint:type-parameter-shadow")
