const fs = require("fs");

config.devServer = config.devServer || {};

config.devServer.server = {
  type: "https",
  options: {
    key: fs.readFileSync("C:/Users/PC/Downloads/admin-panel/certs/192.168.1.32+2-key.pem"),
    cert: fs.readFileSync("C:/Users/PC/Downloads/admin-panel/certs/192.168.1.32+2.pem"),
  }
};

config.devServer.host = "0.0.0.0";
config.devServer.port = 3001;
config.devServer.historyApiFallback = true;