/*-----------------------------------------------------------------------------
Ewha Womans University
Computer Science & Engineering
1315060 SoYeon Jeon
-----------------------------------------------------------------------------*/

/*
|--------------------
|LUIS Connection
|--------------------
*/
"use strict";
const LUISClient = require("./luis_sdk");

const APPID = "34a13c79-9c5c-460d-88aa-f6fd394a8ed9";
const APPKEY = "7ef811c1ff214509988977a533b4229b";

var LUISclient = LUISClient({
  appId: APPID,
  appKey: APPKEY,
  verbose: true
});

LUISclient.predict("Enter the text to predict", {
  //On success of prediction
  onSuccess: function (response) {
    printOnSuccess(response);
  },
  //On failure of prediction
  onFailure: function (err) {
    console.error(err);
  }
});

var printOnSuccess = function (response) {
  console.log("Query: " + response.query);
  console.log("Top Intent: " + response.topScoringIntent.intent);
  console.log("Entities:");
  for (var i = 1; i <= response.entities.length; i++) {
    console.log(i + "- " + response.entities[i-1].entity);
  }
  if (typeof response.dialog !== "undefined" && response.dialog !== null) {
    console.log("Dialog Status: " + response.dialog.status);
    if(!response.dialog.isFinished()) {
      console.log("Dialog Parameter Name: " + response.dialog.parameterName);
      console.log("Dialog Prompt: " + response.dialog.prompt);
    }
  }
};

/*
|--------------------
|App Connection
|--------------------
*/
// Add your requirements
var restify = require('restify'); 
var builder = require('botbuilder'); 
// Setup Restify Server
var server = restify.createServer();

server.listen(process.env.PORT || 3000, function() 
{
   console.log('%s listening to %s', server.name, server.url); 
});

// Create chat bot
var connector = new builder.ChatConnector
({ appId: '29c4961f-2fcc-454e-b9d4-ca0f6a1fe4c5', appPassword: 'W0f2i2hqHSbwoLLMKdvq9Fg' }); 
var bot = new builder.UniversalBot(connector);
server.post('/api/messages', connector.listen());

/*
|--------------------
|DataBase Connection
|--------------------
*/
var mysql = require('mysql2');
var conn = mysql.createConnection
({host: "wjsthdus1.mysql.database.azure.com", user: "wjsthdus@wjsthdus1", password: "Root1234", database: "sample", port: 3306, ssl: true});
// Simple query
conn.query(
	'SELECT * FROM `sample`.`company` WHERE CompanyId = 1',
	function(err, results, fields) {
    	console.log(results); // results contains rows returned by server
    	//console.log(fields); // fields contains extra meta data about results, if available
  	}
);

// Add a global LUIS recognizer to your bot using the endpoint URL of your LUIS app
var model = 'https://southeastasia.api.cognitive.microsoft.com/luis/v2.0/apps/34a13c79-9c5c-460d-88aa-f6fd394a8ed9?subscription-key=d094c1a9b3d04ccfaf6bf42a03fe9dd7';
var recognize = new builder.LuisRecognizer(model);
bot.recognizer(recognize);
var intents = new builder.IntentDialog({ recognizers: [recognize] })

.matches('greeting', (session, args) => {
    session.send('Hi! This is a whosgood bot!');
    session.send('You can ask me something like "What is ESG?"');
})
.matches('info_esg',(session, args)=>{
    session.send('ESG is a set of environmental data, social data and governance data.');
    session.send('You can ask me more question.'+'For example, what is the best company to invest in?');
})
.matches('info_company',(session, args)=>{
    session.send('You can visit Who\'s good website. https://www.whosgood.org/');
    session.send('You can ask me more question.'+'For example, what is the best company to invest in?');
})
.matches('showme',(session, args)=> {
    session.send(JSON.stringify(args));
    conn.query(
		'SELECT CompanyEngName FROM `sample`.`company` WHERE CompanyId = 1',
		function(err, results, fields) {
			
			if (err) {
				console.log(err);
        		session.send('error connection');
    		}else{
    			session.send('please wait a second');
    			console.log(results);
    			session.send(results[0].CompanyEngName);
    			session.send('This is a result. '+'You can ask me more question.');
    		}
    	}
	);
})
.matches('q_CompanyScore',(session, args)=>{
	//session.send(JSON.stringify(args));
    conn.query(
		'SELECT CompanyEngName, CompanyScore FROM `sample`.`company` c, `sample`.`companies_scores` cs WHERE c.CompanyId = cs.CompanyID ORDER BY cs.CompanyScore DESC LIMIT 5',
		function(err, results, fields) {
			if (err) {
				console.log(err);
        		session.send('error connection');
    		}else{
    			session.send('please wait a second');
    			console.log(results);
    			session.send(results[0].CompanyEngName+', '+results[1].CompanyEngName+', '+results[2].CompanyEngName+', '+results[3].CompanyEngName+', '+results[4].CompanyEngName);
    			session.send('This is a result. '+'You can ask me more question.');
    		}
    	}
	);
})
.onDefault((session) => {
    session.send('Sorry, I did not understand \'%s\'. If you want more data, write words that start with "showme".', session.message.text);
});

bot.dialog('/', intents);
