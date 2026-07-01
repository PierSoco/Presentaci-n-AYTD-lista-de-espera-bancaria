const express = require('express');
const http = require('http');
const { WebSocketServer } = require('ws');
const path = require('path');
const os = require('os');

const app = express();
const server = http.createServer(app);
const wss = new WebSocketServer({ server });

let displayWs = null;
const controllers = new Set();

wss.on('connection', (ws) => {
  ws.on('message', (raw) => {
    let msg;
    try {
      msg = JSON.parse(raw);
    } catch {
      return;
    }

    if (msg.type === 'display') {
      displayWs = ws;
      ws.on('close', () => { if (displayWs === ws) displayWs = null; });
      return;
    }

    if (msg.type === 'controller') {
      controllers.add(ws);
      ws.on('close', () => controllers.delete(ws));
      return;
    }

    if (controllers.has(ws) && displayWs && displayWs.readyState === 1) {
      displayWs.send(raw.toString());
    } else if (ws === displayWs) {
      const str = raw.toString();
      for (const ctrl of controllers) {
        if (ctrl.readyState === 1) ctrl.send(str);
      }
    }
  });
});

app.get('/', (_req, res) => {
  res.sendFile(path.join(__dirname, 'presentacion.html'));
});

app.use(express.static(__dirname));

app.get('/remote', (_req, res) => {
  res.sendFile(path.join(__dirname, 'remote.html'));
});

function getAllIPs() {
  const ips = [];
  const nets = os.networkInterfaces();
  for (const name of Object.keys(nets)) {
    for (const net of nets[name]) {
      if (net.family === 'IPv4' && !net.internal) {
        ips.push({ name, address: net.address });
      }
    }
  }
  return ips;
}

const PORT = process.env.PORT || 3000;
server.listen(PORT, () => {
  const ips = getAllIPs();
  console.log('');
  console.log('============================================');
  console.log('  PRESENTACION CON CONTROL REMOTO');
  console.log('============================================');
  console.log('');
  console.log('  PC (abri en el navegador de la compu):');
  console.log('    http://localhost:' + PORT);
  console.log('');
  if (ips.length === 0) {
    console.log('  CELULAR: No se pudo detectar la IP automaticamente.');
    console.log('  Buscala manualmente con: ipconfig');
  } else {
    console.log('  CELULAR (probá cada una hasta que funcione):');
    ips.forEach(function(iface) {
      console.log('    http://' + iface.address + ':' + PORT + '/remote  (' + iface.name + ')');
    });
  }
  console.log('');
  console.log('  Si no funciona, desactivá el firewall o agregá una regla:');
  console.log('    netsh advfirewall firewall add rule name="Node3000" dir=in action=allow protocol=TCP localport=' + PORT);
  console.log('');
  console.log('============================================');
  console.log('');
});
