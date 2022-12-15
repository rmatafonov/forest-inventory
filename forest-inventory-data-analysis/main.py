import logging

from flask import Flask
from flask_cors import CORS
from flask_restful import Api

from lib.api.data_analysis_api import DataAnalysisApi
from lib.config import config
from lib.config.logging_config import LoggingConfig

LoggingConfig.init(logging.DEBUG)

app = Flask(__name__)
CORS(app)
app.config["JSON_AS_ASCII"] = False

api = Api(app)
api_base = config["API_BASE"]
api.add_resource(DataAnalysisApi, f"{api_base}/recognized-text")

logging.info(config["POSTGRES_URI"])

if __name__ == "__main__":
    from waitress import serve
    serve(app, host="0.0.0.0", port=8081)
