package io.andrelucas

import org.apache.kafka.clients.producer.KafkaProducer

import java.util.Properties

object StartApp {

  private val configMap = {
    val props = new Properties()
    props.put("bootstrap.servers", "localhost:9092")

    props
  }


  val producer = new KafkaProducer[String, String](configMap)

  def main(args: Array[String]): Unit = {

  }
}
