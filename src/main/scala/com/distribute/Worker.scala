package com.distribute

import akka.actor.{Actor, ActorSelection, ActorSystem, Props}
import com.typesafe.config.ConfigFactory

import scala.concurrent.duration._

/**
  * Worker启动后向Master发送注册信息，之后master响应master地址，worker向master定时发送心跳
  * @author zap
  * @wechat zapjone
  *
  */
class Worker extends Actor {

  var master: ActorSelection = _
  var workerId: String = "workerId"

  @scala.throws[Exception](classOf[Exception])
  override def preStart(): Unit = {
    master = context.actorSelection("akka.tcp://MasterSystem@127.0.0.1:8080/user/master")
    master ! RegisterWorker(workerId, 1024)
  }

  override def receive: Receive = {
    case RegisteredWorker(masterUrl) => {
      println(masterUrl)
      import context.dispatcher
      context.system.scheduler.schedule(0.seconds, 3.seconds, self, SendHearbeat)
    }
    case SendHearbeat => {
      println("send heartbeat to master")
      master ! Heartbeat(workerId)
    }
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
    val actorSys = ActorSystem("WorkerSystem", ConfigFactory.parseString(configStr))
    actorSys.actorOf(Props[Worker])
  }
}
