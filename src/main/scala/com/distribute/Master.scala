package com.distribute

import akka.actor.{Actor, ActorSystem, Props}
import com.typesafe.config.ConfigFactory

import scala.collection.mutable
import scala.concurrent.duration._

/**
  * Master接受worker的注册，包括worker的资源信息，定期检测存活的worker，
  *
  * @author zap
  * @wechat zapjone
  *
  */
class Master extends Actor {

  val workerIds = new mutable.HashMap[String, WorkerInfo]()
  val workers = new mutable.HashMap[String, Long]()


  @scala.throws[Exception](classOf[Exception])
  override def preStart(): Unit = {
    import context.dispatcher
    context.system.scheduler.schedule(0.seconds, 5.seconds, self, SendHearbeat)
  }

  override def receive: Receive = {
    case RegisterWorker(id, memory) => {
      println(s"master received: $workerIds")

      workerIds += id -> WorkerInfo(memory) // save worker resources info
      workers += id -> System.currentTimeMillis() // last register time

      sender ! RegisteredWorker("akka.tcp://MasterSystem@127.0.0.1:8080/user/master")
    }
    case SendHearbeat => {
      // 定期清除worker
      for (worker <- workers) clearWorker(worker._1, worker._2)

      println(workerIds.size)
    }
    case Heartbeat(workerId: String) => {
      // update save resources info
      workers += workerId -> System.currentTimeMillis()
    }
  }

  def clearWorker(id: String, time: Long): Unit = {
    val curTime = System.currentTimeMillis()
    if ((curTime - time) > 3000) {
      workers.remove(id)
      workerIds.remove(id)
    }
  }

}

object Master {
  def main(args: Array[String]): Unit = {
    val configStr =
      """
        |akka.actor.provider = "akka.remote.RemoteActorRefProvider"
        |akka.remote.netty.tcp.hostname = "127.0.0.1"
        |akka.remote.netty.tcp.port = "8080"
      """.stripMargin
    val actorSys = ActorSystem("MasterSystem", ConfigFactory.parseString(configStr))
    actorSys.actorOf(Props[Master], "master")
  }
}
