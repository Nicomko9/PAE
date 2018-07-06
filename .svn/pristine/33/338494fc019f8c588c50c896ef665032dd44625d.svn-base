application.define("objects/user", function () {
  var object;
  var connected = false;
  var dto = {};

  var o = {
    init: function () {
      application.require("utils/ajax").create("/app/auth", {
        success: function (re) {
          connected = true;
          dto = re;
          object.isReady();
          object.notify("connection");
        }
      }, function () {
        connected = false;
        dto = {};

        object.isReady();
        object.notify("disconnection");

        return true;
      });
    },
    // Getters
    isResponsible: function () {
      if (!connected) {
        throw "User is not connected !";
      }
      return dto.responsible;
    },
    getName: function () {
      if (!connected) {
        throw "User is not connected !";
      }
      return dto.firstname;
    },
    getSurname: function () {
      if (!connected) {
        throw "User is not connected !";
      }
      return dto.lastname;
    },
    getLogin: function () {
      if (!connected) {
        throw "User is not connected !";
      }
      return dto.login;
    },
    isConnected: function () {
      return connected;
    },
    isNotResponsible: function () {
      return !connected || !dto.responsible;
    },
    // Methods
    connect: function (username, password, success, error) {
      application.require("utils/ajax").create("/app/connection", {
        data: {
          "login": username,
          "password": password
        },
        success: function (data) {
          success(data);
          dto = data;
          connected = true;

          object.notify("connection");
        }
      }, error);
    },
    disconnect: function () {
      application.require("utils/ajax").create("/app/disconnect", {
        success: function () {
          connected = false;
          dto = {};

          object.notify("disconnection");
        }
      })
    }
  };

  object = application.require("objects/object")("user", o);

  return o;
}());