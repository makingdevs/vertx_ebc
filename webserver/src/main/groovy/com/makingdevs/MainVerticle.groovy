package com.makingdevs

import io.vertx.core.AbstractVerticle
import io.vertx.ext.web.Router

class MainVerticle extends AbstractVerticle {

  private List clients = []

  @Override
  void start() {
    Router router = Router.router(vertx);

    router.route("/ws").handler { ctx ->
      ctx.request().toWebSocket().onSuccess { ws ->
        println "Nuevo cliente ${ws.properties}"
        clients << ws

        ws.textMessageHandler { message ->
          println "Mensaje recibido: ${message}"
          // broadcastMessage("Cliente: $message")
          clients.each { c ->
            c.writeTextMessage(message)
          }
        }

        ws.closeHandler {
          println "Cliente desconectado"
          clients.remove(ws)
        }

        ws.exceptionHandler { error ->
          println "ERROR en WS: ${error.message}"
          clients.remove(ws)
        }
      }
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
