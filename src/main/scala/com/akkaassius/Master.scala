package com.akkaassius

import akka.actor.{Actor, ActorSystem, Props}
import com.typesafe.config.ConfigFactory

/**
  * 模拟spark的actor记录日志功能
  * @author zap
  *
  */
class Master extends Actor with ActorLogReceive {
  override def receiveWithLogging: Receive = {
    case _: String =>
      println("receive string message")
      sender() ! "ok"
  }
}
object Master{
  def main(args: Array[String]): Unit = {
    val configStr =
      """
        |akka.actor.provider = "akka.remote.RemoteActorRefProvider"
        |akka.remote.netty.tcp.hostname = "127.0.0.1"
        |akka.remote.netty.tcp.port = "8080"
      """.stripMargin
    val system = ActorSystem.apply("MasterSys", ConfigFactory.parseString(configStr))
    system.actorOf(Props[Master], "master")
    system.awaitTermination()
  }
}
