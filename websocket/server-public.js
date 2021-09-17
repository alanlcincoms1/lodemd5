let express = require('express');
let app = express();

const portSocket = process.env.PORT || 4678;
const portApi = process.env.PORT_API || 4679;
let bodyParser = require('body-parser');
let CLIENTS = [];
const WebSocket = require('ws');
let WebSocketServer = WebSocket.Server,
wss = new WebSocketServer({port: portSocket});

app.use(bodyParser.json());
app.use(bodyParser.urlencoded({
    extended: true
}));

app.get('/server', function (req, res) {
    res.send("Total User: " + wss.clients.size);
});

app.post('/broadcast', function (req, res) {
    let data = {};
    data[req.body.event] = req.body.data;
    wss.clients.forEach(function each(client) {
        if (client.readyState === WebSocket.OPEN) {
            client.send(JSON.stringify(data));
        }
    });

    res.send('OK')
});

app.post('/send_to_client', function (req, res) {
    if (CLIENTS[req.body.member_id]) CLIENTS[req.body.member_id].send(JSON.stringify(req.body.data));
    res.send('OK')
});

wss.on('connection', function (ws) {
    ws.on('message', function (data) {
        if (JSON.parse(data)['connect']) {
            ws.id = member_id;

        }

        if (JSON.parse(data)['disconnect']) {
            let member_id = JSON.parse(data)['disconnect'];

        }

    });

    ws.on('error', () => console.log('errored'));
    ws.on('close', function close() {
        console.log('close '+ ws.id);
    });
});

app.listen(portApi, function () {
    console.log('Example app listening on port !'+ portApi + ' '+ portSocket)
});
