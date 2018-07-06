application.define("utils/date-picker", function () {
  var loaded = false;

  return {
    init: function (e, callback) {
      $(e).fadeOut();

      if (loaded == true) {
        var $piker = $(e).pickadate();

        callback($piker.pickadate('picker'));
      } else {
        $.when(
            $.ajax({
              url: "/lib/pickadate.js/lib/compressed/picker.js",
              dataType: "script"
            }),
            $.ajax({
              url: "/lib/pickadate.js/lib/compressed/picker.date.js",
              dataType: "script"
            })
        ).done(function () {
          $.getScript("/lib/pickadate.js/lib/translations/fr_FR.js");
          setTimeout(function () {
            logger.log("date-picker.js", "init()", "Done loading");
            loaded = true;
            var $piker = $(e).pickadate();
            callback($piker.pickadate('picker'));
          }, 500);
        });
      }
    }
  };
}());