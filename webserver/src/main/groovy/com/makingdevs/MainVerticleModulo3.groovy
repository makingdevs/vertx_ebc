package com.makingdevs

import io.vertx.core.AbstractVerticle
import io.vertx.core.DeploymentOptions

class MainVerticleModulo3 extends AbstractVerticle {

  @Override
  void start() {
    println "Desplegando Verticles"

    DeploymentOptions options = new DeploymentOptions().setInstances(5)

    vertx.deployVerticle("com.makingdevs.SenderVerticle")
    vertx.deployVerticle("com.makingdevs.ReceiverVerticle", options)

    vertx.createNetServer().connectHandler({}).listen(20) { socket ->
      println socket.properties
    }
  }
}
