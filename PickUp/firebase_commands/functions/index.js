const functions = require('firebase-functions');
const admin = require('firebase-admin');
admin.initializeApp(functions.config().firebase);


exports.sendNewPostNotification = functions.database.ref('/notifications/{notifId}').onWrite((event) => {

    const notifId = event.params.notifId;
    console.log('new notification. id: ', notifId);
    
    const userId = admin.database().ref(`/notifications/${notifId}/creator/notificationToken`).once('value');

    const zipList = admin.database().ref(`/notifications/${notifId}/zipCodes`).once('value');

    return Promise.all([userId, zipList]).then((results) => {
        const the_userId = results[0]['node_']['value_'];
        const the_zipList = results[1];

        console.log(typeof(the_userId), " the_userId " );
        console.log(" value ", the_userId);

        console.log(typeof(the_zipList), " the_zipList ");
        console.log(" value ", the_zipList);

        const allZips = [];
        the_zipList.forEach(function(child) {
            var chKey = child.key;
            console.log(chKey, " key");
            var chData = child.val();
            console.log(chData, " child data");
            allZips.push(chData);
        });

        const payload = {
            notification: {
                title: 'you have a new poster!',
                body: 'new post yo'
            },
        };

        return admin.messaging().sendToDevice(the_userId, payload);
    }).then((response) => {
        const tokensToRemove = [];
        response.results.forEach((result, index) => {
            const error = result.error;
            if(error) { 
                console.error('fail send notif');
                if(error.code === 'messaging/invalid-registration-token' || error.code === 'messaging/registration-token-not-registered') {
                    tokensToRemove.push(tokensSnapshot.ref.child(tokens[index]).remove());
                }
    
            }
        });
    return Promise.all(tokensToRemove);
    });
});


// Sends notifications to all users in the {zipCode} group
// Uses the firebase notificationToken(s) to get the notifications out to the devices.
exports.sendToGroups = functions.database.ref('/{zipCode}/notificationIDs').onWrite((event) => {

    const zipCode = event.params.zipCode;
    console.log('new zipCode id: ', zipCode);
    
    const tokens = admin.database().ref(`/${zipCode}/notificationTokens`).once('value');

    return Promise.all([tokens]).then((results) => {
        const tokens = results[0];

        console.log(typeof(tokens), " tokens type " );
        console.log(" tokens value ", tokens);

        const allTokens = [];
        tokens.forEach(function(child) {
            var childKey = child.key;
            console.log(childKey, " key");
            var childData = child.val();
            console.log(childData, " child data");
            allTokens.push(childData);
        });

        console.log("allTokens: ", allTokens);
    
        const payload = {
            notification: {
                title: 'New Post!',
                body: 'There is a new post in your area.'
            },
        };

        return admin.messaging().sendToDevice(allTokens, payload);
    }).then((response) => {
        response.results.forEach((result, index) => {
            const error = result.error;
            if(error) { 
                console.error('fail send notif');
                if(error.code === 'messaging/invalid-registration-token' || error.code === 'messaging/registration-token-not-registered') {
                    console.log("ERROR: MESSAGING FAILED");
                }
    
            }
        });
    return Promise.all(tokensToRemove);
    });
});

// // Create and Deploy Your First Cloud Functions
