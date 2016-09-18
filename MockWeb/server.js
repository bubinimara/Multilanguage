var http = require('http');
var dispatcher = require('httpdispatcher');
var fs = require('fs');

//var languageJson = require('./response/language.json');
var languagesJson = require('./response/languages.json');
var categoriesJson = require('./response/categories.json');
var productsJson = require('./response/products.json');




//Lets define a port we want to listen to
const PORT=8080; 

//We need a function which handles requests and send response
function handleRequest(request, response){
   try {
        //log the request on console
        console.log(request.url);
	// set headers as json
	response.setHeader('Content-Type', 'application/json');
        //Disptach
        dispatcher.dispatch(request, response);
    } catch(err) {
        console.log(err);
    }
}


//Create a server
var server = http.createServer(handleRequest);

//Lets start our server
server.listen(PORT, function(){
    //Callback triggered when server is successfully listening. Hurray!
    console.log("Server listening on: http://localhost:%s", PORT);
});


/**
 * Get all languages of the client
 */
dispatcher.onGet("/api/v1/languages", function(req, res) {
        res.end(JSON.stringify(languagesJson));
        });

/**
 * Get all categories wiht all translations
 */
dispatcher.onGet("/api/v1/categories", function(req, res) {
        res.end(JSON.stringify(categoriesJson));
        });


/**
 * Get all products wiht all translations
 */
dispatcher.onGet("/api/v1/products", function(req, res) {
        res.end(JSON.stringify(productsJson));
        });
