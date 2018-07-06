application.define(
    "template",
    function () {
      var inAnimation;
      var currentDisplay;
      var currentView;

      /**
       * Watch for JE updates - update menu
       */
      application.require("objects/jes").watch(function (data) {
        $("#menu-je-futur").html("");
        $("#menu-je-history").html("");

        var past = data.pastJe();
        var $past = $("#menu-je-history");
        if (past.length == 0) {
          $past.append("<li><a href='#' class='text-muted'>"
              + "Aucune journée enregistrée</a></li>");
        }
        else {
          for (var k in past) {
            $past.prepend("<li><a data-view='je:main'>"
                + (past[k].pk - 1) + "-" + past[k].pk
                + "</a></li>");
            $past.find("a:first").attr("data-year", past[k].pk);
            $past.find("a:first").attr("data-context",
                '{ \"year\" : "' + past[k].pk + '" }');
          }
        }

        var future = data.futureJe();
        var $future = $("#menu-je-futur");
        if (future.length == 0) {
          $future.append("<li><a href='#' class='text-muted'>"
              + "Aucune journée à venir</a></li>");
        }
        else {
          for (var k in future) {
            $future.append(
                '<li><a href="#" data-view="je:main">'
                + (future[k].pk - 1) + "-" + future[k].pk
                + "</a></li>");

            $future.find("a:last").attr("data-year", future[k].pk);
            $future.find("a:last").attr("data-context", '{ \"year\" : "'
                + future[k].pk + '" }');
          }
        }
      });

      /**
       * Watch for user update - display correct page
       */
      application.require("objects/user").watch(function (data) {
        switchContainer("container-login");
      }, "disconnection");

      /**
       * Watch for user update - update data based on user profile
       */
      application.require("objects/user").watch(function (data) {
        switchContainer("container-application");
        application.require("dashboard", "main");
        $("[data-toggle='user.full_name']").html(data.getSurname()
            + " " + data.getName());
        $("#container-application").attr("data-admin", data.isResponsible());

        if (data.isResponsible()) {
          $(".admin-block").show(0);
        } else {
          $(".admin-block").hide(0);
        }
      }, "connection");

      /*
       * Change de container principal
       */
      function switchContainer(event) {
        var display;

        if (typeof event == "object") {
          event.preventDefault();
          display = $(this).attr("data-toggle");
        } else {
          display = event;
        }

        var id = "page_" + display.slice("container-".length);

        if (inAnimation || $("body").attr("id") === id) {
          return;
        }

        inAnimation = true;

        $("body > .page-container:not(" + display + ")")
        .fadeOut(0);
        $("#" + display).fadeIn(250, function () {
          inAnimation = false;
          currentDisplay = display;
          $("body").attr("id", id);
        });
      }

      /*
       * Charge une vue dynamiquement
       */
      var loadView = function (view, target, callback) {
        if (typeof target === "function" && typeof callback == "undefined") {
          callback = target;
          target = null;
        }

        // temporary patch
        if (view.indexOf(".html") === view.length - 5) {
          view = view.substr(0, view.length - 5);
        }

        if (view !== currentView) {
          var panes = $("div#page-wrapper > .inner-template[data-template='"
              + view + "']");

          if (panes.length == 0) {
            logger.log("template.js", "loadView()",
                "Unable to insert " + view + " !");
            return;
          }

          var pane = $(panes[0]);

          $("div#page-wrapper > .active").fadeOut(0, function () {
            $(this).removeClass("active");
          });

          pane.fadeIn(200, function () {
            $(this).addClass("active");
          });
          $("body").attr("data-toggle", view);
        }

        if (typeof callback != "undefined") {
          callback();
        }
      };

      /*
       * Gère un click ou envoi de formulaire associé à une
       * vue/template
       */
      function handleEvent(event) {
        var $trigger = $(this);

        if ($trigger.attr("disabled") === "disabled") {
          event.preventDefault();
          return;
        }

        function c() {
          var r = view.substr(0, view.indexOf(":"));
          var v = view.substr(view.indexOf(":") + 1);

          logger.log("template.js", "hanleEvent()",
              "Loading module " + r + " with " + v);

          application.require(r, v, context, $trigger);
        }

        event.preventDefault();

        var template = $(this).attr("data-template");
        var view = $(this).attr("data-view");
        var context = $(this).attr("data-context");

        if (typeof context != "undefined") {
          context = JSON.parse(context);
        } else {
          context = {};
        }

        if (typeof template != "undefined") {
          logger.log("template.js", "handleEvent()",
              "Loading template " + template);

          loadView(template, $(this).attr(
              "data-template-holder"), c)
        } else if (typeof view == "string") {
          c();
        }
      }

      $(document).on("click",
          "button[data-view], a[data-view], a[data-template]",
          handleEvent);
      $(document).on("submit", "form[data-view]", handleEvent);

      /*
       * View representation
       */
      return {
        loadView: loadView
      }

    }());