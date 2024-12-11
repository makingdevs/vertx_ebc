package com.makingdevs

import io.vertx.core.AbstractVerticle
import io.vertx.core.DeploymentOptions

class MainVerticle extends AbstractVerticle {

  @Override
  void start() {
    println "Desplegando Verticles"

    //DeploymentOptions options = new DeploymentOptions().setInstances(10)

    vertx.deployVerticle("com.makingdevs.SenderVerticle")
    vertx.deployVerticle("com.makingdevs.ReceiverVerticle")
  }
}
