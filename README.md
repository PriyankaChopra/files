
swagger-ui
http://10.75.14.133:8090/swagger-ui.html#/

rest url
http://10.75.14.133:8090/settings/nfvo


request data for NFVO registration
{
  "sOrchType": "HP",
  "sPassword": "Welcome@1234",
  "sTargetURL": "http://10.75.14.83:8080",
  "sUsername": "vdsi_onb_vnf_mgr@vdsi"
}

response
{
  "nfvoId": "d64bc8b4-077d-3d34-98e5-d7afd45c7650"
}

rest url
http://10.75.14.133:8090/settings/applications


request data for Register OSS

{
  "appName": "MyOSS",
  "domainId": "6ea7a82f-1f7c-42d7-abe4-2c6d92d94d30",
  "modeInstanceId": "45698bbf-0419-4be4-acfe-4427c00054f7",
  "modeInstanceUndeployId": "7c10bed4-6aa0-47f2-a54c-00515a410b54",
  "nfvoId": "d64bc8b4-077d-3d34-98e5-d7afd45c7650",
  "orchType": "HP",
  "orgId": "b877eb45-c18d-46eb-8a79-d20c3204d23d",
  "resourceArtifactId": "c4ad5969-f921-3552-8c66-7828a6b5d306",
  "tenantId": "f8ff51d0-3bac-4dbb-998d-d7a155aaf384",
  "vnfGroupId": "43b5dfee-ec46-4101-aaa2-ca412f7ba056"
}

Response
{
  "ossRegistrationId": "b91b63ac-d9eb-30b8-91e7-c64b2e85870d"
}





public boolean triggerJenkinsValidation(String vnfId){
		boolean status = false;
		try {String line;
		String crumbVal ="";
		Process process1 = Runtime.getRuntime().exec(new String[] {"/bin/sh", "-c", "curl --user ranjasu:Verizon1 http://jenkins-orch.vici.verizon.com:8080/crumbIssuer/api/xml?xpath=concat\\(//crumbRequestField,%22:%22,//crumb\\)"});
		process1.waitFor();
		Integer result = process1.exitValue();
		System.out.println("Exit_status : "+ result);
		InputStream stderr = process1.getErrorStream ();
		InputStream stdout = process1.getInputStream ();

		BufferedReader reader = new BufferedReader (new InputStreamReader(stdout));
		BufferedReader errorReader = new BufferedReader (new InputStreamReader(stderr));
		crumbVal = reader.readLine();
		System.out.println("CrumbValue: "+crumbVal);
		/*
        while ((line = reader.readLine ()) != null) {
                System.out.println ("Stdout: " + line);
        }
        while ((line = errorReader.readLine ()) != null) {
                System.out.println ("Stderr: " + line);
        }
		 */

		String cmd2 =  "curl -X POST -H \""+crumbVal+"\""+" --user ranjasu:Verizon1 http://jenkins-orch.vici.verizon.com:8080/job/VNF_Onboarding_CICD_Pipeline/job/VNF_Package_Upload/buildWithParameters?token=xyz --data 'VNF_ID="+vnfId+"'";
		System.out.println("CMD2: "+ cmd2);
		//Process process2 = Runtime.getRuntime().exec(new String[] {"/bin/sh", "-c", "curl -X POST -H \""+crumbVal+"\""+" --user ranjasu:Verizon1 http://jenkins-orch.vici.verizon.com:8080/job/VNF_Onboarding_CICD_Pipeline/job/VNF_Package_Upload/build?token=xyz"});

		Process process2 = Runtime.getRuntime().exec(new String[] {"/bin/sh", "-c", cmd2});
		process2.waitFor();
		Integer result2 = process2.exitValue();
		System.out.println("ExitVale: "+ result2);
		InputStream stderr2 = process2.getErrorStream ();
		InputStream stdout2 = process2.getInputStream ();
		/*
        BufferedReader reader2 = new BufferedReader (new InputStreamReader(stdout2));
        BufferedReader errorReader2 = new BufferedReader (new InputStreamReader(stderr2));
        crumbVal = reader2.readLine();
        while ((line = reader2.readLine ()) != null) {
                System.out.println ("Stdout: " + line);
        }
        while ((line = errorReader2.readLine ()) != null) {
                System.out.println ("Stderr: " + line);
        }
		 */

		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return status;
	}
