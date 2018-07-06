application.define("search", function () {
  var jes = application.require("objects/jes");

  var timeout = null;
  var ajax = null;

  var hidable = true;

  var search = [];
  var searchResults = {};

  function show() {
    $(".sidebar-search .autocomplete").show();
  }

  function hide() {
    if (hidable) {
      $(".sidebar-search .autocomplete").hide();
    }
  }

  function launchSearch(val) {
    if (val.length < 2) {
      if (timeout != null) {
        clearTimeout(timeout);
        timeout = null;
      }
      if (ajax != null) {
        ajax.abort();
        ajax = null;
      }

      return;
    }
    show();
    if (timeout != null) {
      clearTimeout(timeout);
    }
    timeout = setTimeout(function () {
      if (ajax != null) {
        ajax.abort();
        ajax = null;
      }
      simpleSearch(val);
    }, 500);
  }

  $(".sidebar-search input[name='app-search']:first").each(function () {
    var elem = $(this);

    $(this).val("");
    elem.data('oldVal', "");

    // Init changes watcher
    elem.bind("propertychange change click keyup input paste focus",
        function (event) {
          if ((event.type == "click" || event.type == "focus") && elem.val()
              != "") {
            hidable = false;
            setTimeout(function () {
              hidable = true;
            }, 50);
            show();
            return;
          }
          // On change
          if (elem.data('oldVal') != elem.val()) {
            elem.data('oldVal', elem.val());

            if (elem.val() == "") {
              hide();
            } else {
              launchSearch(elem.val());
            }
          }
        });

    $(document).on("click", hide);
  });

  function simpleSearch(val) {
    $(".autocomplete .result-set").each(function () {
      $(this).remove()
    });
    ajax = $.ajax({
      "url": "/app/search",
      "method": "POST",
      "dataType": "JSON",
      "data": {search: val},
      "success": function (data) {
        searchResults = data;

        if (-1 != val.match(/^(\d|\/|.)+$/)) {
          var exp = val.replace(/\./, "/");
          var list = jes.all().filter(function (e) {
            return (jes.dateFormat(e.dayDate)).match(new RegExp(exp)) != null;
          });

          if (list.length == 0) {
            $(".autocomplete li.divider:first").before("<li class='result-set'>"
                + "<a>Aucun résultat</a></li>");
          } else {
            for (var k in list) {
              // Max 5 : refine
              if (k == 5) {
                break;
              }
              $(".autocomplete li.divider:first").before(
                  "<li class='result-set'><a>JE "
                  + highlight(jes.dateFormat(list[k].dayDate), exp)
                  + "</a></li>"
              );
            }
          }
        }

        search = val.split("\w+");
        searchResults = data;

        var companies = application.require("objects/companies");

        if (data.companies.length != 0) {
          var i;
          for (i = 0; (i < 3 && i < data.companies.length); i++) {
            companies.set(data.companies[i]);
            $(".autocomplete .divider:last").before(
                "<li class='result-set'>"
                + "<a href='#' data-view='entreprise:display' data-context='{ \"pk\" : "
                + data.companies[i].pk + " }' >"
                + highlight(data.companies[i].companyName)
                + "</a>"
                + "</li>"
            );
          }

          for (i; i < data.companies.length; i++) {
            companies.set(data.companies[i]);
          }

          if (data.companies.length > 3) {
            $(".autocomplete .divider:last").before(
                "<li style='text-align: center' class='result-set'>"
                + "<a data-view='search:fullSearch' data-context='{ \"view\" : "
                + "\"companies\"" + " }' >"
                + "Afficher plus (" + data.companies.length + ")"
                + "</a>"
                + "</li>"
            );
          }
        } else {
          $(".autocomplete li.divider:last").before("<li class='result-set'>"
              + "<a>No result found</a></li>");
        }

        var contacts = application.require("objects/contacts");

        if (data.contacts.length != 0) {
          i = 0;
          for (; (i < 3 && data.contacts.length); i++) {
            contacts.set(data.contacts[i]);
            $(".autocomplete").append(
                "<li class='result-set'>"
                + "<a href='#' data-view='contacts:display' data-context='{ \"pk\" : "
                + data.contacts[i].pk + " }' >"
                + highlight(data.contacts[i].firstname + " "
                    + data.contacts[i].lastname)
                + "</a>"
                + "</li>"
            );
          }

          for (i; i < data.contacts.length; i++) {
            contacts.set(data.contacts[i]);
          }

          if (data.contacts.length > 3) {
            $(".autocomplete").append(
                "<li style='text-align: center' class='result-set'>"
                + "<a href='#' data-view='search:fullSearch' data-context='{ \"view\" : "
                + "\"contacts\"" + " }' >"
                + "Afficher plus (" + data.contacts.length + ")"
                + "</a></li>"
            );
          }
        } else {
          $(".autocomplete").append("<li class='result-set'>"
              + "<a>No result found</a></li>");
        }
      }
    });
  }

  function displayContactHeader() {
    return "<th>Nom & prénom</th><th>Entreprise</th>"
        + "<th>Email</th><th>Telephone</th>";
  }

  function displayCompanyHeader() {
    return "<th>Entreprise</th><th>Adresse</th>";
  }

  $(".inner-template[data-template='search']").on("click", '.search-result',
      function () {
        var record = $(this).closest("tr");

        switch (record.attr("data-type")) {
          case "company":
            application.require("entreprise").main();
            application.require("entreprise").display(null,
                {pk: record.attr("data-pk")});
            break;
          case "contact":
            application.require("contacts").main();
            application.require("contacts").display(null,
                {pk: record.attr("data-pk")});
            break;
          default:
            application.require("dashboard").main();
            application.notifications.notify(
                "Ce résultat de recherche semble mal formatté");
        }
      });

  function highlight(str, exp) {
    if (typeof exp != "undefined") {
      str = str.replace(exp,
          "<span style='font-weight : bold'>" + exp + "</span>");
    } else {
      for (var s in search) {
        str = str.replace(s,
            "<span style='font-weight : bold'>" + s + "</span>");
      }
    }

    return str;
  }

  function displayContact(data) {
    return "<tr data-pk='" + data.pk + "' data-type='contact'>"
        + "<td><a href='#' class='search-result'>" + highlight(
            data.firstname + " " + data.lastname) + "</a></td>"
        + "<td>" + data.company.companyName + "</td>"
        + "<td>" + data.email + "</td>"
        + "<td>" + data.phone + "</td>"
        + "</tr>";
  }

  function displayCompany(data) {
    return "<tr data-pk='" + data.pk + "' data-type='company'>"
        + "<td><a class='search-result' href='#'>" + highlight(data.companyName)
        + "</a></td>"
        + "<td>" + formatAdress(data.address) + "</td>"
        + "</tr>";
  }

  function formatAdress(address) {
    var boite = "";
    if (address.box !== 0) {
      boite = "boite " + address.box;
    }

    return highlight(address.street) + ", " + address.streetNumber + " "
        + boite + ", " + address.zipCode + ", " + highlight(address.commune);
  }

  return {
    fullSearch: function (module, context) {
      application.require("template").loadView("search", null, function () {
        $(".inner-template[data-template='search'] [data-attribute='search']").html(
            search.join(" "));
        $(".inner-template[data-template='search'] [data-attribute='view']").html(
            (context.view == "companies") ? "société" : "personnes de contact");
        $(".inner-template[data-template='search'] [data-attribute='count']").html(
            searchResults[context.view].length);
        $(".inner-template[data-template='search'] table thead tr:first").html(
            (context.view == "companies") ? displayCompanyHeader()
                : displayContactHeader()
        );

        var body = $(".inner-template[data-template='search'] table tbody");
        body.html("");

        for (var k in searchResults[context.view]) {
          var o = searchResults[context.view][k];
          body.append((context.view == "companies") ? displayCompany(o)
              : displayContact(o))
        }
      })
    }
  };
}());