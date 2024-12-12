package com.makingdevs

import io.vertx.core.AbstractVerticle
import io.vertx.core.DeploymentOptions
import io.vertx.ext.web.Router
import io.vertx.core.json.JsonObject
import io.vertx.ext.web.handler.StaticHandler

class MainVerticleModulo4 extends AbstractVerticle {

  private Map<Integer, JsonObject> database = [:]
  private static String AUTH_TOKEN = "1234567890"

  @Override
  void start() {
    println "Iniciando servidor HTTP..."

    Router router = Router.router(vertx);

    router.route("/static/*").handler(StaticHandler.create());

    router.route('/products*').handler { ctx ->
      ctx.response()
        .putHeader('Content-Type', 'application/json')
      ctx.next()
    }

    router.route('/products*').handler { ctx ->
      String token = ctx.request().getHeader("Authorization")
      if(token != AUTH_TOKEN) {
        ctx.response()
          .setStatusCode(401)
          .end('{"Error": "No autorizado"}')
      } else {
        ctx.next()
      }
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
        .end(database.values().toString())
    }

    router.get('/products/:id').handler { ctx ->
      Integer producId = ctx.pathParam("id").toInteger()
      JsonObject product = database[producId]
      if(product) {
        ctx.response()
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
