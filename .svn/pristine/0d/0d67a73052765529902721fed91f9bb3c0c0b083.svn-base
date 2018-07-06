application.define("utils/ajax", function () {
  var loading = 0;

  var defaults = {
    method: "POST",
    dataType: "JSON"
  };

  function merge(query) {
    for (var k in defaults) {
      if (typeof query[k] == "undefined") {
        query[k] = defaults[k];
      }
    }

    return query;
  }

  function startLoading() {
    ++loading;
    if (loading == 1) {
      $("#loading-status").fadeIn(200);
    }
  }

  function stopLoading() {
    if (loading > 0) {
      --loading;
    }
    if (loading == 0) {
      setTimeout(function () {
        if (loading == 0) {
          $("#loading-status").fadeOut(500);
        }
      }, 300);
    }
  }

  /**
   *
   * @param handler
   * @param context
   * @param defaultMsg
   * @returns {Function}
   */
  function errorHandler(handler, context, defaultMsg) {
    return function (xhr) {
      console.log("Handling error code " + xhr.responseJSON);

      if (typeof handler == "function") {
        if (handler(xhr)) {
          return true;
        }
      }

      var msg = errors.translate(xhr.responseJSON, defaultMsg);

      switch (xhr.responseJSON) {
          // Handle all unhandled previous error code
        case 409:
          //Optimistic lock
          application.notifications.notify("La donnée que vous souhaitez "
              + "modifier a déja été modifiée par un autre utilisateur : "
              + "rechargez l'affichage");
          break;
        case 401 :
          application.require("utils/popups").load("error", {
            title: "Erreur de connection",
            message: "Il semblerait que vous ne soyez pas connecté, et que vous "
            + "ne puissiez dès lors pas accéder à cette page",
            button: "Ok !"
          }, function (modal) {
            modal.$.find("[data-source='button']").off("click");
            modal.$.find("[data-source='button']").on("click", function () {
              window.location.reload();
            });
          });
        default:
          if (typeof context == "undefined" || context == "") {
            errors.notify(errors.translate(xhr.responseJSON), "danger");
          } else if (context instanceof jQuery) {
            context.html("<div class='alert alert-warning'> "
                + msg
                + "</div>"
            );
            setTimeout(context.find('.alert').each(function () {
              $(this).fadeOut(350);
            }), 5000);
          }
          break;
      }

      return true;
    };
  }

  return {
    errorHandler: errorHandler,
    /**
     * Create an ajax request object
     * @param url url to call if not defined, pulled from conf
     * @param conf configurations (classical $.ajax({})
     * @param handler specific errors handler, return true to ignore default handler
     * @returns {*}
     */
    create: function (url, conf, handler) {
      if (typeof url != "string") {
        handler = conf;
        conf = url;
        url = conf.url;
        delete conf.url;
      }

      startLoading();

      return $.ajax(url, merge(conf)).fail(errorHandler(handler)).always(
          stopLoading);
    },
    stopLoading: stopLoading
  }
}());