package com.makingdevs

import io.vertx.core.AbstractVerticle
import io.vertx.core.DeploymentOptions
import io.vertx.ext.web.Router

class MainVerticle extends AbstractVerticle {

  @Override
  void start() {
    println "Iniciando servidor HTTP..."

    Router router = Router.router(vertx);

    configureRoutes(router)

    vertx.createHttpServer()
      .requestHandler(router)
      .listen(8083) { http ->
        if(http.succeeded())
          println "Servidor HTTP en http://localhost:8083"
        else
          println "Error al iniciar el server: ${http.cause()}"
      }
  }

  private void configureRoutes(Router router) {

    router.get('/').handler { ctx ->
      ctx.response()
        .putHeader('Content-type', 'text/plain')
        .end('Bienvenido a la API REST con Vert.x y Groovy')
    }

    router.get('/products').handler { ctx ->
      ctx.response().end("Lista de productos")
    }

    router.get('/products/:id').handler { ctx ->
      String producId = ctx.pathParam("id")
      ctx.response().end("Producto ${producId}")
    }

    router.post('/products').handler { ctx ->
      ctx.response().setStatusCode(201).end("Producto creado")
    }

    router.put('/products/:id').handler { ctx ->
      String producId = ctx.pathParam("id")
      ctx.response().end("Producto ${producId} Actualizado")
    }

    router.delete('/products/:id').handler { ctx ->
      String producId = ctx.pathParam("id")
      ctx.response().end("Producto ${producId} Eliminado")
    }

  }
}
