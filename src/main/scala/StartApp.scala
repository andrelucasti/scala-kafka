package io.andrelucas

import org.apache.kafka.clients.producer.{Callback, KafkaProducer, ProducerRecord, RecordMetadata}

import java.util.Properties
import scala.util.Try

object StartApp {

  private val configMap = {
    val props = new Properties()
    props.put("bootstrap.servers", "localhost:29092")
    props.put("client.id", "KafkaProducerExample")
    props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
    props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer")


    props
  }
  val producer = new KafkaProducer[String, String](configMap)

  private val publishMessage = (topic: String,
                                message: String,
                                producer: KafkaProducer[String, String]) => {

    val record = new ProducerRecord[String, String](topic, "teste", message)
    producer.send(record, (metadata: RecordMetadata, exception: Exception) => {
      Try {
        println("Message sent to topic -> " + topic + " , partition -> " + metadata.partition() + " , offset -> " + metadata.offset())
      }.getOrElse(println("Error sending message to topic -> " + topic))
    })

    producer.flush()
  }


  def main(args: Array[String]): Unit = {
    1.to(100).foreach { i =>
      publishMessage("teste-partitions", s"Message $i", producer)
    }
  }
}
