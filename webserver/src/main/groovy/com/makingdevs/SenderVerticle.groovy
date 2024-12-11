package com.makingdevs

import io.vertx.core.AbstractVerticle
import io.vertx.ext.web.Router

class SenderVerticle extends AbstractVerticle {

  @Override
  void start() {
    String instanceId =  deploymentID()
    println "SenderVerticle >>>>>>>>> iniciado: ${instanceId}"

    vertx.setPeriodic(5000) { id ->
      String message = "Mensaje ${System.currentTimeMillis()}"
      println "SenderVerticle: Envía mensaje -> $message"
      vertx.eventBus().send("messages.channel", message)
    }
  }
}
