package com.makingdevs

import io.vertx.core.AbstractVerticle
import io.vertx.ext.web.Router

class MainVerticleModulo1 extends AbstractVerticle {

  @Override
  void start() {
    String id = deploymentID()
    println "Verticle iniciado: ${id}"

    Router router = Router.router(vertx);

    router.route().handler { ctx ->
      println "Request entrante: ${ctx.request().method()} ${ctx.request().uri()}"
      ctx.next()
    }

    router.get("/").handler { ctx ->
      ctx.response()
        .putHeader('content-type', 'text/plain')
        .end("Hola desde Vert.x y Groovy")
    }

    router.post("/").handler { ctx ->
      ctx.response()
        .putHeader('content-type', 'text/plain')
        .end("Datos enviados")
    }

    router.get("/info/*").handler { ctx ->
      def response = [
       status: 'ok',
       version: '1.0.0',
       message: 'Info desde Vert.x'
      ]

      ctx.response()
        .putHeader('content-type', 'application/json')
        .end(response as String)
    }

    vertx.createHttpServer()
      .requestHandler(router)
      .listen(8081) { http ->
        if(http.succeeded())
          println "Servidor HTTP en http://localhost:8081"
        else
          println "Error al iniciar el server: ${http.cause()}"

      }

    vertx.createHttpServer()
      .requestHandler(router)
      .listen(8082) { http ->
        if(http.succeeded())
          println "Servidor HTTP en http://localhost:8082"
        else
          println "Error al iniciar el server: ${http.cause()}"

      }
  }
}
