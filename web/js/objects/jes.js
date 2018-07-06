application.define("objects/jes", function () {
  var loading = false;

  var object;
  var listJe = [];

  function addJe(data) {
    if (data == null) {
      return;
    }
    listJe.push(data);
    listJe.sort(function (a, b) {
      return a.pk - b.pk;
    });
    object.listPk(listJe);
    object.notify();
  }

  function date2o(o) {
    return new Date(o.year, o.monthValue, o.dayOfMonth);
  }

  /**
   * Load JE's at user loading time
   */
  application.require("objects/user").watch(function (data) {
    loading = true;

    application.require("utils/ajax").create({
      url: "/app/je/list",
      success: function (data) {
        listJe = data;
        object.listPk(listJe);
        object.isReady();
        object.notify();
        loading = false;
      }
    }, undefined, function () {
      loading = false;
    });
  }, "connection");

  var o = {
    dateFormat: function (date) {
      return ((date.dayOfMonth < 10) ? "0" : "") + date.dayOfMonth
          + "/" + ((date.monthValue < 10) ? "0" : "")
          + date.monthValue + "/" + date.year;
    },
    load: function (annee, callback) {
      if (loading) {
        setTimeout(function () {
          o.load(annee, callback);
        }, 50);

        return;
      }

      if (o.contains(annee)) {
        callback(listJe[object.getPkIndex(annee)]);
      } else {
        application.require("utils/ajax").create("/app/je", {
          data: {
            year: annee
          },
          success: function (data) {
            addJe(data);
            callback();
          }
        })
      }
    },
    create: function (date, callback, error) {
      application.require("utils/ajax").create({
        url: "/app/je/create",
        data: {date: date},
        success: function (data) {
          addJe(data);
          callback(data);
        }
      }, error);
    },
    contains: function (k) {
      return object.getPkIndex(k) != -1;
    },
    all: function () {
      return listJe
    },
    countJe: function () {
      return listJe.length;
    },
    futureJe: function () {
      return listJe.filter(function (e) {
        return e.pk > application.currentYear;
      });
    },
    pastJe: function () {
      return listJe.filter(function (e) {
        return e.pk < application.currentYear;
      });
    },
    currentJe: function () {
      return listJe.filter(function (e) {
        return e.pk == application.currentYear;
      });
    },
    isCurrent: function (o) {
      if (typeof o == "string") {
        o = parseInt(o);
      }
      if (typeof o == "number") {
        return o == application.currentYear;
      } else {
        return o.pk == application.currentYear;
      }
    },
    isPast: function (o) {
      if (typeof o == "string") {
        o = parseInt(o);
      }

      if (typeof o == "number") {
        return o < application.currentYear;
      } else {
        return date2o(o.dayDate) < new Date()
      }
    },
    isFuture: function (o) {
      if (typeof o == "string") {
        o = parseInt(o);
      }
      if (typeof o == "number") {
        return o > application.currentYear;
      } else {
        return date2o(o.dayDate) > new Date()
      }
    },
    interval: function (pk) {
      return (pk - 1) + "-" + pk;
    }
  };

  object = application.require("objects/object")("je", o);

  return o;
}());