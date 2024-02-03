package io.andrelucas
package producer

import org.apache.kafka.clients.producer.{Callback, KafkaProducer, ProducerRecord, RecordMetadata}

import java.util.Properties
import scala.util.Try

object StartProducer {

  val executor = java.util.concurrent.Executors.newScheduledThreadPool(10)

  private def configMap = {
    val props = new Properties()
    props.put("bootstrap.servers", "localhost:29092")
    props.put("client.id", "KafkaProducerExample")
    props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
    props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer")

    props
  }
  private val syncDeliveryChannel:Callback = (metadata: RecordMetadata,
                                              exception: Exception) => {
    Thread.sleep(200)
    if(Try(exception).isSuccess){
      println(s"Message sent to topic -> ${metadata.topic()}, " +
        s"partition -> ${metadata.partition()}, " +
        s"offset -> ${metadata.offset()} " +
        s"current thread -> ${Thread.currentThread().getName}")

    } else {
      println(s"Error while sending message ${metadata.topic()}")
    }
  }

  private val asyncDeliveryChannel: Callback = (metadata: RecordMetadata,
                                               exception: Exception) => {
    def exec = executor.submit(new Runnable {
      override def run(): Unit = {
        Thread.sleep(200)
        println(s"Message sent to topic -> ${metadata.topic()}, " +
          s"partition -> ${metadata.partition()}, " +
          s"offset -> ${metadata.offset()} " +
          s"current thread -> ${Thread.currentThread().getName}")
      }
    })

    exec

  }

  private val publishMessage = (topic: String, message: String, producer: KafkaProducer[String, String]) => {
    val record = new ProducerRecord[String, String](topic, message)
    producer.send(record, asyncDeliveryChannel)
    producer.flush()
  }

  def main(args: Array[String]): Unit = {
    println(s"current thread -> ${Thread.currentThread().getName}")
    val producer = new KafkaProducer[String, String](configMap)
    1.to(1000).foreach { i =>
      Thread.sleep(200)
      println(s"Sending message $i -> ${Thread.currentThread().getName}")
      publishMessage("test-partitions", s"Messages $i", producer)
    }
  }
}
