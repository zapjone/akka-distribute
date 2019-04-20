package com.akkaassius

import akka.actor.Actor

/**
  * 模拟spark的actor记录日志的功能
  * @author zap
  *
  */
private[akkaassius] trait ActorLogReceive {

  //TODO self：当前对象必须是Actor或其子类
  _self: Actor =>

  //这个receive必须要写返回值，否则报self的问题
  override def receive: Actor.Receive = new Actor.Receive {

  	//TODO 类似于JS中的函数劫持
    private val _receiveWithLogging = receiveWithLogging

    override def isDefinedAt(o: Any): Boolean = _receiveWithLogging.isDefinedAt(o)

    override def apply(o: Any): Unit = {
      println(s"[actor] received message $o from ${_self.sender}")
      val start = System.nanoTime()
      _receiveWithLogging.apply(o)
      val timeTaken = (System.nanoTime() - start).toDouble / 1000000
      println(s"[actor] handled message ($timeTaken ms) $o from ${_self.sender}")
    }
  }

  def receiveWithLogging: Actor.Receive

}
