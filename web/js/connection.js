application.define("connection", function () {
  return {
    connect: function () {
      application.require("utils/forms", function (module) {
        var form = module.getForm("#ConnForm");

        if (form.validate(false)) {
          application.getUser().connect(
              $("#connexion_login").val(),
              $("#connexion_password").val(),
              function () {
                form.clean();
                module.getForm("#InscForm").clean();
                notifications.notify("Connection validée", "info", "user");
              },
              function (xhr) {
                if (xhr.responseJSON == 460) {
                  var i = $("#connexion_password");
                  i.val("");
                  module.tagInput(i, "danger", "Vérifiez vos informations !");

                  return true;
                }

                alerteForm("Conn", "Error connection");

                return true;
              }
          );
        } else {
          var i = $("#connexion_password");
          i.val("");
          module.tagInput(i, "danger", "Vérifiez vos informations !");
        }
      });
    },
    disconnect: function () {
      application.getUser().disconnect(function () {
        application.require("template", "unconnected");
        notifications.notify("Déconnection validée", "info", "user");
      });
    },
    subscribe: function () {
      logger.log("connection.js", "subscribe()", "Subscription");

      application.require("utils/forms", function (module) {
        var form = module.getForm("#InscForm");

        if (form.validate(true)) {
          logger.log("connection.js", "subscribe()", form.getValues());
          $("#BtnInsc i").show();
          application.require("utils/ajax").create("/app/signup", {
            data: {
              json: JSON.stringify(form.getValues())
            },
            method: "POST",
            dataType: "json",
            success: function (data) {
              form.clean();
              module.getForm("#ConnForm").clean();
              if (data == false) {
                alerteForm("Insc", "Error creating user");
              } else {
                window.location.reload();
              }
            },
            error: function (xhr) {
              logger.log("connection.js", "subscribe()",
                  "Error signup : " + xhr.responseJSON);
              $("#BtnInsc i").hide();

              if (xhr.responseJSON == 461) {
                module.tagInput($("#subscribe_login"), "danger",
                    "Ce login est déja utilisé");
              } else if (xhr.responseJSON == 466) {
                module.tagInput($("#subscribe_email"), "danger",
                    "Cet email est déja utilisé");
              } else {
                alerteForm("Insc", errors.translate(xhr.responseJSON));
              }
            }
          });
        }

      });

    }
  }
}());