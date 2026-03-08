# Logstamp: effective log filtering to track process

**Logstamp** is an extensible Java library to pass your stamps (identifiers and other process features) to every log message through the whole process. You need to add stamp in one place, and it's passed to all next messages of your processes. When you filter the log by this stamp, you can see all log messages of it.

The main logic of logstamp is around three next parts: StampContext, StampExtractor and interceptors.

[StampContext](logstamp-core-starter/src/main/java/io/github/shurupov/logstamp/core/StampContext.java) holds your stamps. So you can add stamps to context by executing `add(...)` method, remove stamps by `remove(...)`, clear context for current thread by `clear()` and so on.

[StampExtractor](logstamp-core-starter/src/main/java/io/github/shurupov/logstamp/extractor/StampExtractor.java) is an interface. You can implement it to extract stamps from any incoming object. Some implementations are already done in following packages, but you can add yours.

Interceptors are of two types: `receivers` and `transmitters`. `Receiver` handles incoming message, passes it to all stamp extractors and put all extracted stamps to context. `Transmitter` gets stamps from context and passes them to outgoing message. Interceptors does not implement single interface. They should be integrated to a framework you use to handle or pass messages according to the framework rules.

