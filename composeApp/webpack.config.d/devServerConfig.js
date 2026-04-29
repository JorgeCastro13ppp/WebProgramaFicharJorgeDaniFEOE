const fs = require("fs");

config.devServer = config.devServer || {};

config.devServer.server = {
  type: "https",
  options: {
    key: fs.readFileSync("C:/Users/PC/Downloads/admin-panel/certs/192.168.1.45+1-key.pem"),
    cert: fs.readFileSync("C:/Users/PC/Downloads/admin-panel/certs/192.168.1.45+1.pem"),
  }
};

config.devServer.host = "0.0.0.0";
config.devServer.port = 3000;
config.devServer.historyApiFallback = true;