package com.makingdevs

import io.vertx.core.AbstractVerticle
import io.vertx.ext.web.Router

class ReceiverVerticle extends AbstractVerticle {

  @Override
  void start() {
    String instanceId =  deploymentID()
    println "ReceiverVerticle <<<<<<<<< iniciado: ${instanceId}"

    vertx.eventBus().consumer("messages.channel") { msg ->
      println msg.headers()
      println msg.body().class
      println "ReceiverVerticle: Mensaje recibido -> ${msg.body()}"

    }
  }
}
