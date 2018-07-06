application.define("objects/participations", function () {
  var object;
  var participations = [];

  var o = {
    load: function (annee, success, error) {
      return application.require("utils/ajax").create(
          "/app/participations", {
            data: {annee: annee},
            success: success
          }, null, error);
    },
    pluralize: function (c, p) {
      if (typeof p == "undefined") {
        p = "s";
      }

      if (typeof c == "number") {
        return (c > 1) ? p : "";
      } else {
        return c.length > 1 ? p : ""
      }
    },
    changePresence: function (contact, annee, handler) {
      return application.require("utils/ajax").create({
        url: "/app/presences/change",
        data: {
          pk: contact,
          year: annee
        },
        success: handler
      }, application.require("utils/ajax").errorHandler(function (xhr) {
        if (xhr.responseJSON == 409) {
          application.require("objects/contacts").load(contact).done(
              function () {
                var data = application.require("objects/contacts").get(contact);
                var resp = {
                  company: {
                    pk: data.company.pk
                  }
                };
                handler(resp);
              });
          return true;
        }
        return false;
      }));
    }
  };

  object = application.require("objects/object")("je", o);

  return o;
}());