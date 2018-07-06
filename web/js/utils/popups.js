application.define("utils/popups", function () {
  var activeModal;
  var confirmIn = true;

  /**
   * Patch for closing popups
   */
  $(".modal").each(function () {
    var id = $(this).attr("id");

    if (typeof id == "undefined" || id == "") {
      return false;
    }

    var e = $("#" + id + " .row:first");

    if (!e.hasClass("clearfix")) {
      e.addClass("clearfix");
    }

    e.click(function (event) {
      if (event.target == e[0]) {
        $("#" + id).modal("hide");
        $("#popup-background").fadeOut();
      }
    });

    return true;
  });

  /**
   * Retourne un element DOM de type "modal"
   *
   * @param id
   * @returns {*}
   */
  function loadModal(id) {
    var modal = $("#popup-collection").find(".template-popup[data-template='"
        + id + "'] > div:first");

    if (modal.length == 0) {
      console.log("No modal " + id);

      return null;
    }

    return {
      id: id,
      $: modal,
      setSize: function (size) {
        if (size != "lg" && size != "xl" && size != "sm" && size != "md") {
          return;
        }

        modal.find(".modal-dialog")
        .removeClass("modal-lg modal-xl modal-sm")
        .addClass("modal-" + size);
      }
    };
  }

  $("#inner-popup-confirm a[data-id='deny']").on("click", function () {
    loadModal("confirm").$.modal('hide');
    activeModal = null;
  });

  return {
    confirm: function (action, onAccept) {
      if (confirmIn === true) {
        if (activeModal != null) {
          activeModal.$.modal('hide');
          activeModal = null;
        }

        application.require("utils/popups").load("confirm", {
          "action": action
        }, function (modal) {
          modal.setSize("md");
          var btn = modal.$.find("div.panel-footer [data-id='accept']");

          btn.off("click");
          btn.on("click", function () {
            if ($("#confirm-box").is(":checked") == true) {
              console.log("In");
              confirmIn = false;
            }
            application.require("utils/popups").unload(modal.id);
            onAccept();
          });
        });
      } else {
        onAccept();
      }

    },
    unload: function (id, callback) {
      loadModal(id).$.modal('hide');
      activeModal = null;

      loadModal(id).$.find(".alert.tmp").each(function () {
        $(this).hide();
        $(this).remove();
      });
    },
    load: function (id, data, configure, prepare, post) {
      var modal = loadModal(id);

      activeModal = modal;

      if (typeof configure === "function") {
        configure(modal);
      }

      function run() {
        if (typeof data["keep-form"] == "undefined") {

          modal.$.find("form").each(function () {
            var id = $(this).attr("id");

            if (typeof id === "undefined" || id === "") {
              return;
            }

            application.require("utils/forms").getForm("#" + id).clean();
          });

          modal.$.find("[data-attribute]").each(function () {
            var a = $(this).data("attribute");
            var v = typeof data[a] != "undefined" ? data[a] : "";

            switch ($(this).prop("tagName").toLowerCase()) {
              case "select":
                $(this).val(v);
                break;
              case "input" :
                switch ($(this).attr("type").toLocaleLowerCase()) {
                  case "checkbox":
                    if (v == true) {
                      $(this).prop("checked", true);
                    } else {
                      $(this).prop("checked", false);
                    }
                    break;
                  default:
                    $(this).val(v);
                }
                break;
              default:
                $(this).html(v);
                break;
            }
          });
        }

        modal.$.modal("show");

        if (typeof post === "function") {
          post(modal);
        }
      }

      if (typeof prepare === "function") {
        $.when(prepare).then(run);
      } else {
        run();
      }
    }
  }
}());