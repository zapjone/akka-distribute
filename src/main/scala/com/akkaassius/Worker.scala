package com.akkaassius

import akka.actor.{Actor, ActorSystem, Props}
import com.typesafe.config.ConfigFactory

/**
  * 模拟Spark的actor的记录日志功能
  * @author zap
  *
  */
class Worker extends Actor with ActorLogReceive {

  override def preStart(): Unit = {
    val masterActor = context.actorSelection("akka.tcp://MasterSys@127.0.0.1:8080/user/master")
    masterActor ! "my is worker"
  }

  override def receiveWithLogging: Receive = {
    case msg: String => println(s"receive message from master:$msg")
  }
}

object Worker{
  def main(args: Array[String]): Unit = {
    val configStr =
      """
        |akka.actor.provider = "akka.remote.RemoteActorRefProvider"
        |akka.remote.netty.tcp.hostname = "127.0.0.1"
        |akka.remote.netty.tcp.port = "9090"
      """.stripMargin
    val system = ActorSystem("WorkerSys", ConfigFactory.parseString(configStr))
    system.actorOf(Props[Worker], "worker")
    system.awaitTermination()
  }
}
