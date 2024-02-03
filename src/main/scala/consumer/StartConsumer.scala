package io.andrelucas
package consumer

import org.apache.kafka.clients.consumer.KafkaConsumer

import java.time.Duration
import java.util.{Collections, Properties}

object StartConsumer {

  private def configMap = {
    val props = new Properties()
    props.put("bootstrap.servers", "localhost:29092")
    props.put("client.id", "scala-app-consumer")
    props.put("group.id", "scala-group")
    props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
    props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")

    props
  }

  def main(args: Array[String]): Unit = {
    val consumer = KafkaConsumer[String, String](configMap)
    consumer.subscribe(Collections.singleton("test-partitions"))

    while (true){
      val records = consumer.poll(Duration.ofMillis(200))
      records.forEach{ r =>
        println(s"Reading from - ${r.topic()} | Partition: ${r.partition()} | Key: ${r.key()}")
        println(s"Body: ${r.value()}")
      }
    }
  }
}
