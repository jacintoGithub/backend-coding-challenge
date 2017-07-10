angular.module("app.services", [])
    .service("moneyService", function ($http) {
        function vat() {
            return 0.2;
        };
        this.vat = vat;
        this.eurToGbp = function (euros, callback) {
            return $http.get("http://api.fixer.io/latest")
                .then(function(response) {
                    callback(Math.round(euros * response.data.rates.GBP * 100) / 100);
                }, function(response) {
                    if(response)
                        alert(response.statusText + ":\n" + response.config.url);
              });
        };
        this.includeVat = function (pounds) {
            if(pounds)
                return Math.round((1 + vat()) * pounds * 100) / 100;
        };
        this.onlyVat = function (pounds) {
            if(pounds)
                return Math.round(vat() * pounds * 100) / 100;
        };
});