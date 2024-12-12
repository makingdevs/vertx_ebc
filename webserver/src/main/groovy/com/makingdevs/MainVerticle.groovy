package com.makingdevs

import io.vertx.core.AbstractVerticle
import io.vertx.core.DeploymentOptions
import io.vertx.ext.web.Router
import io.vertx.core.json.JsonObject

class MainVerticle extends AbstractVerticle {

  private Map<Integer, JsonObject> database = [:]

  @Override
  void start() {
    println "Iniciando servidor HTTP..."

    Router router = Router.router(vertx);

    router.route('/products*').handler { ctx ->
      println ctx.request().properties
      ctx.next()
    }

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
      ctx.response()
        .putHeader('Content-Type', 'application/json')
        .end(database.values().toString())
    }

    router.get('/products/:id').handler { ctx ->
      Integer producId = ctx.pathParam("id").toInteger()
      JsonObject product = database[producId]
      if(product) {
        ctx.response()
          .putHeader('Content-Type', 'application/json')
          .end(product.encodePrettily())
      } else {
        ctx.response()
          .setStatusCode(404)
          .end("Producto con ID ${producId} no encontrado")
      }
    }

    router.post('/products').handler { ctx ->
      ctx.request().bodyHandler { body ->
        JsonObject newProduct = body.toJsonObject()

        int id = database.size() + 1
        newProduct.put('id', id)
        database[id] = newProduct

        ctx.response()
          .setStatusCode(201)
          .putHeader('Content-Type', 'application/json')
          .end(newProduct.encodePrettily())
      }
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
