application.define("utils/forms", function () {

  function findInputGroup(el) {
    return $(el.parents(".form-group")[0]);
  }

  function tagValidation(el, type, message) {
    var e = findInputGroup(el);
    if (typeof message != "undefined" && message != "") {
      if (e.children('.control-block').length == 0) {
        e.append('<span class="control-block help-block"></span>');
      }
      e.children('.help-block.control-block').fadeIn(function () {
        $(this).html(message);
      }, 0)
    } else {
      e.children('.help-block.control-block').fadeOut(0);
    }

    e.removeClass("has-success has-warning has-error");
    e.addClass("has-" + ((type == "success") ? "success" : "error"));
  }

  function validateInput(input, feedback) {
    var val = input.val();

    if (input.attr("type") === "submit" || typeof input.attr("name")
        == "undefined"
        || input.attr("name") == "") {
      return true;
    }

    if (input.attr("data-required") == "true") {
      if (typeof val == "undefined" || val == "" || val == 0) {
        if (feedback) {
          tagValidation(input, "error", "Ce champ est obligatoire");
        }
        return false;
      }
    } else {
      if (typeof val == "undefined" || val == "" || (val == 0 && input.attr(
              "name") == "box")) {
        if (feedback) {
          tagValidation(input, "success");
        }
        return true;
      }
    }

    if (["name", "city", "lastname-contact", "firstname-contact"].indexOf(
            input.attr("name")) != -1) {
      val = input.val().charAt(0).toUpperCase() + input.val().slice(1);
      input.val(val);
    }

    if (input.attr('type') === "tel" && input.val() != "") {
      if (!/^((\+|00)32\s?|0)4(\d{2})\/(\s?\d{2}\.){2}(\s?\d{2})$/.test(
              input.val())) {
        if (feedback) {
          tagValidation(input, "warning",
              "Veuillez respecter le format : (+32xxx|xxxx)/xx.xx.xx");
        }
        return false;
      }
    }

    if (input.attr('type') === "email" && val != "") {
      if (!/^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/.test(
              input.val())) {
        if (feedback) {
          tagValidation(input, "warning",
              "Veuillez respecter le format d'email usuel");
        }
        return false;
      }
    }

    if (input.attr("name") === "box") {
      if (!/^([0-9]){0,3}$/.test(input.val())) {
        tagValidation(input, "warning",
            "La boite doit être comprise entre 1 et 999");
        return false;
      }
    }

    if (input.attr("data-copy-of")) {
      var ref = $(input.attr("data-copy-of"));

      if (ref.length == 0) {
        console.log("Matching against invalid selector");
        return false;
      }

      if (ref.val() != val) {
        if (feedback) {
          tagValidation(input, "warning",
              "Les informations ne correspondent pas !");
        }
        return false;
      }
    }

    if (input.attr("data-min") || input.attr("data-max")) {
      var min = parseInt(input.attr("data-min"));
      var max = parseInt(input.attr("data-max"));

      if (val.length < min || val.length > max) {
        if (feedback) {
          if (isNaN(min)) {
            tagValidation(input, "error",
                "Votre " + input.attr("name").toLocaleLowerCase()
                + " doit contenir maximum " + max + " caractères");
          } else if (isNaN(max)) {
            tagValidation(input, "error",
                "Votre " + input.attr("name").toLocaleLowerCase()
                + " doit contenir minimum " + min + " caractères");
          } else {
            tagValidation(input, "error",
                "Votre " + input.attr("name").toLocaleLowerCase()
                + " doit contenir entre " + min + " et " + max + " caractères");
          }
        }

        return false;
      }
    }

    if (typeof input.attr("data-pattern") !== "undefined") {
      var pattern = input.attr("data-pattern");

      if (!new RegExp(pattern).test(val)) {
        if (feedback) {
          tagValidation(input, "error", input.attr('data-pattern-message'));
        }
        return false;
      }
    }

    if (feedback) {
      tagValidation(input, "success");
    }

    return true;
  }

  return {
    tagInput: function (el, type, msg) {
      if (!el instanceof jQuery) {
        el = $(el);
      }
      tagValidation(el, type, msg);
    },
    getForm: function (e) {
      return function () {
        var root = $(e);
        var fields = $(e + " :input");

        if (root.length == 0) {
          throw "Form doesn't exists !";
        }

        return {
          $: root,
          getInputs: function () {
            return root.children(":input");
          },
          validate: function (feedback) {
            $("#" + root[0].id + "Alert > .tmp").hide(0);

            if (typeof feedback == "undefined") {
              feedback = true;
            }
            var ok = true;

            for (var i = 0; i < fields.length; i++) {
              if (false === validateInput($(fields[i]), feedback)) {
                ok = false;
              }
            }

            return ok;
          },
          clean: function () {
            for (var i = 0; i < fields.length; i++) {
              var el = $(fields[i]);

              if (typeof el.attr("type") == "undefined" || el.attr(
                      "type").toLowerCase() !== "checkbox") {
                el.val("");
              } else {
                $(this).removeAttr("checked");
              }
              el.css("background-color", "");

              findInputGroup(el)
              .removeClass("has-success has-warning has-error")
              .children(".control-block").hide();
            }
          },
          getValues: function () {
            var values = {};

            for (var i = 0; i < fields.length; i++) {
              var data = $(fields[i]);
              if (typeof data.attr("name") != "undefined" && data.attr("name")
                  != ""
                  && data.attr("type") != "submit") {
                if (typeof data.attr("type") != "undefined" && data.attr(
                        "type").toLowerCase() == "checkbox") {
                  values[data.attr("name")] = data.is(":checked");
                } else {
                  values[data.attr("name")] = data.val();
                }
              }
            }

            return values;
          }
        }
      }();
    }
  }
}());