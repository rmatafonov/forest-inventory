import logging

from flask import jsonify
from flask_api import status
from flask_restful import Resource, reqparse, abort

from lib.repository.inventory_repository import InventoryRepository
from lib.service.processor import Processor

parser = reqparse.RequestParser()
parser.add_argument("text")


class DataAnalysisApi(Resource):
    __repository = InventoryRepository()

    def post(self):
        args = parser.parse_args()
        if args["text"] is None:
            abort(http_status_code=status.HTTP_400_BAD_REQUEST, message="text have to be specified")

        Processor.process(args["text"])
