package com.invoke

import java.lang.reflect.Modifier

/**
  * 反射运行类,用于提交任务
  * @author zap
  *
  */
object SparkSubmit {

  private[invoke] var class_ : String = _
  private[invoke] var targetClassArgs: Array[String] = _

  def main(args: Array[String]): Unit = {
    parseOpt(args.toList)
    val clazz: Class[_] = Class.forName(class_)
    val mainMethod = clazz.getDeclaredMethod("main", new Array[String](0).getClass)
    if (!Modifier.isStatic(mainMethod.getModifiers)) {
      throw new Exception("The main in the given main class must be static")
    }
    mainMethod.invoke(clazz, targetClassArgs)
  }

  def parseOpt(opts: Seq[String]): Unit = opts match {
    case ("--class") :: value :: tail =>
      class_ = value
      parseOpt(tail)
    case ("--args") :: tail =>
      targetClassArgs = tail.toArray
    case _ =>
  }


}
