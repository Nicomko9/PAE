application.define("je", function () {
  // Dependencies
  var jeObject = application.require("objects/jes");
  var participations = application.require("objects/participations");
  var companiesObject = application.require("objects/companies");
  var contactObject = application.require("objects/contacts");

  // Generic
  var layout = $(".inner-template[data-template='je']");
  var current = 0;

  // Libraries
  var $picker = $("#new-je-date").pickadate({
    formatSubmit: 'dd/mm/yyyy',
    hiddenName: true,
    today: false,
    clear: false,
    close: false
  });

  var datatable = $("#participations-table table").DataTable({
    "columnDefs": [
      {
        "render": function (data, type, row) {
          return data.companyName + "<div class='pull-right row-actions'>"
              + "<div class='btn btn-default btn-xs' data-id='visualise'>"
              + "Visualiser</div></div>";
        },
        "targets": 0
      },
      {
        "render": function (data, type, row) {
          return insertContactPresence(data, row[2]);
        },
        "targets": 1
      },
      {
        "render": function (data) {
          return "<h4><span class='label label-" + (function (e) {
                switch (e) {
                  case "refusée":
                  case "annulée":
                    return "default'>" + e;
                    //break;
                  case "invitée":
                  case "confirmée":
                  case "payée":
                  case "facturée":
                  default:
                    return "primary'>" + e;
                    //break;
                }
              }(data)) + "</span></h4>";
        },
        "targets": 2
      },
      {
        "render": function (data, type, row) {
          return "<div class='dropdown'>"
              + "<button data-toggle='dropdown' class='btn btn-default dropdown-toggle'> "
              + "Gérer <span class='caret'></span>"
              + "</button>"
              + "<ul class='dropdown-menu participation-update'>"
              + displayEtat(data.state)
              + "</ul>"
              + "</div>";
        },
        "targets": 3
      }
    ],
    language: application.datatable.translations,
    columns: [
      {
        className: 'col-md-3'
      },
      {
        className: 'col-md-5',
        orderable: false,
        searchable: false
      },
      {
        className: 'text-center col-md-2'
      },
      {
        orderable: false,
        className: "text-center col-md-2",
        searchable: false
      }
    ]
  });

  $("#participations-table tbody").on("click",
      ".row-actions [data-id='visualise']", function () {
        var pk = $(this).closest("tr").attr("data-pk");

        application.require("entreprise").display(null, {pk: pk});
      });

  companiesObject.watch(function (d, k) {
    var data = datatable.row("[data-pk='" + k + "']").data();

    if (typeof data == "undefined") {
      return true;
    }

    var rowData = $(datatable.row("[data-pk='" + k + "']").node()).data("data");
    rowData.company = d.get(k);
    updateRow(rowData);
  }, "update");

  contactObject.watch(function (d, k) {
    var contact = d.get(k);
    var pk = contact.company.pk;

    if (typeof datatable.row("[data-pk='" + pk + "']").data() == "undefined") {
      return true;
    }

    var rowData = $(datatable.row("[data-pk='" + pk + "']").node()).data(
        "data");

    for (var i in rowData.contacts) {
      if (rowData.contacts[i].pk == k) {
        rowData.contacts[i] = contact;

        break;
      }
    }

    updateRow(rowData);
  }, "update");

  contactObject.watch(function (d, k) {
    var contact = d.get(k);
    var pk = contact.company.pk;

    if (typeof datatable.row("[data-pk='" + pk + "']").data() == "undefined") {
      return true;
    }

    var rowData = $(datatable.row("[data-pk='" + pk + "']").node()).data(
        "data");

    rowData.contacts.push(contact);

    updateRow(rowData);
  }, "add");

  /*
   * start row handling
   */
  function updateRow(data) {
    var row = datatable.row("[data-pk='" + data.company.pk + "']");
    row.remove().draw();

    insertRow(data);
  }

  function insertRow(data) {
    if ($(datatable.row("[data-pk='" + data.company.pk + "']").node()).length
        != 0) {
      updateRow(data);
      return;
    }

    var row = datatable.row.add([
      data.company,
      data.contacts,
      data.state,
      {
        company: data.company.pk,
        je: data.je.pk,
        state: data.state,
        lastState: data.lastState
      }
    ]).draw().node();

    var $row = $(row);
    $row.attr("data-pk", data.company.pk);
    $row.data("version", data.version);
    $row.data("data", data);
  }

  /*
   * End row handling
   */

  function exportAllInvited() {
    var listPk = [];
    $("#csv-all-loading").show();
    datatable.rows().iterator('row', function (context, index) {
      var node = $(this.row(index).node());

      listPk.push(node.attr("data-pk"));
    });

    ajaxCsv(listPk);
  }

  var inviteTable = $("#popup-tab-invite").DataTable({
    "columnDefs": [
      {
        render: function (data) {
          return (data != 0) ? data
              : "<span class='text-muted'>Aucune participation</span>";
        },
        targets: 2
      },
      {
        render: function (data) {
          var str = "<input name='invite' type='checkbox'";
          switch (data) {
            case 2:
              str += " checked='checked' disabled='disabled'";
              break;
            case 1:
              str += " checked='checked'";
              break;
          }
          return str + " />";
        },
        targets: 3
      }
    ],
    language: application.datatable.translations
  });

  function ajaxCsv(pks) {
    if (application.getUser().isNotResponsible()) {
      return notifications.notify("Vous devez être Administrateur", "warning");
    }

    function escapeCsv(str) {
      return '\"' + str + '\"';
    }

    application.require("utils/ajax").create({
      url: "/app/contacts/forCompanies",
      method: "POST",
      dataType: "JSON",
      data: {json: JSON.stringify(pks)},
      success: function (response) {
        var csvData = [];

        csvData.push(["Société", "Prénom", "Nom", "Email", "Téléphone"]);

        for (var k in response) {
          var contact = response[k];

          if (contact.email == "" || contact.email == null) {
            continue;
          }

          csvData.push([
            escapeCsv(contact.company.companyName),
            escapeCsv(contact.firstname),
            escapeCsv(contact.lastname),
            escapeCsv(contact.email),
            escapeCsv(contact.phone)
          ]);
        }

        exportCsv(csvData);
      }
    });
  }

  function mainPrivate(context) {
    $("#je-message").hide(0);

    if (typeof context == "undefined") {
      current = application.currentYear;
    } else {
      current = parseInt(context.year);
    }

    jeObject.load(current, function (data) {
      if (data != null) {
        if (jeObject.isPast(current)) {
          $("#je-message").show(0);
          $("#je-message").find(".alert").html(
              "<p>Cette journée d'entreprise est déja passée, certaines"
              + " fonctionnalités ne fonctionneront pas ou seront désactivée !</p>"
          );
        }
        displayJe(data);

        return;
      }

      if (jeObject.isCurrent(current)) {
        application.require("template").loadView("je", function () {
          layout.find("#je-message").each(function () {
            $(this).show();
            $(this).find("div").html("<h1><strong>Attention</strong></h1><hr>"
                + "<p>Pas de JE pour l'année en cours.</p><br><button"
                + " class='btn btn-default admin-block' data-view='je:showCreate'"
                + ">Lancer Je</button>");
            $(this).find("div").addClass("text-center");
          });
          $("#launch-invites").hide();
          $("#manage-je").hide();

          if (application.getUser().isResponsible()) {
            application.require("je", "showCreate", {
              year: application.currentYear
            });
          }
        });
      } else if (jeObject.isPast(current)) {
        notifications.notify("Cette JE n'existe pas !", "warning");
      } else if (jeObject.isFuture(current)) {
        notifications.notify("Cette JE n'existe pas encore !", "warning");
      } else {
        // Invalid scenario
        notifications.notify("Scenario invalide !", "danger");
      }
    });
  }

  function sendInvites(dl) {
    logger.log("je.js", "sendInvite()", "Send");

    if (application.getUser().isNotResponsible()) {
      return notifications.notify("Vous devez être Administrateur", "warning");
    }

    var n = false;
    var listPk = [];

    if (datatable.row().count() === 0) {
      n = true;
    }

    var invites = [];

    inviteTable.rows().iterator('row', function (context, index) {
      var node = $(this.row(index).node());

      if (node.find("td:last input[type='checkbox']").is(":checked")) {
        if (node.attr("data-checked") == "false" || n) {
          invites.push(node.attr("data-pk"));
        }
      }
    });

    application.require("utils/popups").unload("invitations");

    application.require("utils/ajax").create({
      url: "app/je/invite",
      method: "POST",
      dataType: "JSON",
      data: {
        year: $("#participations-table").data("year"),
        json: JSON.stringify(invites)
      },
      success: function (data) {
        logger.log("je.js", "sendInvites()",
            "Succesfull");
        mainPrivate({year: $("#participations-table").data("year")});

        if (dl) {
          ajaxCsv(invites);
        }
      }
    }, application.require("utils/ajax").errorHandler());
  }

  function displayJe(je) {
    datatable.clear().draw();

    application.require("template").loadView("je.html", null, function () {
      $("#launch-invites").hide(0);
      $("#manage-je").show();
      $("#participations-table").data("year", je.pk);

      if (je != null) {
        loadParticipations(je.pk);
        $("[data-attribute='date-je']").html(jeObject.dateFormat(je.dayDate));
        $("[data-attribute='year-je']").html(jeObject.interval(je.pk));

        current = je.pk;
        $(".inner-template[data-template='je'] [data-visibility]").hide(0);
        if (jeObject.isPast(je.pk)) {
          $(".inner-template[data-template='je'] [data-visibility='past']").show(
              0);
          $("#btn-invite").hide();
        } else {
          $(".inner-template[data-template='je'] [data-visibility='future']").show(
              0);
          $("#btn-invite").show();
        }
      }
    });
  }

  function loadParticipations(year) {
    participations.load(year, function (data) {
      if (data.length == 0) {
        $("#manage-je").hide(0);
        $("#launch-invites").show(0);
      } else {
        $("#manage-je").show();
        $("#launch-invites").hide(0);
        for (var k in data) {
          insertRow(data[k]);
        }
      }
    }, function () {
      logger.log("je.js", "loadParticipations()", "Erreur");
      alerteForm("Je", "Impossible de charger les participations le CSV");
    });
  }

  function showCreatePrivate() {
    if (application.getUser().isNotResponsible()) {
      return application.notify(
          "Vous devez être administrateur pour effectuer cette action");
    }

    application.require("utils/popups").load("launch-je", {},
        function (modal) {
          modal.setSize("md");
          modal.$.find("fieldset.year-choice").show(0);
          modal.$.find("fieldset.date-choice").hide(0);

          if (modal.$.find("fieldset.year-choice").hasClass("error")) {
            return;
          }

          var select = modal.$.find("fieldset.year-choice select");
          select.html("");

          for (var i = 0; i < 5; i++) {
            if (!jeObject.contains(application.currentYear + i)) {
              select.append(
                  "<option value='" + (application.currentYear + i) + "'>"
                  + (application.currentYear + i - 1) + "-"
                  + (application.currentYear + i)
                  + "</option>")
            }
          }

          if (select.find("option").length === 0) {
            modal.$.find("fieldset.year-choice").addClass("error");
            modal.$.find("fieldset.year-choice").html(
                "<div class='alert alert-warning'><strong>"
                + "Il y a déja des journées d'entreprise planifiée pour les 4 prochaines années !"
                + "</strong></div>"
            );
          }
        });
  }

  function selectDatePrivate(year) {
    if (application.getUser().isNotResponsible()) {
      return application.notify(
          "Vous devez être administrateur pour effectuer cette action");
    }

    application.require("utils/popups").load("launch-je", {}, function (modal) {
      modal.setSize("md");
      modal.$.find("fieldset.year-choice").hide(0);
      modal.$.find("fieldset.date-choice").show(0);

      var year = typeof year != "undefined" ? year
          : modal.$.find("fieldset.year-choice select").val();

      var min = new Date(year - 1, 8, 1);
      if (jeObject.isCurrent(year)) {
        min = new Date();
        min.setTime(min.getTime() + (7 * 24 * 60 * 60 * 1000));
      }

      modal.$.find("fieldset.date-choice input[type='date']").hide(0);

      var picker = $picker.pickadate("picker");
      picker.clear();
      picker.set("min", min);
      picker.set("max", new Date(year, 5, 30));
      picker.open(false);
    });
  }

  function createPrivate() {
    if (!application.getUser().isConnected()
        || !application.getUser().isResponsible()) {
      alert("Vous devez etre administrateur");
      return;
    }

    var date = $("#new-je-date").val();
    var m = date.match(/(\d+)\s([^\s]+)\s(\d+)/);

    date = m[1] + "/" + (jQuery.fn.pickadate.defaults.monthsFull.indexOf(m[2])
        + 1) + "/" + m[3];
    application.require("utils/popups").confirm(
        "Créer une journée entreprise pour le " + date,
        function () {
          jeObject.create(date, function (data) {
            application.require("utils/popups").unload("launch-je");
            application.require("je", "main", {year: data.pk});
          });
        },
        function () {
          // @todo add placheolder for JE creation errors

        }
    );
  }

  function insertContactPresence(data, state) {
    var nbPresence = 0;
    var nbContact = data.length;
    var str = "";
    var first = "<div class='col-xs-12'><span name='td-normal' class='text-muted'>";

    if (data.length !== 0) {

      str += "<div class='col-sm-12' name='td-list' style='display:none'>"
          + "<table class='table'><tbody>";

      for (var i in data) {
        var o = data[i];

        str += "<tr data-pk='" + o.pk + "'>" +
            "<td>" + o.firstname + " " + o.lastname + "</td><td>" + o.email +
            "</td><td class='text-center'>";

        if (state == "annulée") {
          if (o.presences.length !== 0) {
            str += "<label class='label label-warning'>Annulée</label>";
          } else {
            str += "<label class='label label-info'>Non présent</label>";
          }
        } else if (state == "invitée") {
          str += "<label class='label label-warning'>?</label>";
        } else if (o.presences.length !== 0) {
          nbPresence++;
          str += "<label class='label label-info'>Présent</label>";
        } else if (jeObject.isPast(current)) {
          str += "<label class='label label-info'>Non présent</label>";
        } else {
          str += "<button data-id='presence' class='btn btn-default btn-xs'>"
              + "Sera présent</button>";
        }

        str += "</td></tr>";
      }

      if (nbContact !== 0) {
        if (state == "annulée" || state == "invitée") {
          first += ((nbContact == 0) ? "Aucun" : nbContact) + " contact"
            + participations.pluralize(nbContact) + " enregistrés";
        } else {
          first += ((nbPresence == 0) ? "Aucun" : nbPresence) + " contact"
              + participations.pluralize(nbPresence)
              + " présent" + participations.pluralize(nbPresence) + " sur "
              + nbContact;
        }

        first += "</span><a class='pull-right fa fa-plus-circle show-presences'></a></div>";
        str += "</tbody></table><button name='update-presence' class='btn btn-primary pull-right'"
            +
            " style='display:none'>Modifier</button></div>";
      } else {
        first += "Aucun contact pour cette entreprise</span></div>";
      }
    } else {
      first += "Aucun contact pour cette entreprise</span></div>";
    }

    return first + str;
  }

  $("#participations-table tbody").on("click", "a.show-presences",
      function (e) {
        e.preventDefault();
        var $e = $(this).closest("td").find("[name='td-list']");

        var that = $(this);

        if ($e.hasClass("active")) {
          that.removeClass("fa-minus-circle");
          $e.removeClass("active");
          $e.hide(250);
          that.addClass("fa-plus-circle");
        }
        else {
          var $o = $("#participations-table tbody .active[name='td-list']");
          if ($o.length == 0) {
            that.removeClass("fa-plus-circle");
            $e.addClass("active");
            $e.show(250);
            that.addClass("fa-minus-circle");
          } else {
            $o.hide(250, function () {
              $o.closest("td").find("a.show-presences").removeClass(
                  "fa-minus-circle");
              $(this).removeClass("active");
              $o.closest("td").find("a.show-presences").addClass(
                  "fa-plus-circle");
            });
            $e.show(250, function () {
              that.removeClass("fa-plus-circle");
              $(this).addClass("active");
              that.addClass("fa-minus-circle");
            });
          }
        }
      });

  $("#participations-table tbody").on("click", "button[data-id='presence']",
      function (e) {
        e.preventDefault();
        var line = $(this).closest("tr");
        var pk = line.attr("data-pk");

        if (typeof pk == "undefined" || pk == "") {
          return application.notifications.notify(
              "Cette personne de contact semble mal enregistrée", "warning");
        }

        application.require("utils/popups").confirm(
            "Indiquer " + line.find("td:first").html()
            + " comme présent à la JE " + jeObject.interval(current),
            function () {
              participations.changePresence(pk, current, function (resp) {
                $("#participations-table").find("tr[data-pk='" + resp.company.pk
                    + "'] [name='td-list']").hide(500);

                var row = datatable.row("[data-pk='" + resp.company.pk + "']");
                var data = $(row.node()).data("data");

                row.remove().draw();

                var found = false;
                for (var o in data.contacts) {
                  var contact = data.contacts[o];

                  if (contact.pk == pk) {
                    data.contacts[o].presences[0] = {
                      actif: true,
                      company: null,
                      contact: null,
                      participation: null,
                      pk: -1,
                      version: 0
                    };
                    found = true;
                    break;
                  }
                }

                if (found) {
                  insertRow(data);
                }

                $("#participations-table").find("tr[data-pk='" + resp.company.pk
                    + "'] [name='td-list']").show(500, function () {
                  $(this).find("").addClass("active");
                });
              });
            });

      });

  function exportCsv(data) {
    var csvContent = "data:text/csv;charset=UTF-8,";
    data.forEach(function (infoArray, index) {
      var dataString = infoArray.join(",");
      csvContent += index < data.length ? dataString + "\n" : dataString;
    });

    var encodedUri = encodeURI(csvContent);
    var link = document.createElement("a");
    link.setAttribute("href", encodedUri);
    link.setAttribute("download", "Entreprises_invitées.csv");
    link.setAttribute("id", "downloadlink");
    link.setAttribute("target", "_blank");
    document.body.appendChild(link);

    link.click();

    $("#downloadlink").remove();
  }

  function showCompanyListForInvitation(data) {
    inviteTable.clear().draw();

    var n = false;
    var listPk = [];

    if (datatable.row().count() === 0) {
      n = true;
    }
    else {
      datatable.rows().iterator('row', function (context, index) {
        var node = $(this.row(index).node());

        listPk.push(node.attr("data-pk"));
      });
    }

    var count = 0;

    if (n == false) {
      application.require("objects/companies").list().done(function (data) {
        for (var i in data) {
          var o = data[i];
          var dateInscription = jeObject.dateFormat(o.inscriptionDate);
          var v = 0;

          // If not new invite list && already checked
          if (!n && listPk.indexOf(o.pk.toString()) !== -1) {
            v = 2;
          }

          var node = inviteTable.row.add([
            o.companyName,
            dateInscription,
            o.lastParticipationYear,
            v
          ]).draw().node();

          updateBtn(count);

          var $node = $(node);
          $node.attr('data-pk', o.pk);
          $node.attr('data-version', o.version);
          $node.attr('data-checked', (v != 0));
          $node.find("input[type='checkbox']").click(function () {
            if ($(this).is(":checked")) {
              count++;
            } else {
              count--;
            }
            updateBtn();
          });
        }
      });
    } else {
      application.require("utils/ajax").create({
        url: "/app/entreprise/listForInvite"
      }).done(function (data) {
        for (var k in data) {
          var o = data[k];
          var dateInscription = jeObject.dateFormat(o.inscriptionDate);
          var v = 0;

          count++;

          var node = inviteTable.row.add([
            o.companyName,
            dateInscription,
            o.lastParticipationYear,
            1
          ]).draw().node();

          updateBtn(count);

          var $node = $(node);
          $node.attr('data-pk', o.pk);
          $node.attr('data-version', o.version);
          $node.attr('data-checked', (v != 0));
          $node.find("input[type='checkbox']").click(function () {
            if ($(this).is(":checked")) {
              count++;
            } else {
              count--;
            }
            updateBtn();
          });
        }

      });
    }

    function updateBtn() {
      $("#popup-invitation-je").find(".confirm-buttons button").each(
          function () {
            $(this).find(".count").html(count);
            if (count == 0) {
              if (!$(this).hasClass("disabled")) {
                $(this).addClass("disabled");
              }
            } else {
              if ($(this).hasClass("disabled")) {
                $(this).removeClass("disabled");
              }
            }
          });
    }
  }

  function displayEtat(state) {
    var str = "<li class='dropdown-header'>Changer l'état</li>";

    switch (state) {
      case "invitée":
        str += "<li><a data-id='update' data-value='confirmée'>Confirmer</a></li>";
        str += "<li><a data-id='update' data-value='refusée'>Refuser</a></li>";
        break;
      case "confirmée":
        str += "<li><a data-id='update' data-value='facturée'>Facturer</a></li>";
        break;
      case "facturée":
        str += "<li><a data-id='update' data-value='payée'>Payer</a></li>";
        break;
    }

    if (state != "invitée" && state != "confirmée") {
      str += "<li><a data-id='rollback'>Etat précédent</a></li>"
    }

    if (state != "annulée" && state != "invitée") {
      if (str != "") {
        str += "<li class='divider'></li>"
      }
      str += "<li><a data-id='cancel'>Annuler</a></li>"
    }

    return str;
  }

  function updateParticipation(action, company, version, state, lastState,
      data) {
    application.require("utils/ajax").create({
      url: "/app/participation/" + action,
      data: {
        state: state,
        company: company,
        je: current,
        lastState: lastState,
        version: version
      },
      success: function (resp) {
        data.version = resp.version;
        data.state = resp.state;
        data.lastState = resp.lastState;

        updateRow(data);
        application.notifications.notify("Mise à jour de la participation",
            "success");
      }
    }, application.require("utils/ajax").errorHandler(function (xhr) {
      if (xhr.responseJSON == 409) {
        application.notifications.notify("Erreur d'accès concurrent "
            + "aux données : rafraichissement de la page", "danger");
        mainPrivate({year: current});
        return true;
      }
    }));
  }

  $("#participations-table tbody").on("click",
      "ul.participation-update [data-id]", function () {
        var action = $(this).attr("data-id");
        var tr = $(this).closest("tr");
        var row = datatable.row(tr);

        var pk = tr.attr("data-pk");
        var version = tr.data("version");
        var state = row.data()[3].state;
        var lastState = row.data()[3].lastState;
        var data = tr.data("data");

        switch (action) {
          case "update":
            updateParticipation(action, pk, version, $(this).attr("data-value"),
                state, data);
            break;
          case "rollback":
          case "cancel":
            updateParticipation(action, pk, version, state, lastState, data);
            break;
          default:
            application.notifications.notify("Action non disponible", "info");
        }
      });

  return {
    main: function (module, context) {
      mainPrivate(context);
    },
    /**
     * Vue création d'une JE (ihm side) - choix de l'année
     */
    showCreate: function () {
      logger.log("je.js", "showCreate", "");
      showCreatePrivate();
    },
    /**
     * Vue création d'une JE - choix de la date
     */
    selectDate: function (module, context) {
      logger.log("je.js", "selectDate", "");
      selectDatePrivate(
          (typeof context.year != "undefined") ? context.year : undefined);
    },
    /**
     * Execute la création d'une JE
     */
    create: function () {
      logger.log("je.js", "create", "");
      createPrivate();
    },
    /**
     * First invitations entreprises.
     */
    invite: function (module, context, $elem) {
      logger.log("je.js", "invite", "");
      if ($elem.hasClass("disabled")) {
        return;
      }
      sendInvites((typeof context.dl != "undefined") ? context.dl : false);
    },
    /**
     * Load the possible invitation
     */
    inviteList: function (module, context) {
      application.require("utils/popups").load("invitations",
          {"keep-form": true}, function () {
            application.require("utils/ajax").create({
              url: "/app/entreprise/listForInvite",
              method: "POST",
              dataType: "JSON",
              success: function (data) {
                showCompanyListForInvitation(data);
              },
              error: function (err) {
                application.require("utils/popups").unload("invitations");
                alerteForm("Je", errors.translate(err.responseJSON));
              }
            });
          });
    },
    exportAllInvited: exportAllInvited
  };
}());