application.define("objects/contacts", function () {
  var object;
  var loading = false;
  var contacts = [];

  function registerContact(contact) {
    contacts.push(contact);
    object.listPk(contacts);
    object.notify("add", contact.pk);
  }

  var o = {
    set: function (contact) {
      var i = object.getPkIndex(contact.pk);

      if (i == -1) {
        registerContact(contact);
      } else {
        contacts[i] = contact;
        object.listPk(contacts);
        object.notify("update", contact.pk);
      }
    },
    findByEmail: function (email) {
      return contacts.filter(function (d) {
        return email == d.email;
      });
    },
    all: function () {
      return contacts;
    },
    list: function () {
      application.require("utils/ajax").create({
        url: "/app/contact/list",
        success: function (data) {
          contacts = data;
          object.listPk(contacts);
          object.notify("list");
        }
      });
    },
    listForCompany: function (company, success, error) {
      return application.require("utils/ajax").create({
        url: "/app/contact/listForCompany",
        data: {json: company},
        success: function (data) {
          for (var k in data) {
            if (object.getPkIndex(k) != -1) {
              registerContact(data[k]);
            }
          }
          success(data);
        }
      }, error);
    },
    create: function (contact, success, error) {
      return application.require("utils/ajax").create({
        url: "/app/contact/create",
        data: {
          json: JSON.stringify(contact)
        },
        success: function (data) {
          registerContact(data);
          success(data);
        }
      }, error);
    },
    update: function (contact, success, error) {
      var data = o.get(contact.pk);

      if (
          data.company.pk == contact.companyPk &&
          data.version == contact.version &&
          data.lastname == contact["lastname-contact"] &&
          data.firstname == contact["firstname-contact"] &&
          data.email == contact["email-contact"] &&
          data.phone == contact["tel-contact"] &&
          data.active == contact.active
      ) {
        console.log("Update : Data are identical");
        success(data);
        return $.when();
      }

      return application.require("utils/ajax").create({
        url: "/app/contact/update",
        data: {
          json: JSON.stringify(contact)
        },
        success: function (data) {
          var i = object.getPkIndex(data.pk);
          contacts[i] = data;
          object.listPk(contacts);
          object.notify("update", data.pk);
          success(data);
        }
      }, error);
    },
    load: function (k) {
      if (object.getPkIndex(k) != -1) {
        return $.when();
      } else {
        return application.require("utils/ajax").create({
          url: "/app/contact/select",
          data: {
            pk: k
          },
          success: function (data) {
            registerContact(data);
          }
        });
      }
    },
    invalidate: function (k) {
      var i = object.getPkIndex(k);
      contacts.splice(i, 1);
      object.listPk();
      return o.load(k);
    },
    get: function (k) {
      if (object.getPkIndex(k) == -1) {
        console.log("Pk not found " + k);
        return undefined;
      }

      return contacts[object.getPkIndex(k)];
    },
    toRow: function (o) {
      var str = "<tr data-pk='" + o.pk + "' data-version='" + o.version + "'>";

      str += "<td class='col-sm-2 col-xs-2'>"
          + o.firstname + "</td><td class='col-sm-3 col-xs-3'>"
          + o.lastname + "</td>" + "<td class='col-sm-2 col-xs-2'>" + o.phone
          + "</td>"
          + "<td class='col-sm-3 col-xs-3'>" + o.email
          + "</td><td class='text-center'>"
          + ((o.active == true)
              ? "<span class='label label-primary'>Actif</span>"
              : "<span class='label label-default'>Inactif</span>") + "</td>";

      return str + "</tr>";
    }
  };

  object = application.require("objects/object")("contacts", o);

  return o;
}());