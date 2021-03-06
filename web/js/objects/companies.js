application.define("objects/companies", function () {
  var loading = false;
  var loaded = false;

  var object;

  var companies = [];

  function registerCompany(company) {
    companies.push(company);
    object.listPk(companies);
    object.notify("add", company.pk);
  }

  var o = {
    set: function (company) {
      var i = object.getPkIndex(company.pk);

      if (i == -1) {
        registerCompany(company);
      } else {
        company[i] = company;
        object.listPk(companies);
        object.notify("update", company.pk);
      }
    },
    findByName: function (name) {
      name = name.toLowerCase();
      return companies.filter(function (d) {
        return name == d.companyName.toLowerCase();
      });
    },
    all: function () {
      return companies;
    },
    create: function (data, success, error) {
      application.require("utils/ajax").create({
        url: "/app/entreprise/create",
        data: {
          json: JSON.stringify(data)
        },
        success: function (data) {
          registerCompany(data);
          success(data);
        }
      }, error);
    },
    update: function (data, success, error) {
      var d = o.get(data.pk);

      if (
          data["name"] == d.companyName
          && data["address.box"] == d.address.box
          && data["address.number"] == d.address.streetNumber
          && data["address.street"] == d.address.street
          && data["address.zip"] == d.address.zipCode
          && data["address.city"] == d.address.commune
      ) {
        console.log("Update : Data are identical");
        success(data);
        return $.when();
      }

      return application.require("utils/ajax").create({
        url: "app/entreprise/update",
        data: {json: JSON.stringify(data)},
        success: function (data) {
          var i = object.getPkIndex(data.pk);
          companies[i] = data;
          object.listPk(companies);
          object.notify("update", data.pk);
          success(data);
        }
      }, error);
    },
    list: function () {
      loading = true;
      return application.require("utils/ajax").create(
          "/app/entreprise/list", {
            success: function (data) {
              companies = data;
              object.listPk(companies);
              object.notify("list");
              loading = false;
            }
          });
    },
    findParticipations: function (k, success, error) {
      return application.require("utils/ajax").create({
        url: "/app/participations/company",
        data: {
          "company-pk": k
        },
        success: success
      }, error);
    },
    load: function (k) {
      if (object.getPkIndex(k) != -1) {
        return $.when();
      } else {
        return application.require("utils/ajax").create({
          url: "/app/company/select",
          data: {
            pk: k
          },
          success: function (data) {
            registerCompany(data);
          }
        });
      }
    },
    get: function (k) {
      if (object.getPkIndex(k) == -1) {
        console.log("Pk not found " + k);
        return undefined;
      }

      return companies[object.getPkIndex(k)];
    },
    invalidate: function (k) {
      var i = object.getPkIndex(k);
      companies.splice(i, 1);
      object.listPk();
      return o.load(k);
    },
    formatAddress: function (address) {
      if (address == null) {
        return "<i>Erreur au chargement de l'addresse</i>";
      }
      var boite = "";
      if (address.box !== 0) {
        boite = "boite " + address.box;
      }

      return address.street + " " + address.streetNumber + " "
          + boite + ", " + address.zipCode + " " + address.commune;
    },
    getPkNameList: function () {
      var list = {};

      companies.forEach(function (d) {
        list[d.pk] = d.companyName
      });

      return list;
    }
  };

  object = application.require("objects/object")("companies", o);

  return o;
}());