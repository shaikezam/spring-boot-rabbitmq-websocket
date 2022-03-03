var ws;

function connect() {
    ws = new WebSocket("ws://localhost:8080/server");

    ws.onmessage = function(event) {
        var log = document.getElementById("log");
        console.log(event.data);
        log.innerHTML += event.data + "\n";
    };
}

function disconnect() {
    ws.close();
}

function send() {
    var content = document.getElementById("msg").value;

    ws.send(content);
}