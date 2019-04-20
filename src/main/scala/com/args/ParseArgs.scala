package com.args

/**
  * 递归解析程序运行时输入的参数，必须将输入的参数转换为集合
  * @author zap
  *
  */
object ParseArgs {

  def main(args: Array[String]): Unit = {
    parse(args.toList)
  }

  def parse(args: Seq[String]): Unit = args match {
    case ("--name") :: value :: tail =>
      println(s"name=$value")
      parse(tail)
    case ("--class") :: value :: tail =>
      println(s"class=$value")
      parse(tail)
    case _ =>
      System.exit(0)
  }

}
