application.define("utils/loader", function () {

  function updateListCompanies(data) {
    logger.log("loader.js", "updateListCompanies", "In");
    var table = "#companies-table";

    var ligne = $(table + " tr[data-pk='" + data.pk + "'");

    if (ligne.length === 0) {
      if ($(table + " tbody tr:first").hasClass("no-contact")) {
        companyDataTable.row(".no-contact").remove().draw();
      }
    } else {
      // ligne exists
      companyDataTable.row("[data-pk='" + data.pk + "']").remove();
    }

    var boite = "";
    if (data.address.box !== 0) {
      boite = "boite " + data.address.box;
    }

    var row = "<tr data-pk='" + data.pk + "' data-version='" + data.version
        + "'>"
        + "<td class='col-sm-2 col-xs-2'>"
        + data.companyName
        + "</td>"
        + "<td class='col-sm-3 col-xs-3' data-pk='" + data.address.pk
        + "' data-version='" + data.address.version + "'>"
        + data.address.street + ", " + data.address.streetNumber + " "
        + boite + ", " + data.address.zipCode + ", " + data.address.commune
        + "</td>"
        + "<td class='col-sm-2 col-xs-2'>"
        + ((data.lastParticipationYear == 0)
            ? "<span class='text-muted'>Pas encore de participation</span>"
            : data.lastParticipationYear)
        + "</td>"
        + "<td class='col-sm-2 col-xs-2 text-center'>"
        + "<button name='vue-entreprise' data-view='entreprise:display' class='col-lg-4 col-md-4"
        + " col-lg-offset-1 col-md-offset-1 btn btn-active'>"
        + "Visualiser</button><button name='edit-entreprise'"
        + " class='col-lg-4 col-md-4 col-lg-offset-2 col-md-offset-2"
        + " btn btn-active' data-view='entreprise:edit'>"
        + "Editer</button>"
        + "</td>"
        + "</tr>";

    companyDataTable.row.add($(row)).draw();
  }

  function updateListContact(data, table) {
    logger.log("loader.js", "updateListContact", "In");
    $("#popup-new-contact").modal("hide");
    var popup = false;
    if (table === "#popup-contact-table") {
      popup = true;
    }
    var ligne = $(table + " tr[data-pk='" + data.pk + "'");

    // In case of create
    if (ligne.length === 0) {
      if ($(table + " tbody tr:first").hasClass("no-contact")) {
        $(table + " tbody tr.no-contact").remove();
      }

      $("table tbody").prepend("<tr data-pk='" + data.pk + "' data-version='"
          + data.version + "'> "
          + "<td class='col-sm-2 col-xs-2'></td>"
          + "<td class='col-sm-3 col-xs-3'></td>"
          + "<td class='col-sm-2 col-xs-2'></td>"
          + "<td class='col-sm-3 col-xs-3'></td>"
          + "<td class='col-sm-2 col-xs-2 text-center'></td>"
          + "</tr>");
      ligne = $(table + " tr[data-pk='" + data.pk + "'");
    }

    if (ligne.lenght !== 0) {
      ligne.data("version", data.version);
      ligne = ligne.find("td:first");
      if (!popup) {
        ligne.text(data.company.companyName);
        ligne = ligne.next('td');
        ligne.text(data.firstname + " " + data.lastname);
      } else {
        ligne.text(data.firstname);
        ligne = ligne.next('td');
        ligne.text(data.lastname);
      }
      ligne = ligne.next('td');
      ligne.text(data.phone);
      ligne = ligne.next('td');
      ligne.text(data.email);
      ligne = ligne.next('td');
      ligne.html("<button name='edit-contact' class='btn btn-active'"
          + "data-view='contacts:edit'>"
          + "Editer</button>");
    }
  }

  /**
   * Show the contact list.
   *
   * @param data
   *            data to show
   * @param table
   *            id of table
   * @param contact
   *            true if contact false if company
   * @param edit
   *            true if edit mode false if visualisation
   */
  function showContactGen(data, contact, table, edit, company) {
    $("" + table + " tbody").html("");
    if (!contact && edit) {
      $("" + table + " tbody").html(setFormContPopupForm(company));
    }
    if (data.length === 0) {
      showNoData(table, "showContactGen()");
    } else {
      for (var i in data) {
        var o = data[i];
        var str = "<tr data-pk='" + o.pk + "' data-version='" + o.version
            + "'>";
        if (contact) {
          if (o.company === null) {
            str += "<td><span class='text-muted'>Pas de société enregistrée</span></td>"
          } else {
            str += "<td data-pk='" + o.company.pk + "'>" + o.company.companyName
                + "</td>";
          }
        } else {
          $("#popup-list-entreprise-contact").val(o.company.companyName);
        }
        str += "<td class='col-sm-2 col-xs-2'>"
            + o.firstname + "</td><td class='col-sm-3 col-xs-3'>"
            + o.lastname + "</td>" + showPhoneMail(o);
        if (contact) {
          str += "<button name='edit-contact' class='btn btn-active'"
              + "data-view='contacts:edit'>"
              + "Editer</button>";
        } else {
          if (o.active == true) {
            str += "<div class='label label-info'>actif</div>";
          } else {
            str += "<div class='label label-warning'>inactif</div>";
          }
        }
        if (edit) {
          $(table + " tbody").prepend(str + "</td></tr>");
        } else {
          $(table + " tbody").append(str + "</td></tr>");
        }
      }
      if (contact && contactsTable == null) {
        contactsTable = $("#contact-table table").DataTable(
            {
              columns: [null, null, null, null,
                {orderable: false, searchable: false}]
            });
      }
    }
    if (contact) {
      $("#contact-loading").hide(250);
    } else {
      $("#popup-contact-loading").hide(250);
    }

  }

  function showPhoneMail(o) {
    return "<td class='col-sm-2 col-xs-2'>" + o.phone + "</td>"
        + "<td class='col-sm-3 col-xs-3'>" + o.email + "</td>"
  }

  function showNoData(table, methode) {
    logger.log("loader.js", "showNoData()", methode + " - No data");
    $(table + " tbody").prepend("<tr class='no-contact'><td colspan='"
        + $(table + " thead th").length + "'><p class='text-center text-muted'>"
        + "Aucun contact enregistré</p></td></tr>")
  }

  function setFormContPopupForm(company) {
    return "<tr data-pk='' data-version=''><input type='hidden' name='companyPk'"
        + "id='popup-contact-company' value='" + company + "'>"
        + "<td class='col-sm-2 col-xs-2'><input type='text'"
        + " id='firstname-contact-entreprise' name='firstname-contact'"
        + " placeholder='Prénom' data-required='true'>"
        + "</td><td class='col-sm-3 col-xs-3'><input type='text'"
        + " id='lastname-contact-entreprise' name='lastname-contact'"
        + " placeholder='Nom' data-required='true'></td><td class="
        + "'col-sm-3 col-xs-3'><input type='tel'id='tel-contact-entreprise'"
        + " name='tel-contact'placeholder='0477/00.00.00'></td><td class="
        + "'col-sm-3 col-xs-3'><input type='email'id='email-contact-entreprise'"
        + " name='email-contact' placeholder='nom.prenom@entreprise.be'></td>"
        + "<td class='col-sm-1 col-xs-1'><button class='fa fa-plus btn"
        + " btn-success btn-circle'id='add-contact-entreprise'></button></td></tr>"
  }

  var contactsTable = null;
  var companyDataTable = null;

  function showCompanies(list) {
    $("#companies-table tbody").html("");

    if (list.length === 0) {
      $("#companies-table tbody").append("<tr>"
          + "<td></td>"
          + "<td class='col-lg-2 col-md-2'></td>"
          + "<td class='col-lg-4 col-md-4'></td>"
          + "<td class='col-lg-2 col-md-2'></td>"
          + "<td class='col-lg-4 col-md-4 text-center'>"
          + "	<a data-view='entreprise:display' class='btn btn-active'>"
          + "		Visualiser</a>"
          + "<button name='entreprise:edit' class='btn btn-active'>"
          + "Editer</button>" + "</td>" + "</tr>");
    } else {
      for (var i in list) {
        var o = list[i];
        var boite = "";
        if (o.address.box === 0) {
        } else {
          boite = "boite " + o.address.box;
        }

        $("#companies-table tbody").append(
            "<tr data-pk='" + o.pk + "' data-version='" + "" + o.version
            + "'><td class='col-lg-2 col-md-2' data-toggle='company-name'>"
            + o.companyName + "</td><td class='col-lg-4 col-md-4' "
            + "data-pk='" + o.address.pk + "' data-version='"
            + o.address.version + "'>"
            + o.address.street + ", " + o.address.streetNumber + " "
            + boite + ", " + o.address.zipCode + ", " + o.address.commune
            + "</td><td class='col-lg-2 col-md-2' data-order='"
            + o.lastParticipationYear + "'>" + ((o.lastParticipationYear == 0)
                ? "<span class='text-muted'>Pas encore de participation</span>"
                : o.lastParticipationYear)
            + "</td><td class='col-lg-4 col-md-4 text-center'><row>"
            + "<button name='vue-entreprise' data-view='entreprise:display' class='col-lg-4 col-md-4"
            + " col-lg-offset-1 col-md-offset-1 btn btn-active'>"
            + "Visualiser</button>"
            + "<button name='edit-entreprise'"
            + " class='col-lg-4 col-md-4 col-lg-offset-2 col-md-offset-2"
            + " btn btn-active' data-view='entreprise:edit'>"
            + "Editer</button></row></td></tr>");
      }

      if (companyDataTable == null) {
        companyDataTable = $("#companies-table table").DataTable(
            {columns: [null, null, null, {orderable: false}]});
      }
    }
    $("#entreprise-loading").hide(250);
  }

  /**
   * LoadContact from server.
   *
   * @param contact
   *            true if it is from contact.js
   * @param table
   *            id of table
   * @param data
   *            button clicked
   * @param edit
   *            true if edit mode false if visulasition
   */
  function privateLoadContact(contact, table, data, edit) {
    var path = "/app/contact/list";
    var param, company;

    // case if company
    if (!contact) {
      path += "ForCompany";
      company = param = data.parents("tr").data("pk");
    } else {
      param = data.text();
    }
    $.ajax({
      url: path,
      method: "POST",
      data: {
        json: param
      },
      dataType: "JSON",
      success: function (data) {
        showContactGen(data, contact, table, edit, company)
      },
      error: function (xhr) {
        logger.log("loader.js", "listContact()", xhr.responseJSON);
      }
    });
  }

  function privateLoadCompanies() {
    $("#entreprise-loading").show(250);
    $.ajax({
      url: "/app/entreprise/list",
      method: "POST",
      dataType: "JSON",
      success: function (data) {
        showCompanies(data);
      },
      error: function (data) {
        logger.log("loader.js", "loadCompanies()", "Erreur");
      }
    });
  }

  return {
    loadContact: function (cont, table, data, edit) {
      logger.log("loader.js", "listContact()", "List");
      $("#contact-loading").show(250);
      privateLoadContact(cont, table, data, edit);
    },
    loadCompanies: function () {
      privateLoadCompanies();
    },
    loadSelectedCompanies: function () {
      $("#selected-entreprise-loading").show(250);
      logger.log("loader.js", "loadSelectedCompanies()", "List");
    },
    fillColor: function (table) {
      $(table).find("input").each(function () {
        if ($(this).val() !== "") {
          $(this).css("background-color", "#FAFAD2");
          $(this).focus(function () {
            $(this).css("background-color", "");
          });
        }
      });
    },
    /**
     * Create - Update Contact or Company.
     *
     * @param action
     *            true if update false if create
     * @param form
     *            the form with the values
     * @param object
     *            true if contact false if company
     * @param message
     *            the message to show
     */
    crudAjax: function (action, form, object, message) {
      application.require("utils/forms", function (module) {
        var formulaire = module.getForm(form);

        if (!formulaire.validate()) {
          console.log("Form values not OK");
          return;
        }

        var data = JSON.stringify(formulaire.getValues());
        if (!object) {
          object = "entreprise";
        } else {
          object = "contact"
        }
        if (!action) {
          action = "create";
        } else {
          action = "update";
        }
        console.log(data);
        if (formulaire.validate(true)) {
          $.ajax({
            url: "/app/" + object + "/" + action,
            data: {
              json: data
            },
            method: "POST",
            dataType: "JSON",
            success: function (data) {
              formulaire.clean();
              logger.log("loader.js", "crudAjax()", "Success");
              if (object === "contact") {
                var table = "#contact-table";
                if (form === "#popup-cont-comp-form") {
                  table = "#popup-contact-table"
                }
                updateListContact(data, table);
              } else {
                updateListCompanies(data);
              }
              $("#popup-new-" + object).modal('hide');
              notifications.notify(message, "success", "user");
            },
            error: function (xhr) {
              logger.log("loader.js", "crudAjax()", xhr.responseJSON);

              if (xhr.responseJSON == 473) {
                module.tagInput($("#popup-name-entreprise"),
                    "danger", errors
                    .translate(xhr.repsonseJSON));
              } else {
                notifications
                .notify(errors
                    .translate(xhr.responseJSON),
                    "warning");
              }
            }
          });
        }
      });
    }
  }
}());