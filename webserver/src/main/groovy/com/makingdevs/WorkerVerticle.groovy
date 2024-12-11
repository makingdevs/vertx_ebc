package com.makingdevs

import io.vertx.core.AbstractVerticle
import io.vertx.core.Promise

class WorkerVerticle extends AbstractVerticle {

  @Override
  void start(Promise<Void> startPromise) {

    String instanceId =  deploymentID()
    println "Worker Verticle iniciado: ${instanceId}"

    vertx.executeBlocking({ ->
      println "WorkerVerticle: Ejecutando tarea bloqueante..."
      Thread.sleep(8000)
      12+323+323+4+23*323+323*323*455/67 * new Random().nextDouble()
    }).onComplete({ result ->
      if(result.succeeded())
        println "WorkerVerticle OK: ${result.result()}"
      else
        println "WorkerVerticle ERROR: ${result.properties}"
    })

    startPromise.complete()
  }
}
