package com.makingdevs

import io.vertx.core.AbstractVerticle
import io.vertx.ext.web.Router

class ReceiverVerticle extends AbstractVerticle {

  @Override
  void start() {
    String instanceId =  deploymentID()
    println "ReceiverVerticle <<<<<<<<< iniciado: ${instanceId}"

    vertx.eventBus().consumer("messages.channel") { msg ->
      println "ReceiverVerticle: Mensaje recibido -> ${msg.body()}"
      // msg.reply("Enviando respuesta: ${msg.body()}-OK")
      // msg ok
      // if msg.user.group == "ADMIN", notify
    }

    vertx.eventBus().consumer("messages.special") { msg ->
      println "ReceiverVerticle - Special: Mensaje recibido -> ${msg.body()}"
      // msg.reply("Enviando respuesta: ${msg.body()}-OK")
    }
  }
}
