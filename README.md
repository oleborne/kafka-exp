# Kafka Experimentation

Small test bench to expirement with Kafka and Spring.

Points studied on Spring/Kafka

- Producer semantics
- Consumer semantics
- Annotation-based Kafka components registration
- Programmatic Kafka components registration
- Back-off pattern

## Connecting to a Event Hubs namespace

Azure Event Hubs supports Kakfa protocol.
To connect to an Event Hubs namespace, update the `application.properties` file by replacing the following values:

* **EVENTHUB_NAME**: the name of your Event Hubs namespace (Properties > Name) 
* **EVENTHUB_SAS_CONNECTIONSTRING**: the connection string for a *Shared access policy* that can Manage, Send
  and Listen.

## Points studied  

### Kafka Configuration

With the connection information, "Spring auto-configuration magic" kicks-in and you can already start producing and
consuming messages with no further configuration required.

#### Kafka Factories

Using customization of the default factories allows to retain most of that magic without having to "rebuild the world
from scratch" if you need to alter just a specific aspect of the configuration
(see `DefaultKafkaProducerFactoryCustomizer` and `DefaultKafkaConsumerFactoryCustomizer` usage).

In addition, the default factories could be used to jump start the creation of custom factories.

### Serialization

The default serializer is the `StringSerializer`. This can not serialize a Java object into Kafka byte array format.

Avro would typically be the go-to data format to use when interacting with Kafka. However, this is usually done in a
bigger context, notably with access to a schema registry. If you don't already have such an infrastructure ready, you
would need to start doing some customization.
In addition, if you don't intend to use Kafka as a integration infrastructure but just internally in a component,
a shared schema registry is not relevant.

JSON is a less compact format than Avro. However, there's readily available tools to serialize/deserialize Java objects.
For use case where Kafka is used inside a single component and there's no strong pressure on performance, then
JSON is more than "good enough".

### Producing messages

Messages are publishing using the `KafkaTemplate` interface. The returned value is a `ListenableFuture` that needs
to be bridged to a `Mono` to be integrate with Reactor.

### Consuming messages

Spring provides convenient annotations to declare and register listeners.

You can control the topics, the group id and concurrency.

You can also specify a particular factory to notably influence the deserializer used.



