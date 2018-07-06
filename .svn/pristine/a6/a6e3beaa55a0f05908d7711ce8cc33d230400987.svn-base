# JS front-end management

## créer un nouveau composant

Pour créer un nouveau composant, se rendre dans le dossier JS
Pour une composant , créer un fichier nommé {$ID}.js
Ex : module test => web/js/test.js

Ensuite, définir le module :

~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
application.define("{$ID}", function() {
  // Si du contenu JS est indiqué ici, il est appelé au chargement du module
  console.log("démarrage composant");


  // Privé
  var attributPrive = 42;
  function methodePrivee () {}

  // Public
  return {
    attributPublic : 42,
    methodePublique : function() { // Do something here }
  };
} ());
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~


## utiliser le nouveau composant

Plusieures manières :

application.require("composant");
=> execute uniquement le constructeur et pré-charge le module

application.require("composant", "methode", context = {})
=> appelle une méthode publique

application.require("composant", function(leComposant, context = {}) {
  leComposant.doSomething();
})
=> utilise un callback, le context du composant (module) est utilisable via le 1er argument
et le context général (arguments manuels) est disponible via le 2e argument

ex signature 

~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Utilisation
require(foo, callback(), { year : 2017 })


// Declaration
function callback(module, context) {
  module <= le module foo est disponible
  module.doSomethingPublic();
  
  context <= l'objet passé en context
  context.year
}
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~


## Appeler le composant vie HTMl

sur un lien ou formulaire :
data-view="composant:methode" 
=> execute une methode publique d'un module

=> ajouter un context :
data-context = "{ qqchose : 42 }"

sur un lien 
data-template="template" (fichier.html à charger)
data-template-holder="somewhere" (ou injecter le html)


# JS Form component

Validation :
data-required = true | false

data-pattern = "regex"
data-pattern-message = "Message d'erreur"

data-min=""
data-max=""