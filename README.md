# files

Fetch Jenkins API
==================
curl --user admin:f78598af09db161f9c5eea94774f2553 http://jenkins-orch.vici.verizon.com:8080/crumbIssuer/api/xml?xpath=concat\(//crumbRequestField,%22:%22,//crumb\)
•	Use the response as a header type in the subsequent request.

Trigger Job-API:
==============
curl -X POST -H "Jenkins-Crumb:b27fadd2c583cdfd57526babeaa7ec0f" --user admin:f78598af09db161f9c5eea94774f2553  --header 'content-type: application/x-www-form-urlencoded' --data-urlencode json='{"parameter": [{"name":"id", "value":"123"}, {"name":"verbosity", "value":"high"}]}' http://jenkins-orch.vici.verizon.com:8080/job/VNF-Onboarding/job/VNF-Onboard.Download.VNF.Packages1/build



I have updated the Jenkins job with remote build “token” xyz.

[root@BBTPNJ33P0K ~]# curl  --user ranjasu:Verizon1 http://jenkins-orch.vici.verizon.com:8080/crumbIssuer/api/xml?xpath=concat\(//crumbRequestField,%22:%22,//crumb\)
Jenkins-Crumb:8ed7365145722c7edc1bc3fb6cf06457[root@BBTPNJ33P0K ~]#
[root@BBTPNJ33P0K ~]#
 [root@BBTPNJ33P0K ~]#
[root@BBTPNJ33P0K ~]#
[root@BBTPNJ33P0K ~]# curl -X POST -H "Jenkins-Crumb:8ed7365145722c7edc1bc3fb6cf06457" --user ranjasu:Verizon1 http://jenkins-orch.vici.verizon.com:8080/job/VNF_Onboarding_CICD_Pipeline/job/VNF_Package_Upload/build?token=xyz
