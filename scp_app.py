from flask import Flask, redirect, url_for, jsonify, request, abort
import yaml, json, datetime, os
app = Flask(__name__)
import yaml
import os
import zipfile
import logging
import ssh
import sys

#logname = sys.argv[1]
logname = "abc.log"
logger = logging.getLogger()
handler = logging.StreamHandler()
formatter = logging.Formatter(
    '%(asctime)s %(name)-12s %(levelname)-8s %(message)s')
#logging.basicConfig(filename=logname,
#                            filemode='a',
#                            format='%(asctime)s,%(msecs)d %(name)s %(levelname)s %(message)s',
#                            datefmt='%H:%M:%S',
#                            level=logging.DEBUG)
handler.setFormatter(formatter)
logger.addHandler(handler)
logger.setLevel(logging.DEBUG)


@app.route('/scp/uploadPackage', methods=['POST'])
def parse_heat_template():  # templateFileList):
    try:
        input_json = request.get_json(force=True)
        #input_json  = json.loads(data)
        logger.debug(input_json)
        sourcePackagePath = input_json['source_path']
        destinationPackagePath = input_json['destination_path']
        host_ip = input_json['host_ip']
        username = input_json['username']
        password = input_json['password']
        
        logger.debug( "Source Path :-> {0}".format(sourcePackagePath))
        logger.debug("Destination Path :-> {0}".format( destinationPackagePath))
        logger.debug( "username : -> {0}".format( username))
        logger.debug ("password :-> {0}".format( password))
        command = "scp {0} {1}@{2}:{3}".format(sourcePackagePath , username, host_ip, destinationPackagePath )
        logger.debug( "Commmand --> {0}".format( command))
        #command = scpToRemoteServer('password', 'scp a.txt abhishek@10.76.110.122:/tmp/'
        scp_status = ssh.scpToRemoteServer(password, command)
        logger.debug ( "Scp Status :---> {0} ".format( scp_status))
        if scp_status == True:
            response = {'status': "ok"}
            output = jsonify (response)
        # output = jsonify(temp_list)
            return output
            
        else:
            response = {'status' : "fail"}
            #response.status_code = 403
            output = jsonify (response)
            output.status_code = 403
            return output
            #abort(403)


    except Exception as e:
        logger.exception(" Exception caught in {0}".format(e) )
        import traceback; traceback.print_exc()

@app.route('/tests/endpoint', methods=['POST'])
def my_test_endpoint():
    input_json = request.get_json(force=True)
    # force=True, above, is necessary if another developer
    # forgot to set the MIME type to 'application/json'
    print 'data from client:', input_json
    dictToReturn = {'answer':42}
    return jsonify(dictToReturn)



if __name__=='__main__':
    app.run('0.0.0.0',8000, debug = True)
