package com.distribute

/**
  * 通信消息
  * @author zap
  * @wechat zapjone
  *
  */
trait RemoteMessage extends Serializable

// worker -> master
case class RegisterWorker(id: String, memory: Int) extends RemoteMessage

// master -> worker
case class RegisteredWorker(masterUrl: String) extends RemoteMessage

// worker resources
case class WorkerInfo(memory: Int) extends RemoteMessage

// heartbeat message
case class Heartbeat(id: String) extends RemoteMessage

// send self message
case object SendHearbeat


