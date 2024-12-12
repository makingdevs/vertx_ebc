package com.makingdevs

import io.vertx.core.AbstractVerticle
import io.vertx.ext.web.Router
import io.vertx.ext.bridge.PermittedOptions
import io.vertx.ext.web.handler.sockjs.SockJSBridgeOptions
import io.vertx.ext.web.handler.sockjs.SockJSHandler
import io.vertx.reactivex.ext.shell.*
import io.vertx.ext.shell.term.TelnetTermOptions
import io.vertx.ext.shell.ShellServiceOptions
import io.vertx.ext.shell.ShellService

class MainVerticle extends AbstractVerticle {

  @Override
  void start() {
    Router router = Router.router(vertx)

    PermittedOptions permittedOptions = new PermittedOptions()
      .setAddressRegex("chat\\..+");

    SockJSHandler sockJSHandler = SockJSHandler.create(vertx)
    SockJSBridgeOptions options = new SockJSBridgeOptions()
      .addInboundPermitted(permittedOptions)
      .addOutboundPermitted(permittedOptions)

    router.route("/eventbus/*").subRouter(sockJSHandler.bridge(options))

    vertx.eventBus().consumer("chat.publish") { msg ->
      println "Msg recibido en 'chat.general': ${msg.body()}"
      vertx.eventBus().publish("chat.general", msg.body())
    }

    // vertx.setPeriodic(10000) { id ->
    //   vertx.eventBus().publish("chat.general", "Timestamp: ${new Date()}")
    // }

    ShellService service = ShellService.create(vertx,
      new ShellServiceOptions().setTelnetOptions(
        new TelnetTermOptions().
        setHost("localhost").
        setPort(4000)
      )
    );
    service.start();

    vertx.createHttpServer()
      .requestHandler(router)
      .listen(8082) { http ->
        if(http.succeeded())
          println "Servidor HTTP en http://localhost:8082/eventbus"
        else
          println "Error al iniciar el server: ${http.cause()}"

      }
  }
}
