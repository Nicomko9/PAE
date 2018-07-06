var logger = function(){
	//public methods
	return {
		"log" : function(file, method, mess){
			// console.log("[" + file + " - " + method + "] \n" + mess);
		}
	}
}();

var notifications = function () {
  // Attributes
  var container = $("#notification-area");
  var counter = 0;
  var timeout = 5000;

  // Stores running notification
  var notifications = {};

  // Private : return next notification id from 1 to 100
  function nextId() {
    counter = ((++counter % 100) + 1);

    return counter;
  }

  // On action, hide notifications
  $(".page-container").on("click", function () {
    for (var id in notifications) {
      notifications[id].fadeOut(function () {
        delete notifications[id];
      })
    }
  });

  // Public "methods"
  return {
    "setTimeout": function (t) {
      if (typeof t != "integer") {
        throw "setTimeout require a integer";
      }

      timeout = t;
    },
    "notify": function (message, type, icon) {
      var id = nextId();

      if (typeof message != "string") {
        throw "System notification require at least a string message";
      }

      if (typeof type == "undefined" || type == "" || type == null) {
        type = "info";
      }

      container.prepend("<div id='notification_" + id
          + "' class='notice notification'><div class='oaerror " + type
          + "'>" + message + "</div></div>");

      var notification = $("#notification_" + id);

      notifications[id] = notification;

      if (typeof icon == "string") {
        notification.find(".oaerror").prepend("<i class='fa fa-fw fa-" + icon
            + "'></i>&nbsp;&nbsp;");
      }

      setTimeout(function () {
        notification.fadeOut(function () {
          $("#notification_" + id).remove();
          delete notifications[id];
        });
      }, timeout);
    }
  }
}();

var errors = function () {
  // error id => message
  var messages = {
    0: "Erreur non-définie",
    200: "Ok",
    201: "Créé",
    202: "Super User",
    203: "Creates Denied",
    400: "Mauvaise requête",
    401: "No Cookies",
    404: "Page Fault",
    409: "Données modifiées en cours de traitement. Veuillez les recharcher.",
    444: "Pas de Réponse",
    460: "Données non correctes",
    461: "Login utilisateur déjà utilisé",
    462: "Login trop long",
    463: "Format de l'email incorrect",
    464: "Mot de passe incorrect",
    465: "Login incorrect",
    466: "Email déjà utilisé",
    467: "Utilisateur déjà connecté",
    468: "Erreur Json",
    469: "Pas de Je a cette date la.",
    470: "Il existe déja une JE à cette date",
    471: "Date dans un format non reconnu par l'application",
    472: "Informations incorrectes",
    473: "Nom d'entreprise déjà utilisé",
    474: "Contact invalide",
    475: "Id déjà utilisé pour ce contact",
    476: "Nom de l'entreprise invalide",
    477: "Entreprise invalide",
    478: "Email déja utilisé",
    479: "Utilisateur invalide",
    480: "Adresse invalide",
    481: "Participation invalide",
    482: "Présence invalide",
    483: "JE invalide",
    484: "Etat de participation invalide",
    500: "Erreur interne server",
    503: "Service non disponible"
  };

  return {
    notify: function (message, icon) {
      if (message instanceof Number) {
        message = application.errors.translate(message);
      }
      application.notifications.notify(message, "warning", icon);
    },
    translate: function (code, defaultMsg) {
      if (typeof messages[code] != "undefined") {
        return messages[code];
      } else {
        return typeof defaultMsg == "string" ? defaultMsg : "Erreur serveur non répertoriée";
      }
    }
  };
}();

var application = function () {
  var init    = [];
  var loading = true;

  var date = new Date();
  var currentYear = date.getFullYear();

  var views = {};

  // Year : 2017, if september : current year is next one
  if (date.getMonth() > 9) {
    currentYear++;
  }


  return {
    onReady   : function(callback) {
      init[init.length] = callback
    },
    datatable : {
      translations : {
        "sProcessing":     "Traitement en cours...",
        "sSearch":         "Rechercher&nbsp;:",
        "sLengthMenu":     "Afficher _MENU_ &eacute;l&eacute;ments",
        "sInfo":           "Affichage de l'&eacute;l&eacute;ment _START_ &agrave; _END_ sur _TOTAL_ &eacute;l&eacute;ments",
        "sInfoEmpty":      "Affichage de l'&eacute;l&eacute;ment 0 &agrave; 0 sur 0 &eacute;l&eacute;ment",
        "sInfoFiltered":   "(filtr&eacute; de _MAX_ &eacute;l&eacute;ments au total)",
        "sInfoPostFix":    "",
        "sLoadingRecords": "Chargement en cours...",
        "sZeroRecords":    "Aucun &eacute;l&eacute;ment &agrave; afficher",
        "sEmptyTable":     "Aucune donn&eacute;e disponible dans le tableau",
        "oPaginate": {
          "sFirst":      "Premier",
          "sPrevious":   "Pr&eacute;c&eacute;dent",
          "sNext":       "Suivant",
          "sLast":       "Dernier"
        },
        "oAria": {
          "sSortAscending":  ": activer pour trier la colonne par ordre croissant",
          "sSortDescending": ": activer pour trier la colonne par ordre d&eacute;croissant"
        }
      }
    },
    // Generic components
    errors: errors,
    notifications: notifications,
    ajax  : null,

    // attributes
    currentYear: currentYear,

    load: function () {
      var listener;
      while (listener = init.pop()) {
        listener();
      }

      $("a").each(function() {
        var href = $(this).attr("href");
        if (href == "#" || href == "") {
          $(this).removeAttr("href");
        }
      });
    },
    // methods
    isLoading: function () {
      return loading;
    },
    getUser: function () {
      return views["objects/user"];
    },
    define: function (id, data) {
      views[id] = data;
    },
    require: function (id, callback, context, $e) {
      if (typeof views[id] == "undefined") {
        logger.log("script.js", "application.require",
            "Deprecated ! Usage of require through AJAX ! This will lead to bugs (loading " + id + ")");
        logger.log("script.js", "application.require", "Exiting");
        return;
      }

      if (typeof callback == "undefined") {
        return views[id];
      } else if (typeof callback == "string"
          && typeof views[id][callback] == "function") {
        return views[id][callback](views[id][callback], context, $e);
      } else if (typeof callback == "function") {
        return callback(views[id], context, $e);
      }
    }
  };
}();

function alerteForm(form, message) {
	$("#" + form + "FormAlert").html("");
	$("#" + form + "FormAlert")
  	.append(
      "<div class='text-center tmp alert alert-warning alert-dismissable' "
      + "style='display:none'><button type='button' class='close' data-dismiss='alert'"
      + "aria-hidden='true'>&times;</button><p><strong>Attention</strong>" +
      		"<hr class='message-inner-separator'>" + message + "</p></div>");
  $("#" + form + "FormAlert > .tmp").show(1000);

  setTimeout(function() {
    $("#" + form + "FormAlert > .tmp").hide(500, function() {
      $(this).remove();
    });
  }, 10000);
}
