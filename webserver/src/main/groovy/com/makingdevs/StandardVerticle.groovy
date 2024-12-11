package com.makingdevs

import io.vertx.core.AbstractVerticle
import io.vertx.ext.web.Router

class StandardVerticle extends AbstractVerticle {

  @Override
  void start() {
    String instanceId =  deploymentID()
    println "Standard Verticle iniciado: ${instanceId}"

    vertx.setPeriodic(5000) { id ->
      println "StandardVerticle (${instanceId}): Tarea en el Event loop"
    }
  }
}
