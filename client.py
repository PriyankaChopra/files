import requests, json

url = 'http://10.76.110.110:8000/scp/uploadPackage'
data = {
  "host_ip" : "10.76.110.121", 
  "username": "abhishek" ,
  "password": "password",
  "source_path": "/home/invlab09/vnf_package.tar",
  "destination_path": "/tmp"
   }
response = requests.post(url, data=json.dumps(data))
print response
print response.content
