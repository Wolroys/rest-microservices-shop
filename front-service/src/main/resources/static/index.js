(function () {
    angular
        .module('market', ['ngRoute', 'ngStorage'])
        .config(config);

    function config($routeProvider){
        $routeProvider
            .when('/', {
                templateUrl: 'shop/shop.html',
                controller: 'mainController'
            })
            .when('/login', {
                templateUrl: 'auth/login.html',
                controller: 'loginController'
            })
            .when('/register', {
                templateUrl: 'auth/register.html',
                controller: 'registerController'
            })
            .otherwise({
                redirectTo: '/'
            });
    }

})();
