#!/usr/bin/python
import sys
import requests
import unittest
import configparser
import subprocess
import time
from random import seed
from random import randint


class TestStringMethods(unittest.TestCase):

    # set fields packageDescription labelerName genericName returned by the query to drugStore
    def test_1_create(self):
        config = configparser.ConfigParser()
        config.read("test.ini")
        url = config.get('baseUrl', 'url')
        pload = '{"productNdc":"69618-010","price":561121,"existences":20,"status":"ACTIVE"}'
        r = requests.post(url + "/store/drug/create", data=pload)
        print(r.text)
        self.assertEqual(r.status_code, 200, 'the service create should respond with 200 success code')
        response = r.json()
        self.assertIsNotNone(response['productNdc'], 'there should be the identifier')
        self.assertIsNotNone(response['genericName'], 'there should be the generic name field')
        self.assertIsNotNone(response['packageDescription'], 'there should be the package description')
        self.assertIsNotNone(response['labelerName'], 'there should be the labelerName')

    # set parameters from dto and update the entity
    def test_2_update(self):
        config = configparser.ConfigParser()
        config.read("test.ini")
        url = config.get('baseUrl', 'url')
        pload = '{"productNdc":"69618-010","price":512,"existences":10,"status":"ACTIVE"}'
        r = requests.put(url + "/store/drug/update", data=pload)
        print(r.text)
        self.assertEqual(r.status_code, 200, 'the service update should respond with 200 success code')
        r = requests.get(url + "/store/drug/69618-010")
        print(r.text)
        self.assertEqual(r.status_code, 200, 'the service get drug should respond with 200 success code')
        response = r.json()
        self.assertEqual(response[0]['price'], 512, 'the price must be updated')
        self.assertEqual(response[0]['existences'], 10, 'the existence must be updated')

    # Update the drug with INACTIVE status
    def test_3_disable(self):
        config = configparser.ConfigParser()
        config.read("test.ini")
        url = config.get('baseUrl', 'url')
        r = requests.delete(url + "/store/drug/69618-010")
        print(r.text)
        self.assertEqual(r.status_code, 200, 'the service disable should respond with 200 success code')
        r = requests.get(url + "/store/drug/69618-010")
        print(r.text)
        self.assertEqual(r.status_code, 200, 'the service get drug should respond with 200 success code')
        response = r.json()
        self.assertEqual(response[0]['status'], 'INACTIVE', 'the status should be updated to INACTIVE')

    def test_4_uploadfile(self):
        config = configparser.ConfigParser()
        config.read("test.ini")
        url = config.get('baseUrl', 'url')
        seed(1)
        filename = randint(100000, 900000)
        print("file to be uploaded", str(filename) + ".pdf")
        with open('pdf/example.pdf', 'rb') as f:
            data = f.read()
            files = {'upload_file': (str(filename) + ".pdf", data)}
            r = requests.post(url + "/store/drug/uploadPdf", files=files)
            print(r.text)
            self.assertEqual(r.status_code, 200, 'the service upload file should respond with 200 success code')

    # Use a Database for persistent storage
    #@unittest.skip
    def test_5_persistent_database(self):
        self.assertEqual(1, 1)
        config = configparser.ConfigParser()
        config.read("test.ini")
        # restart the application
        appType = config.get('openshift','app-type')

        process = subprocess.run(['oc', 'login', config.get('openshift','url'), '--token', config.get('openshift', 'account_token')],
                                   universal_newlines=True, stdout=subprocess.PIPE, stderr=subprocess.PIPE)
        print(process.stdout)
        time.sleep(30)
        process = subprocess.run(['oc', 'scale', '--replicas=0', appType + '/' + config.get('openshift','deployment')],
                                   universal_newlines=True, stdout=subprocess.PIPE, stderr=subprocess.PIPE)
        print(process.stdout)
        process = subprocess.run(['oc', 'scale', '--replicas=1', appType + '/' + config.get('openshift','deployment')],
                                   universal_newlines=True, stdout=subprocess.PIPE, stderr=subprocess.PIPE)
        print(process.stdout)

        #oc get deployment camel-workshop -o jsonpath={.status.readyReplicas}
        
        readyReplicas = 0
        while readyReplicas == 0 or readyReplicas == '' or readyReplicas is None:
            time.sleep(15)
            process = subprocess.run(['oc', 'get', appType + '/' + config.get('openshift','deployment'),'-o', 'jsonpath={.status.readyReplicas}'],
                                       universal_newlines=True, stdout=subprocess.PIPE, stderr=subprocess.PIPE)
            print('replicas:',process.stdout)
            readyReplicas = process.stdout
            
        url = config.get('baseUrl', 'url')
        r = requests.get(url + "/store/drug/69618-010")
        print(r.text)
        self.assertEqual(r.status_code, 200, 'the service get drug should respond with 200 success code')
        response = r.json()
        self.assertTrue(response, "the response must not be empty")
        self.assertEqual(response[0]['productNdc'], '69618-010', 'the identifier should be the same from request')
        self.assertIsNotNone(response[0]['genericName'], 'there should be the generic name field')
        self.assertIsNotNone(response[0]['packageDescription'], 'there should be the package description')
        self.assertIsNotNone(response[0]['labelerName'], 'there should be the labelerName')

    # use pvc to store
    def test_6_downloadFile(self):
        config = configparser.ConfigParser()
        config.read("test.ini")
        url = config.get('baseUrl', 'url')
        seed(1)
        filename = randint(100000, 900000)
        print("file to be downloaded", str(filename) + ".pdf")
        response = requests.get(url + "/store/drug/getPdf/"+ str(filename) + ".pdf")
        self.assertEqual(response.status_code, 200, 'the service download pdf should respond with 200 success code')
        self.assertTrue(response, "the response must not be empty")


if __name__ == '__main__':
    unittest.main()
