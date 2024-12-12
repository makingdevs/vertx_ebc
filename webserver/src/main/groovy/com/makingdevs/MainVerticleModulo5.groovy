package com.makingdevs

import io.vertx.core.AbstractVerticle
import io.vertx.core.DeploymentOptions
import io.vertx.ext.web.Router
import io.vertx.core.json.JsonObject
import io.vertx.ext.web.handler.StaticHandler
import io.vertx.sqlclient.*

class MainVerticleModulo5 extends AbstractVerticle {

  private Pool client

  @Override
  void start() {
    println "Iniciando servidor HTTP..."

    SqlConnectOptions connectOptions = new SqlConnectOptions(
      host: "localhost",
      port: 5432,
      database: "exampledb",
      user: "postgres",
      password: "postgres")

    PoolOptions poolOptions = new PoolOptions(maxSize: 5)
    client = Pool.pool(vertx, connectOptions, poolOptions)

    Router router = Router.router(vertx);

    router.route("/static/*").handler(StaticHandler.create());

    router.route('/products*').handler { ctx ->
      ctx.response()
        .putHeader('Content-Type', 'application/json')
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
      client.query("SELECT * FROM products").execute { operation ->
        if(operation.succeeded()){
          def products =  operation.result().collect { row ->
            [id: row.getInteger("id"), name: row.getString("name"), price: row.getNumeric("price")]
          }

          ctx.response().end(JsonObject.of("products", products).encodePrettily())
          // ctx.response().end(JsonObject.of("products", products).toBuffer())
        } else {
          ctx.response().setStatusCode(500).end("Error al obtener productos")
        }
      }
    }

    router.get('/products/:id').handler { ctx ->
      Integer productId = ctx.pathParam("id").toInteger()
      client.preparedQuery('SELECT * FROM products WHERE id = $1').execute(Tuple.of(productId)) { operation ->
        if(operation.succeeded() && operation.result().size() > 0){
          def row = operation.result().iterator().next()
          def product = [
            id: row.getInteger("id"),
            name: row.getString("name"),
            price: row.getNumeric("price")
          ]

          ctx.response().end(JsonObject.of("product", product).toBuffer())
        } else {
          ctx.response().setStatusCode(404).end('{"error": "Producto ' + productId + ' NO encontrado"}')
        }
      }
    }

    router.post('/products').handler { ctx ->
      ctx.request().bodyHandler { body ->
        JsonObject newProduct = body.toJsonObject()

        if(!newProduct.containsKey('name') || !newProduct.containsKey('price'))
          ctx.response().setStatusCode(400).end('{"error": "name y price son obligados"}')

        client.preparedQuery('INSERT INTO products(name, price) VALUES($1, $2) RETURNING id')
          .execute(Tuple.of(newProduct.getString("name"), newProduct.getString("price").toBigDecimal())) { operation ->
            if(operation.succeeded()) {
              Integer id = operation.result().iterator().next().getInteger("id")
              newProduct.put("id", id)

              ctx.response()
                .setStatusCode(201)
                .end(newProduct.encodePrettily())
            } else {
              ctx.response().setStatusCode(500).end("Error al crear producto: ${operation.cause()}")
            }
          }

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
