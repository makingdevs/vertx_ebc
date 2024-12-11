package com.makingdevs

import io.vertx.core.AbstractVerticle
import io.vertx.core.DeploymentOptions

class MainVerticleModulo2 extends AbstractVerticle {

  @Override
  void start() {
    println "Desplegando Verticles"

    DeploymentOptions options = new DeploymentOptions().setInstances(10)

    vertx.deployVerticle(StandardVerticle.class.name, options) { res ->
      if(res.succeeded())
        println "StandardVerticle desplegado con ID: ${res.result()}"
      else
        println"Error al desplegar StandardVerticle: ${res.cause()}"
    }

    vertx.deployVerticle("com.makingdevs.WorkerVerticle", options) { res ->
      if(res.succeeded())
        println "WorkerVerticle desplegado con ID: ${res.result()}"
      else
        println"Error al desplegar WorkerVerticle: ${res.cause()}"
    }
  }
}
