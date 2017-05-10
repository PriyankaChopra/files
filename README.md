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



try {
			String line;
			Scanner scan = new Scanner(System.in);

//			Process process = Runtime.getRuntime().exec(new String[] {"/bin/sh","-c", "sshpass -p 'sdnnfv@123' scp /home/sdnuser/ys.json invlab09@10.76.110.110:/tmp/"});
			
//			Process process = Runtime.getRuntime().exec(new String[] {"/bin/sh","-c", "ifconfig"});
			
//			ProcessBuilder builder = new ProcessBuilder("/bin/sh","-c", "ifconfig111");
			ProcessBuilder builder = new ProcessBuilder("/bin/sh","-c", "ifconfig");
			builder.redirectErrorStream(true);
			Process process = builder.start();
			
			process.waitFor();
			Integer result = process.exitValue();
			System.out.println(result);
			OutputStream stdin = process.getOutputStream ();
			InputStream stderr = process.getErrorStream ();
			InputStream stdout = process.getInputStream ();

			BufferedReader reader = new BufferedReader (new InputStreamReader(stdout));
			BufferedReader errorReader = new BufferedReader (new InputStreamReader(stderr));

			while ((line = reader.readLine ()) != null) {
			    System.out.println ("Stdout: " + line);
			}
			while ((line = errorReader.readLine ()) != null) {
			    System.out.println ("Stderr: " + line);
			}
			
			
			
			/*final BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(stdin));
			Thread T=new Thread(new Runnable() {
			    @Override
			    public void run() {
			        while(true)
			        {
			            String input = scan.nextLine();
			            input += "\n";
			            try {
			                writer.write(input);
			                writer.flush();
			            } catch (IOException e) {
			                // TODO Auto-generated catch block
			                e.printStackTrace();
			            }

			        }

			    }
			} );
			T.start();
			while ((line = reader.readLine ()) != null) {
			    System.out.println ("Stdout: " + line);
			}*/
			
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
